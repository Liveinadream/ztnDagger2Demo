package com.ztn.app.base

import com.ztn.app.di.component.ActivityComponent
import com.ztn.app.di.component.DaggerActivityComponent
import com.ztn.app.di.module.ActivityModule
import javax.inject.Inject


/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
abstract class BaseActivity<T : BasePresenter<out BaseView>> : SimpleActivity(), BaseView {

    @Inject
    protected lateinit var mPresenter: T


    fun getActivityComponent(): ActivityComponent {
        return DaggerActivityComponent
            .builder()
            .appComponent(BaseApplication.instance.appComponent)
            .build()
    }

    protected val activityModule: ActivityModule
        get() = ActivityModule(this)

    override fun onViewCreated() {
        super.onViewCreated()
//        mPresenter.attachView(this)
        initInject()
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    protected abstract fun initInject()
}