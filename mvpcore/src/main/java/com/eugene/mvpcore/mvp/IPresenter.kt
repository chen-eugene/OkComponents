package com.eugene.mvpcore.mvp

import android.app.Activity

/**
 * 框架要求框架中的每个 Presenter 都需要实现此类,以满足规范
 */
interface IPresenter {

    /**
     * 做一些初始化操作
     */
    fun onStart()

    /**
     * 在框架中 [Activity.onDestroy] 时会默认调用 [IPresenter.onDestroy]
     */
    fun onDestroy()


}