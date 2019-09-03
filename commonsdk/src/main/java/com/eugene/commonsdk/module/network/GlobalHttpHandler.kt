package com.eugene.commonsdk.module.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 处理 Http 请求和响应结果的处理类
 * 使用 {@link RepositoryConfigModule.Builder#globalHttpHandler(GlobalHttpHandler)} 方法配置
 */
interface GlobalHttpHandler {

    fun onHttpResultResponse(httpResult: String, chain: Interceptor.Chain, response: Response): Response

    fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request

    companion object {

        //空实现
        val EMPTY: GlobalHttpHandler = object : GlobalHttpHandler {
            override fun onHttpResultResponse(httpResult: String, chain: Interceptor.Chain, response: Response): Response {
                //不管是否处理,都必须将response返回出去
                return response
            }

            override fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request {
                //不管是否处理,都必须将request返回出去
                return request
            }
        }
    }

}