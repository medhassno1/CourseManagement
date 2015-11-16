package com.ftd.schaepher.coursemanagement.tools;

import android.content.Context;
import android.util.Log;

import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Schaepher on 2015/11/12.
 */
public class ServerTools {
    private Context context;
    private CourseDBHelper dbHelper;
    private ParseJson parseJson;

    public static final String UPDATE_CB_TABLE = "updateCbTable";
    public static final String UPDATE_TASK_TABLE="updateTaskTable";
    public static final String INSERT_TABLE= "insertTable";

    public ServerTools(Context context) {
        this.context = context;
        dbHelper = new CourseDBHelper();
        dbHelper.createDataBase(context);
        parseJson = new ParseJson();
    }



    public void postTableToServer(Class<?> tableClass,String tableName,String action) {

        List list = dbHelper.findAll(tableClass);

        String jsonData = parseJson.getJsonString(list);
        jsonData = jsonData.replace("null", "\"\"");

        RequestParams params = new RequestParams();
        params.add("jsonData", jsonData);
        params.add("tableName",tableName);
        params.add("action",action);

        NetworkManager.post(NetworkManager.URL_POST_JSON, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Charset charset = Charset.defaultCharset();
                String string = new String(bytes,charset);
                Log.w("post",string);
                System.out.println(string);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            }
        });

    }

    public void getTable(Class<?> tableClass,String tableName) {
        RequestParams params = new RequestParams();
        params.add("tableName", tableName);

        NetworkManager.post(NetworkManager.URL_JSON_GET, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, String s, Object o) {
                ParseJson parseJson = new ParseJson();
                List list = parseJson.getListFromJsonString(s);
                dbHelper.deleteAll(TableUserTeacher.class);
/*                for (TableUserTeacher teacher : list) {
                    dbHelper.insert(teacher);
                }*/
            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, String s, Object o) {

            }

            @Override
            protected Object parseResponse(String s, boolean b) throws Throwable {
                return null;
            }


        });
    }
}
