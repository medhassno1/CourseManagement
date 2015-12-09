package com.ftd.schaepher.coursemanagement.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schaepher on 2015/12/9.
 */
public class GlobalMap {
    private static final Map<String,String> globalMap = new HashMap<>();
    static {
        globalMap.put("idTeacher","user_teacher");
        globalMap.put("idDepartmentHead","user_department_head");
        globalMap.put("idTeachingOffice","user_teaching_office");

    }

    public static String get(String key){
        return globalMap.get(key);
    }
}
