package com.ftd.schaepher.coursemanagement.pojo;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Schaepher on 2016/09/16.
 * 教师用户表
 */
@DatabaseTable(tableName = "TableUserTeacher")
public class TableUserTeacher extends Person{

    public TableUserTeacher() {

    }

    public TableUserTeacher(String workNumber, String password, String name, String sex,
                            String birthday, String telephone, String email, String department) {
        super(workNumber, password, name, sex, birthday, telephone, email, department);
    }

}
