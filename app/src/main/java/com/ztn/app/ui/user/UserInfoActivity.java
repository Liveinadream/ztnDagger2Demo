package com.ztn.app.ui.user;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ztn.app.BR;
import com.ztn.app.R;
import com.ztn.app.base.AppViewModelFactory;
import com.ztn.app.databinding.ActivityUserInfoBinding;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * Created by 冒险者ztn on 2019-10-07.
 * 用户信息界面
 */
public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding, UserInfoViewModel> {

    public static void startWithNothing(Context context) {
        context.startActivity(new Intent(context, UserInfoActivity.class));
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_user_info;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public UserInfoViewModel initViewModel() {
        AppViewModelFactory appViewModelFactory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, appViewModelFactory).get(UserInfoViewModel.class);
    }
}
