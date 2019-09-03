package com.eugene.commonsdk.di.component

import android.app.Activity
import android.app.Application
import com.eugene.commonsdk.base.delegate.AppDelegate
import com.eugene.commonsdk.di.module.AppModule
import com.eugene.commonsdk.di.module.ClientModule
import com.eugene.commonsdk.di.module.RepositoryConfigModule
import com.eugene.commonsdk.integration.AppManager
import com.eugene.commonsdk.integration.IRepositoryManager
import com.eugene.commonsdk.integration.cache.Cache
import com.eugene.commonsdk.module.imageloader.ImageLoader
import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.ExecutorService
import javax.inject.Singleton

/**
 * 可通过 {@link ArmsUtils#obtainAppComponentFromContext(Context)} 拿到此接口的实现类
 * 拥有此接口的实现类即可调用对应的方法拿到 Dagger 提供的对应实例
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ClientModule::class, RepositoryConfigModule::class])
interface SdkComponent {

    fun application(): Application

    /**
     * 用于管理所有 [Activity]
     * 之前 [AppManager] 使用 Dagger 保证单例, 只能使用 [SdkComponent.appManager] 访问
     * 现在直接将 AppManager 独立为单例类, 可以直接通过静态方法 [AppManager.getAppManager] 访问, 更加方便
     * 但为了不影响之前使用 [SdkComponent.appManager] 获取 [AppManager] 的项目, 所以暂时保留这种访问方式
     *
     * @return [AppManager]
     */
    @Deprecated("Use {@link AppManager#getAppManager()} instead")
    fun appManager(): AppManager?

    /**
     * 用于管理网络请求层, 以及数据缓存层
     *
     * @return [IRepositoryManager]
     */
    fun repositoryManager(): IRepositoryManager?

    /**
     * RxJava 错误处理管理类
     *
     * @return [RxErrorHandler]
     */
    fun rxErrorHandler(): RxErrorHandler?

    /**
     * 图片加载管理器, 用于加载图片的管理类, 使用策略者模式, 可在运行时动态替换任何图片加载框架
     * arms-imageloader-glide 提供 Glide 的策略实现类, 也可以自行实现
     * 需要在 [ConfigModule.applyOptions] 中
     * 手动注册 [BaseImageLoaderStrategy], [ImageLoader] 才能正常使用
     *
     * @return
     */
    fun imageLoader(): ImageLoader?

    /**
     * 网络请求框架
     *
     * @return [OkHttpClient]
     */
    fun okHttpClient(): OkHttpClient?

    /**
     * Json 序列化库
     *
     * @return [Gson]
     */
    fun gson(): Gson?

    /**
     * 缓存文件根目录 (RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下), 应该将所有缓存都统一放到这个根目录下
     * 便于管理和清理, 可在 [ConfigModule.applyOptions] 种配置
     *
     * @return [File]
     */
    fun cacheFile(): File

    /**
     * 用来存取一些整个 App 公用的数据, 切勿大量存放大容量数据, 这里的存放的数据和 [Application] 的生命周期一致
     *
     * @return [Cache]
     */
    fun extras(): Cache<String, Any>?

    /**
     * 用于创建框架所需缓存对象的工厂
     *
     * @return [Cache.Factory]
     */
    fun cacheFactory(): Cache.Factory?

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return [ExecutorService]
     */
    fun executorService(): ExecutorService

    fun inject(appDelegate: AppDelegate)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(base: Application): Builder

        fun armConfigModule(repositoryConfigModule: RepositoryConfigModule?): Builder

        fun build(): SdkComponent
    }


}