package com.app.matrimony.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.matrimony.databinding.LayoutItemAddPhotoBinding
import com.app.matrimony.databinding.LayoutItemPhotoBinding
import com.app.matrimony.utils.Constants.AdapterOptions.ADD_PHOTO

class PhotosAdapter(private val photos: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onClickAction: OnClickAction

    fun moveItem(from: Int, to: Int) {
        val fromData = photos[from]
        photos.removeAt(from)
        photos.add(to, fromData)
    }

    override fun getItemViewType(position: Int): Int {
        return if (photos[position] == ADD_PHOTO) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            return ViewHolderAddPhoto(
                LayoutItemAddPhotoBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
        return ViewHolder(
            LayoutItemPhotoBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.size > 0 && holder is ViewHolder) {
            holder.binding.isProfile = position == 0
        } else onBindViewHolder(holder, position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.binding.uri = photos[position]
                holder.binding.isProfile = position == 0
                if (::onClickAction.isInitialized) holder.binding.clickAction = onClickAction
            }

            is ViewHolderAddPhoto -> {
                if (::onClickAction.isInitialized) holder.binding.clickAction = onClickAction
            }
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    inner class ViewHolder internal constructor(val binding: LayoutItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    inner class ViewHolderAddPhoto internal constructor(val binding: LayoutItemAddPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}