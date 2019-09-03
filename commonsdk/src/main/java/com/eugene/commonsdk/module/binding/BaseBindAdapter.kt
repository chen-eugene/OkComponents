package com.eugene.commonsdk.module.binding

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import com.chad.library.adapter.base.BaseQuickAdapter
import com.eugene.commonsdk.R


/**
 * DataBinding BaseBindAdapter
 */
abstract class BaseBindAdapter<T>(@LayoutRes layoutResId: Int, data: List<T>?) : BaseQuickAdapter<T, BaseBindHolder>(layoutResId, data) {

    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false)
                ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

}
