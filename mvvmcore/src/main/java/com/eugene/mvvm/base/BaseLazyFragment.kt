package com.eugene.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import com.eugene.mvvm.R
import com.eugene.mvvm.mvvm.IViewModel

abstract class BaseLazyFragment<DB : ViewDataBinding, VM : IViewModel> : BaseFragment<DB, VM>() {

    /**
     * 真正要显示的View是否已经初始化
     */
    private var mIsInit = false

    private var mRootView: FrameLayout? = null

    private var savedInstanceState: Bundle? = null

    override fun onCreateViewLazy(savedInstanceState: Bundle?) {
        super.onCreateViewLazy(savedInstanceState)
        this.savedInstanceState = savedInstanceState

        //一旦isVisibleToUser==true即可对真正需要的显示内容进行加载
        if (userVisibleHint && !mIsInit) {
            setContentView()
            initView(mContentView!!, savedInstanceState)
            mIsInit = true
        } else {
            //进行懒加载
            mRootView = FrameLayout(activity?.applicationContext)
            mRootView?.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT)
            val child = LayoutInflater.from(activity?.applicationContext).inflate(R.layout.mvvm_fragment_lazy_loading, null)
            mRootView?.addView(child)
            super.mContentView = mRootView
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        //一旦isVisibleToUser==true即可进行对真正需要的显示内容的加载
        //可见，但还没被初始化
        if (isVisibleToUser && !mIsInit && mContentView != null) {
            setContentView()
            initView(mContentView!!, savedInstanceState)
            mIsInit = true
        }

        if (mIsInit && mContentView != null) {
            if (isVisibleToUser) {
                onFragmentStartLazy()
            } else {
                onFragmentStopLazy()
            }
        }
    }

    private fun setContentView() {

        val layoutResId = initLayout(savedInstanceState)
        if (layoutResId <= 0) {
            return
        }

        val child = LayoutInflater.from(activity?.applicationContext).inflate(layoutResId, mRootView, false)
        if (super.mContentView != null && mContentView?.parent != null) {
            //移除所有lazy view，加载真正要显示的view
            mRootView?.removeAllViews()
            mRootView?.addView(child)
        } else {
            super.mContentView = child
        }
    }

    /**
     * 当Fragment被滑到可见的位置时，调用
     */
    protected fun onFragmentStartLazy() {
    }

    /**
     * 当Fragment被滑到不可见的位置，offScreen时，调用
     */
    protected fun onFragmentStopLazy() {
    }
}











