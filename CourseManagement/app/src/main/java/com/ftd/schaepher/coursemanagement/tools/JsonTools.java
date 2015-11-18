package com.ftd.schaepher.coursemanagement.tools;

import com.ftd.schaepher.coursemanagement.pojo.TableCourseCombine;
import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Schaepher on 2015/11/9.
 */
public class JsonTools {
    Gson gson;

    public JsonTools() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    public List getJsonList(String json,String pojoName) {
        Type type;
        switch(pojoName){
            case "TableUserTeacher":
                type = new TypeToken<List<TableUserTeacher>>(){}.getType();
                break;
            case "TableUserDepartmentHead":
                type = new TypeToken<List<TableUserDepartmentHead>>(){}.getType();
                break;
            case "TableUserTeachingOffice":
                type = new TypeToken<List<TableUserTeachingOffice>>(){}.getType();
                break;
            case "TableTaskInfo":
                type = new TypeToken<List<TableTaskInfo>>(){}.getType();
                break;
            case "TableCourseCombine":
                type = new TypeToken<List<TableCourseCombine>>(){}.getType();
                break;
            case "TableCourseMultiline":
                type = new TypeToken<List<TableCourseMultiline>>(){}.getType();
                break;
            default:
                return null;
        }
        return gson.fromJson(json,type);
    }

    public String getJsonString(List<?> list) {
        return gson.toJson(list);
    }

}
