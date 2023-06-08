package com.ztn.library.rx.permission.bridge;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class RequestManager {

    public static RequestManager get() {
        return RequestManagerHandler.requestManager;
    }

    static class RequestManagerHandler {
        static RequestManager requestManager = new RequestManager();
    }

    private final Executor mExecutor;

    private RequestManager() {
        this.mExecutor = Executors.newCachedThreadPool();
    }

    public void add(BridgeRequest request) {
        mExecutor.execute(new RequestExecutor(request));
    }
}