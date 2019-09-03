package com.eugene.mvpcore.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eugene.commonsdk.base.service.IActivity
import com.eugene.commonsdk.integration.cache.Cache
import com.eugene.commonsdk.integration.cache.CacheType
import com.eugene.commonsdk.integration.lifecycle.IActivityLifecycle
import com.eugene.commonsdk.utils.ArmsUtils
import com.eugene.mvpcore.mvp.IPresenter
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

open abstract class BaseActivity<P : IPresenter> : AppCompatActivity(), IActivity, IActivityLifecycle {

    protected val TAG = this.javaClass.simpleName
    private val mLifecycleSubject = BehaviorSubject.create<ActivityEvent>()
    private var mCache: Cache<String, Any>? = null

    @set:Inject
    var mPresenter: P? = null

    @Synchronized
    override fun provideCache(): Cache<String, Any>? {
        if (mCache == null) {
            val temp = ArmsUtils.obtainSdkComponentFromContext(this)?.cacheFactory()?.build(CacheType.ACTIVITY_CACHE)
            if (temp != null)
                mCache = temp as Cache<String, Any>
        }
        return mCache
    }

    override fun provideLifecycleSubject(): Subject<ActivityEvent> {
        return mLifecycleSubject
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutResID = initLayout(savedInstanceState)
        //如果initView返回0,框架则不会调用setContentView()
        if (layoutResID != 0) {
            setContentView(layoutResID)
        }

        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null)
            mPresenter!!.onDestroy()//释放资源
        this.mPresenter = null
    }

    /**
     * 是否使用 EventBus
     * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Arms 的体积
     *
     * @return 返回 {@code true} (默认为使用 {@code true}), Arms 会自动注册 EventBus
     */
    override fun useEventBus(): Boolean {
        return true
    }


    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link com.jess.arms.base.BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    override fun useFragment(): Boolean {
        return true
    }
}
