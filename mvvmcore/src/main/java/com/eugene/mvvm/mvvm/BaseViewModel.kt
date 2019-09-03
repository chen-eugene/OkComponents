package com.eugene.mvvm.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import org.greenrobot.eventbus.EventBus

/**
 * MVVM BaseViewModel (ViewModel 不再持有 View，而是 store and manage UI-related data)
 */
abstract class BaseViewModel<M : IModel>(application: Application) : AndroidViewModel(application), IViewModel {

    constructor(application: Application, model: M) : this(application) {
        this.mModel = model
    }

    protected var mModel: M? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onStart() {
        if (useEventBus())
        //注册eventbus
            EventBus.getDefault().register(this)
    }

    /**
     * 是否使用 EventBus
     *
     * @return True if use
     */
    protected fun useEventBus() = true

    override fun onCleared() {
        super.onCleared()
        if (useEventBus())
            EventBus.getDefault().unregister(this)
    }

}