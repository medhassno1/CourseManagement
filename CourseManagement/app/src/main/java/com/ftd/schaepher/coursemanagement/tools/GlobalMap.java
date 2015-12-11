package com.ftd.schaepher.coursemanagement.tools;

import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schaepher on 2015/12/9.
 */
public class GlobalMap {
    private static final Map<String,String> globalMap = new HashMap<>();
    static {
        globalMap.put("user_teacher","TableUserTeacher");
        globalMap.put("user_department_head","TableUserDepartmentHead");
        globalMap.put("user_teaching_office","TableUserTeachingOffice");
        globalMap.put("pojo_package_name",TableUserTeacher.class.getPackage().getName()+".");

    }

    public static String get(String key){
        return globalMap.get(key);
    }
}
