package com.eugene.module_gold.mvvm.model.entry

const val NOT_FOUND = 404
const val SUCCESS = 200
const val REQUEST_REFUSED = 403
const val REQUEST_REDIRECTED = 307
const val SERVER_ERROR = 500

class BaseResp<T>(val status: Int, val data: T?, val msg: String?) {

    companion object {

        fun <T> success(data: T?): BaseResp<T> {
            return BaseResp(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): BaseResp<T> {
            return BaseResp(SERVER_ERROR, data, msg)
        }
    }


}