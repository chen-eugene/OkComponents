package com.eugene.commonsdk.integration

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager

import com.eugene.commonsdk.base.service.IAppLifecycle
import com.eugene.commonsdk.di.module.RepositoryConfigModule

/**
 * [IConfigModule] 可以给框架配置一些参数,需要实现 [IConfigModule] 后,在 AndroidManifest 中声明该实现类
 */
interface IConfigModule {
    /**
     * 使用[RepositoryConfigModule]给框架配置一些配置参数
     *
     * @param context
     */
    fun applyOptions(context: Context?, builder: RepositoryConfigModule.Builder)

    /**
     * 使用[IAppLifecycle]在Application的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    fun injectAppLifecycle(context: Context?, lifecycles: MutableList<IAppLifecycle>)

    /**
     * 使用[Application.ActivityLifecycleCallbacks]在Activity的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    fun injectActivityLifecycle(context: Context?, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>)


    /**
     * 使用[FragmentManager.FragmentLifecycleCallbacks]在Fragment的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    fun injectFragmentLifecycle(context: Context?, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>)
}
