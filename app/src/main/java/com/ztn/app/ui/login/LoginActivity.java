package com.ztn.app.ui.login;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ztn.app.BR;
import com.ztn.app.R;
import com.ztn.app.base.AppViewModelFactory;
import com.ztn.app.databinding.ActivityLoginBinding;

import me.goldze.mvvmhabit.base.BaseActivity;


/**
 * Created by 冒险者ztn on 2019-10-07.
 * mvvm 登陆界面
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    public static void startWithNothing(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    //ActivityLoginBinding类是databinding框架自定生成的,对activity_login.xml
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(LoginViewModel.class);
    }
}
