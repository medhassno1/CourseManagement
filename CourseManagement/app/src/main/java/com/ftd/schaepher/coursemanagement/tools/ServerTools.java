package com.ftd.schaepher.coursemanagement.tools;

import android.content.Context;

import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by Schaepher on 2015/11/12.
 */
public class ServerTools {
    Context context;
    CourseDBHelper dbHelper;
    ParseJson parseJson;

    public ServerTools(Context context) {
        this.context = context;
        dbHelper = new CourseDBHelper();
        dbHelper.createDataBase(context);
        parseJson = new ParseJson();
    }

    public void postTableToServer(Class<?> tableClass,int action) {

        List list = dbHelper.findAll(tableClass);

        String jsonData = parseJson.getTeacherJson(list);
        jsonData = jsonData.replace("null", "\"\"");

        RequestParams params = new RequestParams();
        params.add("jsonData", jsonData);
        params.add("action",String.valueOf(action));

        NetworkManager.post(NetworkManager.URL_JSON_POST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            }
        });

    }

    public void getTeacherTable() {
        String tableName = "user_teacher";
        RequestParams params = new RequestParams();
        params.add("tableName", tableName);

        NetworkManager.post(NetworkManager.URL_JSON_GET, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, String s, Object o) {
                ParseJson parseJson = new ParseJson();
//                Log.w("服务器返回的数据", parseJson.getTeacherList(s).toString());
                List<TableUserTeacher> list = parseJson.getTeacherList(s);
                CourseDBHelper dbHelper = new CourseDBHelper(context);
                dbHelper.deleteAll(TableUserTeacher.class);
                for (TableUserTeacher teacher : list) {
                    dbHelper.insert(teacher);
                }
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
