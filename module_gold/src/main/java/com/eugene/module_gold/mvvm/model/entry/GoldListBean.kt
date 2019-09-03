package com.eugene.module_gold.mvvm.model.entry

data class GoldListBean(val objectId: String,
                        val createdAt: String,
                        val title: String,
                        val collectionCount: Int,
                        val commentsCount: Int,
                        val url: String,
                        val user: GoldListUserBean,
                        val screenshot: GoldListScreenshotBean)

data class GoldListUserBean(val username: String)

data class GoldListScreenshotBean(val url: String)