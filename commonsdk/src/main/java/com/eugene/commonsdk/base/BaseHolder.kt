package com.eugene.commonsdk.base

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val views = SparseArray<View>()
    private var clickListener: ((View, Int) -> Unit)? = null

    init {
        itemView.setOnClickListener(this)
    }

    /**
     * 绑定数据
     */
    abstract fun onBindViewData(data: T, pos: Int)

    /**
     * 在 Activity 的 onDestroy 中使用 [DefaultAdapter.releaseAllHolder] 方法 (super.onDestroy() 之前)
     * [BaseHolder.onRelease] 才会被调用, 可以在此方法中释放一些资源
     */
    open fun onRelease() {

    }

    override fun onClick(view: View) {
        clickListener?.invoke(view, adapterPosition)
    }

    fun setOnClickListener(listener: (View, Int) -> Unit) {
        this.clickListener = listener
    }

    fun <V : View> findView(id: Int): V {
        var view = views[id]
        if (view == null) {
            view = itemView.findViewById(id)
            views[id, view]
        }
        return view as V
    }

}