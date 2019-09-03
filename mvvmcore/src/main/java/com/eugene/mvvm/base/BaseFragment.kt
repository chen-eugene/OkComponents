package com.eugene.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.eugene.commonsdk.base.service.IFragment
import com.eugene.commonsdk.integration.cache.Cache
import com.eugene.commonsdk.integration.cache.CacheType
import com.eugene.commonsdk.utils.ArmsUtils
import com.eugene.mvvm.mvvm.IViewModel

/**
 * 如果只使用 DataBinding, 则 VM 的泛型可以传 {@link me.xiaobailong24.mvvmarms.mvvm.BaseViewModel}
 */
abstract class BaseFragment<DB : ViewDataBinding, VM : IViewModel> : Fragment(), IFragment {

    protected val TAG = this::class.java.name

    private var mCache: Cache<String, Any>? = null

    /**
     * 是否第一次加载，用于懒加载
     */
    protected var mFirst = true

    protected var mContentView: View? = null

    /**
     * ViewDataBinding
     */
    protected var mBinding: DB? = null


    protected var mViewModel: VM? = null

//    @set:Inject
//    protected var mViewModelFactory: ViewModelProvider.Factory? = null

    @Synchronized
    override fun provideCache(): Cache<String, Any>? {
        if (mCache == null) {
            val temp = ArmsUtils.obtainSdkComponentFromContext(activity)?.cacheFactory()?.build(CacheType.ACTIVITY_CACHE)
            if (temp != null)
                mCache = temp as Cache<String, Any>
        }
        return mCache
    }

    /**
     * 子类通过重写onCreateView，调用setOnContentView进行布局设置，否则contentView==null，返回null
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        onCreateViewLazy(savedInstanceState)

        //不使用懒加载，直接继承BaseFragment
        if (mContentView == null) {
            val layoutId = initLayout(savedInstanceState)
            if (layoutId > 0)
                mContentView = inflater.inflate(layoutId, null)
        }

        return if (mContentView == null)
            super.onCreateView(inflater, container, savedInstanceState)
        else
            mContentView
    }

    /**
     * 懒加载实现
     */
    protected open fun onCreateViewLazy(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mViewModel != null)
            lifecycle.addObserver(mViewModel as LifecycleObserver)
        initData(savedInstanceState)
    }

    override fun setData(data: Any?) {
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun injectable(): Boolean {
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.mContentView = null
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mBinding = null
//        this.mViewModelFactory = null
        if (mViewModel != null)
            lifecycle.removeObserver(mViewModel as LifecycleObserver)
        this.mViewModel = null
    }

}