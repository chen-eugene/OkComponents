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
package com.eugene.zhihu.mvp.model

import com.eugene.commonsdk.di.scope.ActivityScope
import com.eugene.commonsdk.integration.IRepositoryManager
import com.eugene.mvpcore.mvp.BaseModel
import com.eugene.zhihu.mvp.contract.DetailContract
import com.eugene.zhihu.mvp.contract.ZhihuHomeContract
import com.eugene.zhihu.mvp.model.api.service.ZhihuService
import com.eugene.zhihu.mvp.model.entity.DailyListBean
import com.eugene.zhihu.mvp.model.entity.ZhihuDetailBean
import io.reactivex.Observable
import javax.inject.Inject

/**
 * ================================================
 * 展示 Model 的用法
 *
 * @see [Model wiki 官方文档](https://github.com/JessYanCoding/MVPArms/wiki.2.4.3)
 * Created by JessYan on 09/04/2016 10:56
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
@ActivityScope
class ZhihuModel @Inject constructor(repositoryManager: IRepositoryManager?)
    : BaseModel(repositoryManager), ZhihuHomeContract.Model, DetailContract.Model {

    override fun getDailyList(): Observable<DailyListBean>? {
        return mRepositoryManager?.obtainRetrofitService<ZhihuService>(ZhihuService::class.java)
                ?.getDailyList()
    }

    override fun getDetailInfo(id: Int): Observable<ZhihuDetailBean>? {
        return mRepositoryManager?.obtainRetrofitService<ZhihuService>(ZhihuService::class.java)
                ?.getDetailInfo(id)
    }
}
