package com.j256.simplemagic;

/**
 * Created by 冒险者ztn on 2019-12-10.
 */
public class ContentInfoUtilSingle {

    private static ContentInfoUtil instance = null;

    public static ContentInfoUtil getInstance() {
        if (instance == null) {
            synchronized (ContentInfoUtilSingle.class) {
                instance = new ContentInfoUtil();
            }
        }
        return instance;
    }

}
