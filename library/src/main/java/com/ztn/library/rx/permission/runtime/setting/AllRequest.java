package com.ztn.library.rx.permission.runtime.setting;

import com.ztn.library.rx.permission.source.Source;

/**
 * <p>SettingRequest executor.</p>
 * Created by zhaoyuntao on 2016/12/28.
 */
public class AllRequest implements SettingRequest {

    private Source mSource;

    public AllRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public void start(int requestCode) {
        SettingPage setting = new SettingPage(mSource);
        setting.start(requestCode);
    }
}