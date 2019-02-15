package com.ztn.app.ui

import android.content.Context
import android.content.Intent
import com.ztn.app.base.BaseActivity
import com.ztn.app.base.contract.LoginContract
import com.ztn.app.presenter.LoginPresenter
import com.ztn.app.R
import com.ztn.common.customs.CustomProgressDialog
import com.ztn.common.utils.wayutils.DialogUtils
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View {

    companion object {
        fun startWithNothing(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    private var customProgressDialog: CustomProgressDialog? = null

    override fun loading() {
        customProgressDialog = DialogUtils.creat(this, false, "加载中")

    }

    override fun dismissLoading() {
        customProgressDialog?.dismiss()
    }


    override fun initInject() {
        getActivityComponent().inject(this)
    }


    override fun initEventAndData() {
    }

    override fun loginSuccess() {
    }

    override fun onViewCreated() {
        super.onViewCreated()
        mPresenter.attachView(this)
        login.setOnClickListener {
            mPresenter.login()
        }
    }
}

