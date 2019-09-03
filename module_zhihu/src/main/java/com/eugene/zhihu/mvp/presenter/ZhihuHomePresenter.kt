/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eugene.zhihu.mvp.presenter


import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.eugene.commonsdk.di.scope.ActivityScope
import com.eugene.commonsdk.integration.AppManager
import com.eugene.mvpcore.mvp.BasePresenter
import com.eugene.mvpcore.utils.RxLifecycleUtils
import com.eugene.zhihu.mvp.contract.ZhihuHomeContract
import com.eugene.zhihu.mvp.model.entity.DailyListBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

/**
 * ================================================
 * 展示 Presenter 的用法
 *
 * @see [Presenter wiki 官方文档](https://github.com/JessYanCoding/MVPArms/wiki.2.4.4)
 * Created by JessYan on 09/04/2016 10:59
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
@ActivityScope
class ZhihuHomePresenter @Inject constructor(model: ZhihuHomeContract.Model, rootView: ZhihuHomeContract.View)
    : BasePresenter<ZhihuHomeContract.Model, ZhihuHomeContract.View>(model, rootView) {

    @set:Inject
    internal var mErrorHandler: RxErrorHandler? = null
    @set:Inject
    internal var mAppManager: AppManager? = null
    @set:Inject
    internal var mApplication: Application? = null
    @set:Inject
    internal var mDatas: MutableList<DailyListBean.StoriesBean>? = null
    @set:Inject
    internal var mAdapter: RecyclerView.Adapter<*>? = null

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 `Presenter` 可以与 [SupportActivity] 和 [Fragment] 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    internal fun onCreate() {
        requestDailyList()//打开 App 时自动加载列表
    }

    fun requestDailyList() {
        mModel?.getDailyList()
                ?.subscribeOn(Schedulers.io())
                ?.retryWhen(RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                ?.doOnSubscribe { disposable ->
                    mRootView?.showLoading()//显示下拉刷新的进度条
                }?.subscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doFinally {
                    mRootView?.hideLoading()//隐藏下拉刷新的进度条
                }
//                ?.compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                ?.compose(RxLifecycleUtils.bindToLifecycle<DailyListBean>(mRootView!!))
                ?.subscribe(object : ErrorHandleSubscriber<DailyListBean>(mErrorHandler!!) {
                    override fun onNext(dailyListBean: DailyListBean) {
                        mDatas?.clear()
                        mDatas?.addAll(dailyListBean.stories)
                        mAdapter?.notifyDataSetChanged()
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mAdapter = null
        this.mDatas = null
        this.mErrorHandler = null
        this.mAppManager = null
        this.mApplication = null
    }
}
