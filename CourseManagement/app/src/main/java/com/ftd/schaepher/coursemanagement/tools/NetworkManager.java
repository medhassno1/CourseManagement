package com.ftd.schaepher.coursemanagement.tools;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Schaepher on 2015/10/27.
 */
public class NetworkManager {

    public static final String URL_LOGIN =
            "http://schaepher.imwork.net:22817/Teacher_class_syetemDemo1.1/php/login.php";
//    public static final String URL_LOGIN =
//            "http://jeek-zsy.imwork.net:12051/TeacherClass/Teacher_class_syetemDemo1.1/php/login.php";

    // 测试获取 json数据地址
    public static final String URL_JSON_GET =
            "http://schaepher.imwork.net:22817/Teacher_class_syetemDemo1.1/php/json-test.php";
    // 测试发送json数据地址
    public static final String URL_JSON_POST =
            "http://schaepher.imwork.net:22817/Teacher_class_syetemDemo1.1/php/json-update.php";

    // 实例化对象
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void post(String urlString, RequestParams params, BaseJsonHttpResponseHandler res) {
        client.post(urlString, params, res);
    }

    public static void post(String urlString, RequestParams params,
                            AsyncHttpResponseHandler res) {
        client.post(urlString, params, res);
    }

    public static void get(String urlString, RequestParams params,
                           AsyncHttpResponseHandler res) {
        client.get(urlString, params, res);
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

}
