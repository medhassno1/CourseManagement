package com.ftd.schaepher.coursemanagement.tools;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

/**
 * Created by Schaepher on 2015/10/27.
 */
public class NetworkManager {
    // 服务器基础地址，指向存放api的文件夹
    private static final String URL_BASE = "http://139.129.39.29/Teacher_class_systemDemo(alpha)/Teacher_class_syetemDemo/mobile_api/";
    // 创建创建表格并且插入数据（发布新任务）
    public static final String CREATE_TABLE = URL_BASE + "create_table.php";
    // 删除任务
    public static final String DELETE_TASK = URL_BASE + "delete_task.php";
    // 删除用户
    public static final String DELETE_USER = URL_BASE + "delete_user.php";
    // 插入任意一张表。（可用来创建用户等添加的操作）
    public static final String INSERT_TABLE = URL_BASE + "insert_table.php";
    // 提交报课
    public static final String SUBMIT_SELECTED_COURSE = URL_BASE + "insert_tc_update_bc.php";
    // 登陆
    public static final String LOGIN = URL_BASE + "login.php";
    // 获取任意表的数据
    public static final String GET_ANY_TABLE = URL_BASE + "query_table_name.php";
    // 获取教师某张表的选课情况
    public static final String GET_TABLE_SELECT = URL_BASE + "query_teacher_select_courses.php";
    // 插入或者更新（如果已存在）合一表
    public static final String INSERT_OR_UPDATE_CB_TABLE = URL_BASE + "update_insert_cb_table.php";
    // 更新用户信息
    public static final String UPDATE_USER_DATA = URL_BASE + "update_user.php";

    private static final OkHttpClient client = new OkHttpClient();

    static {
        client.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
    }

    // 异步的post
    public static void postToServerAsync(String tableName, String jsonData, String actionURL, ResponseCallback callback) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("tableName", tableName)
                .add("jsonData", jsonData)
                .build();
        Request request = new Request.Builder()
                .url(actionURL)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    // 异步的post
    public static void deleteServerUser(String tableName, String workNumber, ResponseCallback callback) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("tableName", tableName)
                .add("workNumber", workNumber)
                .build();
        Request request = new Request.Builder()
                .url(DELETE_USER)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    // 同步的post
    public static String postToServerSync(String tableName, String jsonData, String actionURL) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("tableName", tableName)
                .add("jsonData", jsonData)
                .build();
        Loger.w("JsonData", jsonData);
        Request request = new Request.Builder()
                .url(actionURL)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    // 同步的post
    public static String updateUserData(String tableName, String jsonData,
                                        String manageMajor, String actionURL) throws IOException {
        if (manageMajor == null) {
            manageMajor = " ";
        }
        RequestBody formBody = new FormEncodingBuilder()
                .add("tableName", tableName)
                .add("jsonData", jsonData)
                .add("manageMajor", manageMajor)
                .build();
        Request request = new Request.Builder()
                .url(actionURL)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    // 获取任意表的数据
    public static void getJsonString(String tableName,
                                     ResponseCallback callback) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("tableName", tableName)
                .build();
        Request request = new Request.Builder()
                .url(GET_ANY_TABLE)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    // 获取教师选课信息
    public static void getTeacherSelect(String tableName, String workNumber,
                                        ResponseCallback callback) throws IOException {
        Loger.w("tableName",tableName);
        Loger.w("workNumber",workNumber);
        RequestBody formBody = new FormEncodingBuilder()
                .add("tableName", tableName)
                .add("workNumber", workNumber)
                .build();
        Request request = new Request.Builder()
                .url(GET_TABLE_SELECT)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    // 登陆
    public static void login(String userName, String password, String identity,
                             ResponseCallback callback) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("login-user", userName)
                .add("login-password", password)
                .add("ident", identity)
                .build();

        Request request = new Request.Builder()
                .url(LOGIN)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    // 回调接口
    public interface ResponseCallback extends Callback {
        @Override
        void onResponse(Response response) throws IOException;

        @Override
        void onFailure(Request request, IOException e);
    }

}
