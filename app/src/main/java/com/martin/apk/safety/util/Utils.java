package com.martin.apk.safety.util;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * <p>
 * Package Name:com.martin.apk.safety.util
 * </p>
 * <p>
 * Class Name:Utils
 * <p>
 * Description:common utils
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/11/27 4:15 PM Release
 * @Reviser:
 * @Modification Time:2018/11/27 4:15 PM
 */
public class Utils {

    private static Application sApplication;
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("u should init first");
    }
    /**
     * 初始化工具类
     *
     * @param app 应用
     */
    public static void init(@NonNull final Application app) {
        Utils.sApplication = app;
    }
}
