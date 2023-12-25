package com.app.matrimony.ui.main

import android.content.ClipData
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.matrimony.R
import com.app.matrimony.utils.Constants.AdapterOptions.ADD_PHOTO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var photos = ArrayList<String>()

    private val eventChannel = Channel<UpdateListState>()
    var eventChannelFlow = eventChannel.receiveAsFlow()

    var subTitle = MutableLiveData<Int>()

    private fun sendEvent(state: UpdateListState) {
        viewModelScope.launch {
            eventChannel.send(state)
        }
    }

    fun insertAddPhotoItem() {
        if (!photos.contains(ADD_PHOTO) && photos.isEmpty()) {
            photos.add(ADD_PHOTO)
            sendEvent(UpdateListState.InsertState(0))
            subTitle.value =
                if (photos.contains(ADD_PHOTO)) R.string.title_add_photo else R.string.title_drag_photo
        }
    }

    private fun removeAddPhotoItem() {
        if (photos.contains(ADD_PHOTO)) {
            photos.remove(ADD_PHOTO)
            sendEvent(UpdateListState.DeleteState(0))
            subTitle.value =
                if (photos.contains(ADD_PHOTO)) R.string.title_add_photo else R.string.title_drag_photo
        }
    }

    fun removePhoto(item: String) {
        photos.indexOf(item).also {
            if (it > -1) {
                photos.removeAt(it)
                sendEvent(UpdateListState.DeleteState(it))
                insertAddPhotoItem()
            }
        }
    }

    fun bulkAddPhotos(clipData: ClipData) {
        removeAddPhotoItem()
        val lastIndex = photos.lastIndex
        for (i in 0 until clipData.itemCount) {
            if (photos.size < 6) {
                photos.add(clipData.getItemAt(i).uri.toString())
            } else {
                sendEvent(UpdateListState.ErrorState(R.string.toast_max_limit))
                break
            }
        }
        if (lastIndex != photos.lastIndex) {
            sendEvent(UpdateListState.BulkInsertState(lastIndex + 1, photos.lastIndex - lastIndex))
        }
    }

    fun addPhotos(uri: Uri): Boolean {
        removeAddPhotoItem()
        if (photos.size < 6) {
            photos.add(uri.toString())
            sendEvent(UpdateListState.InsertState(photos.lastIndex))
        } else {
            sendEvent(UpdateListState.ErrorState(R.string.toast_max_limit))
            return false
        }
        return true
    }

}

sealed class UpdateListState {
    data class DeleteState(val position: Int) : UpdateListState()
    data class InsertState(val position: Int) : UpdateListState()
    data class BulkInsertState(val startPos: Int, val count: Int) : UpdateListState()
    data class ErrorState(val res: Int) : UpdateListState()
}