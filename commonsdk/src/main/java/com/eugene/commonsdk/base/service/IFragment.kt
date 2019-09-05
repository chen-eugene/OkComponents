package com.eugene.commonsdk.base.service

import android.app.Activity
import android.os.Bundle
import android.os.Message
import android.view.View
import com.eugene.commonsdk.integration.cache.Cache

/**
 * 框架要求框架中的每个 {@link Fragment} 都需要实现此类,以满足规范
 */
interface IFragment {

    /**
     * 提供在 [Fragment] 生命周期内的缓存容器, 可向此 [Fragment] 存取一些必要的数据
     * 此缓存容器和 [Fragment] 的生命周期绑定, 如果 [Fragment] 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 [LifecycleModel](https://github.com/JessYanCoding/LifecycleModel)
     *
     * @return like [LruCache]
     */
    fun provideCache(): Cache<String, Any>?

    /**
     * 是否使用 EventBus
     * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Arms 的体积
     *
     * @return 返回 `true`, Arms 会自动注册 EventBus
     */
    fun useEventBus(): Boolean

    /**
     * 初始化布局
     *
     * @return
     */
    fun initLayout(savedInstanceState: Bundle?): Int

    /**
     * 初始化 View
     *
     * @param view
     * @param savedInstanceState
     * @return
     */
    fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 [Message], 通过 what 字段来区分不同的方法, 在 [.setData]
     * 方法中就可以 `switch` 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     *
     *
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 [.setData] 方法时 [Fragment.onCreate] 还没执行
     * 但在 [.setData] 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 [Fragment.onCreate] 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 [.setData], 在 [.initData] 中初始化就可以了
     *
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * [.setData] 框架是不会调用的, 是拿给开发者自己去调用的, 让 [Activity] 或者其他类可以和 [Fragment] 通信,
     * 并且因为 [.setData] 是 [IFragment] 的方法, 所以你可以通过多态, 持有父类,
     * 不持有具体子类的方式就可以和子类 [Fragment] 通信, 这样如果需要替换子类, 就不会影响到其他地方,
     * 并且 [.setData] 可以通过传入 [Message] 作为参数, 使外部统一调用 [.setData],
     * 方法内部再通过 `switch(message.what)` 的方式, 从而在外部调用方式不变的情况下, 却可以扩展更多的方法,
     * 让方法扩展更多的参数, 这样不管 [Fragment] 子类怎么变, 它内部的方法以及方法的参数怎么变, 却不会影响到外部调用的任何一行代码
     *
     * @param data 当不需要参数时 `data` 可以为 `null`
     */
    fun setData(data: Any?)

    /**
     * 是否依赖注入，如果不需要，则重写此方法，返回 false
     *
     * @return true: 进行依赖注入；false:不进行依赖注入
     */
    fun injectable(): Boolean
}