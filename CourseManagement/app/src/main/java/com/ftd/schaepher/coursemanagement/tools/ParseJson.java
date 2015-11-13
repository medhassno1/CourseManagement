package com.ftd.schaepher.coursemanagement.tools;

import android.util.Log;

import com.ftd.schaepher.coursemanagement.pojo.TableSystemLeader;
import com.ftd.schaepher.coursemanagement.pojo.TableTeacher;
import com.ftd.schaepher.coursemanagement.pojo.Teacher;
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

    public void getTeacherList(String jsonArray) {
        List<TableTeacher> teacherList = gson.fromJson(jsonArray, new TypeToken<List<TableTeacher>>() {
        }.getType());
        for (TableTeacher teacher:teacherList)
        {
            Log.w("teacher",teacher.toString());
        }
    }

    public String getTeacherJson(List<TableTeacher> teacherList){
        // 如果一行中某一列为空，则在这里会被舍弃。需要解决这个问题
        return gson.toJson(teacherList);
    }

}
