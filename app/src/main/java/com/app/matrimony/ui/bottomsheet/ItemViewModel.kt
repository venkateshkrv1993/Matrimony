package com.app.matrimony.ui.bottomsheet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.matrimony.R
import com.app.matrimony.utils.Constants.PickerOptionsType.TYPE_CAMERA
import com.app.matrimony.utils.Constants.PickerOptionsType.TYPE_GALLERY
import com.app.matrimony.utils.Constants.PickerOptionsType.TYPE_WHATSAPP


class ItemViewModel : ViewModel() {

    val pickerOptions = MutableLiveData<List<ItemModel>>()

    fun loadData() {
        val list = ArrayList<ItemModel>()
        list.add(ItemModel(TYPE_GALLERY, R.drawable.ic_gallery, "Upload from Gallery"))
        list.add(ItemModel(TYPE_CAMERA, R.drawable.ic_camera, "Take a Photo"))
        list.add(
            ItemModel(
                TYPE_WHATSAPP,
                R.drawable.icon_whatsapp,
                "<html><body>Whatsapp your photos to us @<br /><small> +91 9XXXX XXXXX</small></body><html>"
            )
        )
        pickerOptions.value = list
    }

}