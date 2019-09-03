package com.eugene.mvpcore.mvp

import android.app.Activity
import android.content.Intent
import com.eugene.commonsdk.utils.ArmsUtils

/**
 *  * 框架要求框架中的每个 View 都需要实现此类, 以满足规范
 * <p>
 * 为了满足部分人的诉求以及向下兼容, {@link IView} 中的部分方法使用 JAVA 1.8 的默认方法实现, 这样实现类可以按实际需求选择是否实现某些方法
 * 不实现则使用默认方法中的逻辑, 不清楚默认方法的请自行学习
 */
interface IView {

    /**
     * 显示加载
     */
    fun showLoading() {

    }

    /**
     * 隐藏加载
     */
    fun hideLoading() {

    }

    /**
     * 显示信息
     *
     * @param message 消息内容, 不能为 `null`
     */
    abstract fun showMessage(message: String)

    /**
     * 跳转 [Activity]
     *
     * @param intent `intent` 不能为 `null`
     */
    fun launchActivity(intent: Intent) {
        checkNotNull(intent)
        ArmsUtils.startActivity(intent)
    }

    /**
     * 杀死自己
     */
    fun killMyself() {

    }


}