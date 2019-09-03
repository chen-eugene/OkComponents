package com.eugene.module_gold.mvvm.model.api.service

import com.eugene.module_gold.mvvm.model.api.GOLD_DOMAIN_NAME
import com.eugene.module_gold.mvvm.model.entry.GoldBaseResponse
import com.eugene.module_gold.mvvm.model.entry.GoldListBean
import io.reactivex.Observable
import me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * 展示 {@link Retrofit#create(Class)} 中需要传入的 ApiService 的使用方式
 * 存放关于 gold 的一些 API
 */
interface GoldService {

    /**
     * 文章列表
     */
    @Headers(DOMAIN_NAME_HEADER + GOLD_DOMAIN_NAME)
    @GET("/1.1/classes/Entry")
    fun getGoldList(@Header("X-LC-Id") id: String,
                    @Header("X-LC-Sign") sign: String,
                    @Query("where") where: String,
                    @Query("order") order: String,
                    @Query("include") include: String,
                    @Query("limit") limit: Int,
                    @Query("skip") skip: Int): Observable<GoldBaseResponse<List<GoldListBean>>>


}