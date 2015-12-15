package com.ftd.schaepher.coursemanagement.tools;

/**
 * Created by Administrator on 2015/11/25.
 * <p/>
 * 这个类用来存储全局变量
 */
public class NetState {
    private static boolean isFirstNetConnect = true;
    private static boolean isFirstLoseNetConnect = true;

    public static boolean isFirstLoseNetConnect() {
        return isFirstLoseNetConnect;
    }

    public static void setIsFirstLoseNetConnect(boolean isFirstLoseNetConnect) {
        NetState.isFirstLoseNetConnect = isFirstLoseNetConnect;
    }

    public static boolean isFirstNetConnect() {
        return isFirstNetConnect;
    }

    public static void setIsFirstNetConnect(boolean isFirstNetConnect) {
        NetState.isFirstNetConnect = isFirstNetConnect;
    }
}
