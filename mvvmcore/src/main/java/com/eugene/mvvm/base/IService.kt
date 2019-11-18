package com.eugene.mvvm.base

interface IService {

    /**
     * LiveData注册Observer
     */
    fun registerObserver()

    /**
     * 显示加载
     */
    fun showLoading()

    /**
     * 隐藏加载
     */
    fun hideLoading()

}