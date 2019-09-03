package com.eugene.commonsdk.base.service

import android.app.Activity
import android.os.Bundle
import com.eugene.commonsdk.integration.EventBusManager
import com.eugene.commonsdk.utils.ArmsUtils

const val LAYOUT_LINEARLAYOUT = "LinearLayout"
const val LAYOUT_FRAMELAYOUT = "FrameLayout"
const val LAYOUT_RELATIVELAYOUT = "RelativeLayout"
const val ACTIVITY_DELEGATE = "ACTIVITY_DELEGATE"

/**
 *  {@link Activity} 代理类,用于框架内部在每个 {@link Activity} 的对应生命周期中插入需要的逻辑
 */
interface IActivityDelegate {

    fun onCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(outState: Bundle)

    fun onDestroy()
}

class ActivityDelegateImpl(activity: Activity) : IActivityDelegate {
    private var mActivity: Activity? = null
    private var iActivity: IActivity? = null

    init {
        this.mActivity = activity
        this.iActivity = activity as IActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity?.useEventBus() == true) {
            //注册到事件主线
            EventBusManager.getInstance().register(mActivity)
        }
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onDestroy() {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity != null && iActivity?.useEventBus() == true)
            EventBusManager.getInstance().unregister(mActivity)
        this.iActivity = null
        this.mActivity = null
    }

}


