/*
 * Copyright 2018 zhaoyuntao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ztn.library.rx.permission.notify;

import com.ztn.library.rx.permission.RequestExecutor;
import com.ztn.library.rx.permission.bridge.BridgeRequest;
import com.ztn.library.rx.permission.bridge.RequestManager;
import com.ztn.library.rx.permission.source.Source;

/**
 * Created by Zhaoyuntao on 2018/5/29.
 */
class ORequest extends BaseRequest implements RequestExecutor, BridgeRequest.Callback {

    private Source mSource;

    ORequest(Source source) {
        super(source);
        this.mSource = source;
    }

    @Override
    public void start() {
        if (mSource.canNotify()) {
            callbackSucceed();
        } else {
            showRationale(this);
        }
    }

    @Override
    public void execute() {
        BridgeRequest request = new BridgeRequest(mSource);
        request.setType(BridgeRequest.TYPE_NOTIFY);
        request.setCallback(this);
        RequestManager.get().add(request);
    }

    @Override
    public void cancel() {
        callbackFailed();
    }

    @Override
    public void onCallback() {
        if (mSource.canNotify()) {
            callbackSucceed();
        } else {
            callbackFailed();
        }
    }
}