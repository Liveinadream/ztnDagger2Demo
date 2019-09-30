package com.ztn.app.user;

import android.databinding.ObservableField;

import com.ztn.app.ui.UserInfoActivity;

import java.util.List;

/**
 * Created by 冒险者ztn on 2019-09-30.
 * 用户信息
 */
public class UserInfoViewMode {

    public ObservableField<String> headImageUrl = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> age = new ObservableField<>();
    public ObservableField<String> job = new ObservableField<>();
    public ObservableField<List<Object>> hobbies = new ObservableField<>();

    public UserInfoViewMode(UserInfoActivity activity) {
    }
}
