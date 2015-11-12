package com.ftd.schaepher.coursemanagement.tools;

import android.util.Log;

import com.ftd.schaepher.coursemanagement.pojo.TableTeacher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Schaepher on 2015/11/9.
 */
public class ParseJson {

    Gson gson;

    public ParseJson() {
        gson = new Gson();
    }

    public void toTeacher(String jsonArray) {
        List<TableTeacher> teacherList = gson.fromJson(jsonArray, new TypeToken<List<TableTeacher>>() {
        }.getType());
        for (TableTeacher teacher:teacherList)
        {
            Log.w("teacher",teacher.toString());
        }
    }

}
