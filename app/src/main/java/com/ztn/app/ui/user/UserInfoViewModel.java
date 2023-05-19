package com.ztn.app.ui.user;

import android.app.Application;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import com.ztn.app.data.DemoRepository;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * 用户信息界面
 */
public class UserInfoViewModel extends BaseViewModel<DemoRepository> {
    public ObservableField<String> name = new ObservableField<>("");
    public String title = "用户信息";

    public UserInfoViewModel(@NonNull Application application, DemoRepository model) {
        super(application, model);
        name.set(model.getUserName());
    }

    //登录按钮的点击事件
    public BindingCommand saveCommand = new BindingCommand(this::saveData);

    public void saveData() {
        model.saveUserName(name.get());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
