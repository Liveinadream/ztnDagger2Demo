package com.ztn.app.ui.login;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ztn.app.data.DemoRepository;
import com.ztn.common.ToastHelper;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by 冒险者ztn on 2019-10-07.
 * 登陆界面
 */
public class LoginViewModel extends BaseViewModel<DemoRepository> {

    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    public boolean show = false;
    public String userNameHint = "用户名";

    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    public String passwordHint = "密码";
    public String title = "登陆界面";

    public LoginViewModel(@NonNull Application application, DemoRepository demoRepository) {
        super(application, demoRepository);
        userName.set(model.getUserName());
        password.set(model.getPassword());
        TextView textView = new TextView(getApplication());
        textView.setOnClickListener(v -> {
        });
    }

    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(this::login);

    /**
     * 网络模拟一个登陆操作
     **/
    private void login() {
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        //RaJava模拟登录
        addSubscribe(model.login()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(disposable -> showDialog())
                .subscribe((Consumer<Object>) o -> {
                    dismissDialog();
                    //保存账号密码
                    model.saveUserName(userName.get());
                    model.savePassword(password.get());
                    //进入DemoActivity页面
                    ToastHelper.INSTANCE.showToast("登陆成功");
//                        startActivity(DemoActivity.class);
                    //关闭页面
                    finish();
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
