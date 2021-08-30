package com.ztn.app.presenter

import com.ztn.app.base.BasePresenter
import com.ztn.app.base.contract.LoginContract
import com.ztn.network.interfaces.ZhihuApis
import com.ztn.app.rx.RxThread
import javax.inject.Inject


/**
 * Created by 冒险者ztn on 2019/2/12.
 */
//private val iVew: ICommonView
open class LoginPresenter @Inject
constructor(private val api: ZhihuApis, private val rxThread: RxThread) :
    BasePresenter<LoginContract.View>(),
    LoginContract.Present {

    override fun login() {

        requireRootView().loading()
        compositeDisposable.add(api.getWelcomeInfo("dadsa")
            .compose(rxThread.applyAsync())
            .doOnTerminate { requireRootView().dismissLoading() }
            .subscribe({
                requireRootView().loginSuccess()
            }, {})
        )


    }
}

