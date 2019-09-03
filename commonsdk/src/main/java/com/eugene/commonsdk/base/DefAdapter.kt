package com.eugene.commonsdk.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class DefAdapter<T> : RecyclerView.Adapter<BaseHolder<T>>() {

    var datas: MutableList<T> = mutableListOf()

    /**
     * @param View
     * @param Int viewType
     * @param T data
     * @param Int position
     */
    private var listener: ((View, Int, T, Int) -> Unit)? = null

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<T> {
        val itemView = LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        val holder = getHolder(itemView, viewType)
        holder.setOnClickListener { view, position ->
            listener?.invoke(view, viewType, datas[position], position)
        }
        return holder
    }

    /**
     * 绑定数据
     */
    override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
        holder.onBindViewData(datas[position], position)
    }

    /**
     * 提供用于 `item` 布局的 `layoutId`
     */
    abstract fun getLayoutId(viewType: Int): Int

    /**
     * 让子类实现用以提供 [BaseHolder]
     */
    abstract fun getHolder(v: View, viewType: Int): BaseHolder<T>

    fun setOnItemClickListener(listener: (View, Int, T, Int) -> Unit) {
        this.listener = listener
    }

    companion object {
        /**
         * 遍历所有[BaseHolder],释放他们需要释放的资源
         *
         * @param recyclerView
         */
        fun releaseAllHolder(recyclerView: RecyclerView?) {
            recyclerView ?: return

            for (i in recyclerView.childCount - 1 downTo 0) {
                val view = recyclerView.getChildAt(i)
                val viewHolder = recyclerView.getChildViewHolder(view)
                if (viewHolder != null && viewHolder is BaseHolder<*>) {
                    viewHolder.onRelease()
                }
            }
        }
    }

}