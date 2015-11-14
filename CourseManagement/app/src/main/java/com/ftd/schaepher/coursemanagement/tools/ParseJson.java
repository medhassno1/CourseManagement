package com.ftd.schaepher.coursemanagement.tools;

import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Schaepher on 2015/11/9.
 */
public class ParseJson {
    Gson gson;

    public ParseJson() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    public List<TableUserTeacher> getTeacherList(String jsonArray) {
        return gson.fromJson(jsonArray, new TypeToken<List<TableUserTeacher>>() {
        }.getType());
    }

    public String getTeacherJson(List<?> list) {
        return gson.toJson(list);
    }
}
