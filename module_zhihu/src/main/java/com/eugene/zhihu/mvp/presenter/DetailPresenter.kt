package com.eugene.zhihu.mvp.presenter

import android.app.Application
import com.eugene.commonsdk.di.scope.ActivityScope
import com.eugene.commonsdk.integration.AppManager
import com.eugene.commonsdk.module.imageloader.ImageLoader
import com.eugene.mvpcore.mvp.BasePresenter
import com.eugene.mvpcore.utils.RxLifecycleUtils
import com.eugene.zhihu.mvp.contract.DetailContract
import com.eugene.zhihu.mvp.model.entity.ZhihuDetailBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

@ActivityScope
class DetailPresenter @Inject constructor() : BasePresenter<DetailContract.Model, DetailContract.View>() {

    @set:Inject
    internal var mApplication: Application? = null

    @set:Inject
    internal var mErrorHandler: RxErrorHandler? = null

    @set:Inject
    internal var mImageLoader: ImageLoader? = null

    @set:Inject
    internal var mAppManager: AppManager? = null


    fun requestDetailInfo(id: Int) {
        mModel?.getDetailInfo(id)
            ?.subscribeOn(Schedulers.io())
            //遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            ?.retryWhen(RetryWithDelay(3, 2))
            ?.doOnSubscribe {
                mRootView?.showLoading()
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doFinally {
                mRootView?.hideLoading()
            }
            //使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
            ?.compose(RxLifecycleUtils.bindToLifecycle<ZhihuDetailBean>(mRootView!!))
            ?.subscribe(object : ErrorHandleSubscriber<ZhihuDetailBean>(mErrorHandler) {
                override fun onNext(t: ZhihuDetailBean) {
                    mRootView?.shonContent(t)
                }
            })

    }

    override fun onDestroy() {
        super.onDestroy()
        this.mErrorHandler = null
        this.mAppManager = null
        this.mImageLoader = null
        this.mApplication = null
    }
}