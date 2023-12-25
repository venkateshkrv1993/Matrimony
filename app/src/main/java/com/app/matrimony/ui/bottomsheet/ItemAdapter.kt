package com.app.matrimony.ui.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.matrimony.databinding.LayoutItemPickerOptBinding

class ItemAdapter(
    private val items: List<ItemModel>,
    private val onOptionSelection: OnOptionSelection
) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutItemPickerOptBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]
        holder.binding.clickAction = onOptionSelection
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder internal constructor(val binding: LayoutItemPickerOptBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}
