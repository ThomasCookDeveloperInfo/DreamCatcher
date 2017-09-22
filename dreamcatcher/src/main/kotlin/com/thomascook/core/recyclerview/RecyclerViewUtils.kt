package com.thomascook.core.recyclerview

import android.os.Handler
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

/**
 * Helper utility functions for sending notifications to a [RecyclerView]
 */
object RecyclerViewUtils {
    fun notifyItemRangeInserted(
            recyclerView: RecyclerView,
            adapter: RecyclerView.Adapter<*>, startPos: Int, itemCount: Int) {

        if (recyclerView.isComputingLayout) {
            Handler().post({ adapter.notifyItemRangeInserted(startPos, itemCount) })
        } else {
            adapter.notifyItemRangeInserted(startPos, itemCount)
        }
    }

    fun notifyItemInserted(
            recyclerView: RecyclerView,
            adapter: RecyclerView.Adapter<*>, startPos: Int) {
        notifyItemRangeInserted(recyclerView, adapter, startPos, 1)
    }

    fun notifyItemRangeRemoved(
            recyclerView: RecyclerView,
            adapter: RecyclerView.Adapter<*>, startPos: Int, itemCount: Int) {

        if (recyclerView.isComputingLayout) {
            Handler().post({ adapter.notifyItemRangeRemoved(startPos, itemCount) })
        } else {
            adapter.notifyItemRangeRemoved(startPos, itemCount)
        }
    }

    fun notifyItemRemoved(
            recyclerView: RecyclerView,
            adapter: RecyclerView.Adapter<*>, startPos: Int) {
        notifyItemRangeRemoved(recyclerView, adapter, startPos, 1)
    }

    fun notifyItemRangeChanged(
            recyclerView: RecyclerView,
            adapter: RecyclerView.Adapter<*>, startPos: Int, itemCount: Int) {

        if (recyclerView.isComputingLayout) {
            Handler().post({ adapter.notifyItemRangeChanged(startPos, itemCount) })
        } else {
            adapter.notifyItemRangeChanged(startPos, itemCount)
        }
    }

    fun notifyItemChanged(
            recyclerView: RecyclerView,
            adapter: RecyclerView.Adapter<*>, startPos: Int) {
        notifyItemRangeChanged(recyclerView, adapter, startPos, 1)
    }

    fun clearRecyclerView(recyclerView: RecyclerView,
                          adapter: RecyclerViewBase.ViewAdapter<*>,
                          startPos: Int, length: Int) {
        for (index in startPos + length downTo startPos) {
            adapter.removeData(index)
        }
        notifyItemRangeRemoved(recyclerView,
                adapter, startPos, length)
    }
}

/**
 * Used to create a [DiffUtil.Callback] to be used with [android.support.v7.widget.RecyclerView]
 * in order to issue list changes notifications.
 */
class RecyclerDiffCallback(private val oldList: List<RecyclerViewBase.ViewModelBase>,
                           private val newList: List<RecyclerViewBase.ViewModelBase>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}

