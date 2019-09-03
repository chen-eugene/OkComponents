package com.eugene.commonsdk.module.binding

import android.view.View
import androidx.databinding.ViewDataBinding

import com.chad.library.adapter.base.BaseViewHolder
import com.eugene.commonsdk.R


/**
 * DataBinding BaseBindHolder
 */
class BaseBindHolder(view: View) : BaseViewHolder(view) {

    val binding: ViewDataBinding
        get() = itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding

}
