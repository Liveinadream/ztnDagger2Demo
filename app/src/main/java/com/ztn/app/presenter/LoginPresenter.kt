package com.ztn.app.presenter

import com.ztn.app.base.BasePresenter
import com.ztn.app.base.contract.LoginContract
import com.ztn.app.model.http.api.ZhihuApis
import com.ztn.app.rx.RxThread
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
open class LoginPresenter @Inject constructor() :
    BasePresenter<LoginContract.View>(),
    LoginContract.Present {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: LoginContract.View

    override fun attachView(mRootView: LoginContract.View) {
        super.attachView(mRootView)
        view = mRootView
    }

    override fun login() {

        view.loading()

//        subscriptions.add(api.getWelcomeInfo("dadsa")
//            .compose(rxThread.applyAsync())
//            .doOnTerminate { view.dismissLoading() }
//            .subscribe({
//                view.loginSuccess()
//            }, {})
//        )


    }
}

