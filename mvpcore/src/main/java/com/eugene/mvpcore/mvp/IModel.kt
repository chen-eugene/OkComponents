package com.eugene.mvpcore.mvp

/**
 * 框架要求框架中的每个 Model 都需要实现此类,以满足规范
 */
interface IModel {

    /**
     * 在框架中 [BasePresenter.onDestroy] 时会默认调用 [IModel.onDestroy]
     */
    fun onDestroy()

}