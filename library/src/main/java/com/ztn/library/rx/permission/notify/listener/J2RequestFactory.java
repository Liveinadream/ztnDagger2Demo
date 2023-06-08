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
package com.ztn.library.rx.permission.notify.listener;

import com.ztn.library.rx.permission.notify.Notify;
import com.ztn.library.rx.permission.source.Source;

/**
 * Created by zhaoyuntao on 2018/5/29.
 */
public class J2RequestFactory implements Notify.ListenerRequestFactory {

    @Override
    public ListenerRequest create(Source source) {
        return new J2Request(source);
    }
}