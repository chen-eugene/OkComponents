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
package com.eugene.zhihu.mvp.model.api.service

import com.eugene.zhihu.mvp.model.api.Api.ZHIHU_DOMAIN_NAME
import com.eugene.zhihu.mvp.model.entity.DailyListBean
import com.eugene.zhihu.mvp.model.entity.ZhihuDetailBean
import io.reactivex.Observable
import me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * ================================================
 * 展示 [Retrofit.create] 中需要传入的 ApiService 的使用方式
 * 存放关于 zhihu 的一些 API
 *
 *
 * Created by JessYan on 08/05/2016 12:05
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
interface ZhihuService {
    /**
     * 最新日报
     */
    @Headers(DOMAIN_NAME_HEADER + ZHIHU_DOMAIN_NAME)
    @GET("/api/4/news/latest")
    fun getDailyList(): Observable<DailyListBean>?

    /**
     * 日报详情
     */
    @Headers(DOMAIN_NAME_HEADER + ZHIHU_DOMAIN_NAME)
    @GET("/api/4/news/{id}")
    fun getDetailInfo(@Path("id") id: Int): Observable<ZhihuDetailBean>?
}
