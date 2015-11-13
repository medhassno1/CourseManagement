package com.ftd.schaepher.coursemanagement.tools;

import android.content.Context;
import android.util.Log;

import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Schaepher on 2015/11/12.
 */
public class ServerTools {

    Context context;

    public ServerTools(Context context) {
        this.context = context;
    }

    public void postTeacherTable() {
        CourseDBHelper dbHelper = new CourseDBHelper();
        dbHelper.creatDataBase(context);
        List list = dbHelper.findall(TableUserTeacher.class);

        ParseJson parseJson = new ParseJson();
        String jsonData = parseJson.getTeacherJson((List<TableUserTeacher>)list);

        Log.w("json数据", jsonData);

        RequestParams params = new RequestParams();
        params.add("jsonData", jsonData);

        NetworkManager.post(NetworkManager.URL_JSON_POST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Charset charset = Charset.forName("UTF-8");
                String html = new String(bytes, charset);
                Log.w("html", html);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }


    public void getTeacherTable() {
        NetworkManager.post(NetworkManager.URL_JSON_GET, null, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, String s, Object o) {
                ParseJson parseJson = new ParseJson();
                parseJson.getTeacherList(s);
            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, String s, Object o) {

            }

            @Override
            protected Object parseResponse(String s, boolean b) throws Throwable {
                JSONArray array = new JSONArray(s);
                return array;
            }
        });
    }
}
