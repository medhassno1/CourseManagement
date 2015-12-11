package com.ftd.schaepher.coursemanagement.tools;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Schaepher on 2015/10/27.
 */
public class NetworkManager {

    /*  public static final String URL_LOGIN =
              "http://schaepher.imwork.net:22817/Teacher_class_syetemDemo1.1/php/login.php";
      public static final String URL_GET_JSON =
              "http://schaepher.imwork.net:22817/Teacher_class_syetemDemo1.1/php/query-by-table-name.php";
      public static final String URL_POST_JSON =
              "http://schaepher.imwork.net:22817/Teacher_class_syetemDemo1.1/php/post-table.php";*/
    public static final String URL_LOGIN =
            "http://114.215.153.57/tcs/mobile-api/login.php";
    public static final String URL_GET_JSON =
            "http://114.215.153.57/tcs/mobile-api/query-by-table-name.php";
    public static final String URL_POST_JSON =
            "http://114.215.153.57/tcs/mobile-api/post-table.php";
    private static final String URL_GET_JSON_TC =
            "http://114.215.153.57/tcs/mobile-api/post_select_teacher.php";

    public static final String UPDATE_CB_TABLE = "updateCbTable";
    public static final String UPDATE_TASK_TABLE = "updateTaskTable";
    public static final String INSERT_TABLE = "insert";

    private static final OkHttpClient client = new OkHttpClient();

    static {
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
    }

    public static String postJsonString(String tableName, String jsonData, String action) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("tableName", tableName)
                .add("jsonData", jsonData)
                .add("action", action)
                .build();
        Request request = new Request.Builder()
                .url(URL_POST_JSON)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static void getJsonString(String tableName,
                                     ResponseCallback callback) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("tableName", tableName)
                .build();
        Request request = new Request.Builder()
                .url(URL_GET_JSON)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getJsonString(String tableName, String workNumber,
                                     ResponseCallback callback) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("tableName", tableName)
                .add("workNumber", workNumber)
                .build();
        Request request = new Request.Builder()
                .url(URL_GET_JSON_TC)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void login(String userName, String password, String identity,
                             ResponseCallback callback) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("login-user", userName)
                .add("login-password", password)
                .add("ident", identity)
                .build();

        Request request = new Request.Builder()
                .url(URL_LOGIN)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public interface ResponseCallback extends Callback {
        @Override
        void onResponse(Response response) throws IOException;

        @Override
        void onFailure(Request request, IOException e);
    }

}
