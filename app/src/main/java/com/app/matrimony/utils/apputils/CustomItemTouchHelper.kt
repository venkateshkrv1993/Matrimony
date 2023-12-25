package com.app.matrimony.utils.apputils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.matrimony.ui.main.PhotosAdapter
import com.app.matrimony.utils.Constants

object CustomItemTouchHelper {

    fun getCustomItemHelper(): ItemTouchHelper {
        return ItemTouchHelper(itemTouchCallback)
    }

    private val itemTouchCallback =
        object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            0
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val adapter = recyclerView.adapter as PhotosAdapter
                val from = viewHolder.absoluteAdapterPosition
                val to = target.absoluteAdapterPosition
                adapter.moveItem(from, to)
                adapter.notifyItemMoved(from, to)
                if (from == 0 || to == 0) {
                    var startPos = from
                    var endPos = to
                    if (to < from) {
                        startPos = to
                        endPos = from
                    }
                    for (i in startPos..endPos) {
                        adapter.notifyItemChanged(i, Constants.AdapterOptions.UPDATE_PHOTO)
                    }
                }
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun onSelectedChanged(
                viewHolder: RecyclerView.ViewHolder?,
                actionState: Int
            ) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1.0f
            }
        }

}