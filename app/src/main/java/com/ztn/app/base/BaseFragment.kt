package com.ztn.app.base

import com.ztn.app.di.component.DaggerFragmentComponent
import com.ztn.app.di.component.FragmentComponent
import com.ztn.app.di.module.FragmentModule
import javax.inject.Inject

/**
 * Created by chenxz on 2018/4/21.
 */
abstract class BaseFragment<T : BasePresenter<out BaseView>> : SimpleFragment() {

    @Inject
    protected lateinit var mPresenter: T


    fun getFragmentComponent(): FragmentComponent {
        return DaggerFragmentComponent
            .builder()
            .appComponent(BaseApplication.instance.appComponent)
            .fragmentModule(fragmentModule)
            .build()
    }

    protected val fragmentModule
        get() = FragmentModule(this)

    protected abstract fun initInject()

    override fun initView() {
        initInject()
//        mPresenter.attachView(this)
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }
}