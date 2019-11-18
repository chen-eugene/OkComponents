package com.eugene.module_gold.app

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


abstract class CommonHandleSubscriber<T> : Observer<T> {

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        val msg = if (e is UnknownHostException || e is ConnectException) {
            "网络不可用"
        } else if (e is SocketTimeoutException) {
            "请求网络超时"
        } else if (e is HttpException) {
            convertStatusCode(e)
        } else if (e is JsonParseException || e is ParseException || e is JSONException || e is JsonIOException) {
            "数据解析错误"
        } else {
            ""
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

}