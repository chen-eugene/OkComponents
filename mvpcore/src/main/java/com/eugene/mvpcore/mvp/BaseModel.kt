package com.eugene.mvpcore.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.eugene.commonsdk.integration.IRepositoryManager

/**
 * 基类 Model
 * @param mRepositoryManager 用于管理网络请求层, 以及数据缓存层
 */
abstract class BaseModel(protected var mRepositoryManager: IRepositoryManager?)
    : IModel, LifecycleObserver {

    /**
     * 在框架中 [BasePresenter.onDestroy] 时会默认调用 [IModel.onDestroy]
     */
    override fun onDestroy() {
        mRepositoryManager = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }

}