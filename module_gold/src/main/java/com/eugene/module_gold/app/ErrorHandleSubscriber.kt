package com.eugene.module_gold.app

import com.airbnb.lottie.utils.Utils
import com.eugene.commonres.utils.ToastUtil
import com.eugene.module_gold.mvvm.model.entry.BaseResp
import com.eugene.module_gold.mvvm.model.entry.SUCCESS
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

const val NOT_FOUND = 404
const val REQUEST_REFUSED = 403
const val REQUEST_UNAUTHORIZED = 401
const val REQUEST_REDIRECTED = 307
const val SERVER_ERROR = 500

abstract class ErrorHandleSubscriber<T> : Observer<BaseResp<T>> {

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        //这里只是对几个常用错误进行简单的处理, 展示这个类的用法, 在实际开发中请您自行对更多错误进行更严谨的处理
        val msg = when (e) {
            is UnknownHostException, is ConnectException -> {
                "网络不可用"
            }
            is SocketTimeoutException -> {
                "请求网络超时"
            }
            is HttpException -> {
                convertStatusCode(e)
            }
            is JsonParseException, is ParseException, is JSONException, is JsonIOException -> {
                "数据解析错误"
            }
            else -> {
                "未知错误"
            }
        }

        if (msg.isBlank())
            return

//        ToastUtil.showMessage(Utils.getApp(), msg)
    }

    private fun convertStatusCode(httpException: HttpException): String {
        return when {
            httpException.code() == SERVER_ERROR -> "服务器发生错误"
            httpException.code() == NOT_FOUND -> "请求地址不存在"
            httpException.code() == REQUEST_REFUSED -> "请求被服务器拒绝"
            httpException.code() == REQUEST_UNAUTHORIZED -> "未授权"
            httpException.code() == REQUEST_REDIRECTED -> "请求被重定向到其他页面"
            else -> httpException.message()
        }
    }

    override fun onNext(t: BaseResp<T>) {
        when (t.status) {
            SUCCESS -> {
                onSuccess(t.data)
            }
            else -> {
                onFail(t)
            }
        }
    }

    abstract fun onSuccess(t: T?)

    open fun onFail(t: BaseResp<T>) {}


}