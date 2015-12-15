package com.ftd.schaepher.coursemanagement.tools;

import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schaepher on 2015/12/9.
 */
public class GlobalMap {
    private static final Map<String, String> globalMap = new HashMap<>();

    static {
        globalMap.put("user_teacher", TableUserTeacher.class.getName());
        globalMap.put("user_department_head", TableUserDepartmentHead.class.getName());
        globalMap.put("user_teaching_office", TableUserTeachingOffice.class.getName());
        globalMap.put("pojo_package_name", TableUserTeacher.class.getPackage().getName());

    }

    public static String get(String key) {
        return globalMap.get(key);
    }
}
