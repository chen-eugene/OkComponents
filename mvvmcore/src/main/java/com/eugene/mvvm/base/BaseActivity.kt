package com.eugene.mvvm.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import com.eugene.commonsdk.base.service.IActivity
import com.eugene.commonsdk.integration.cache.Cache
import com.eugene.commonsdk.integration.cache.CacheType
import com.eugene.commonsdk.utils.ArmsUtils
import com.eugene.mvvm.mvvm.IViewModel
import javax.inject.Inject

/**
 * 如果只使用 DataBinding, 则 VM 的泛型可以传BaseViewModel
 */
abstract class BaseActivity<DB : ViewDataBinding, VM : IViewModel> : AppCompatActivity(), IActivity {

    private var mCache: Cache<String, Any>? = null

    /**
     * ViewDataBinding
     */
    protected var mBinding: DB? = null

    /**
     * instance in subclass; 自动销毁
     */
    protected var mViewModel: VM? = null

    /**
     * MVVM ViewModel ViewModelProvider.Factory
     */
    @set:Inject
    var mViewModelFactory: ViewModelProvider.Factory? = null

    @Synchronized
    override fun provideCache(): Cache<String, Any>? {
        if (mCache == null) {
            val temp = ArmsUtils.obtainSdkComponentFromContext(this)?.cacheFactory()?.build(CacheType.ACTIVITY_CACHE)
            if (temp != null)
                mCache = temp as Cache<String, Any>
        }
        return mCache
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置DataBinding
        val layoutId = initLayout(savedInstanceState)
        if (layoutId > 0)
            mBinding = DataBindingUtil.setContentView(this, layoutId)

        if (mViewModel != null)
            lifecycle.addObserver(mViewModel as LifecycleObserver)

        initView(savedInstanceState)

        initData(savedInstanceState)
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun useFragment(): Boolean {
        return true
    }

    override fun injectable(): Boolean {
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        this.mBinding = null
//        this.mViewModelFactory = null
        //移除LifecycleObserver
        if (mViewModel != null)
            lifecycle.removeObserver(mViewModel as LifecycleObserver)

        this.mViewModel = null
    }

}