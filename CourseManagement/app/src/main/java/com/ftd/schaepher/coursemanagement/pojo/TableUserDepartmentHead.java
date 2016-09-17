package com.ftd.schaepher.coursemanagement.pojo;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Schaepher on 2016/09/16.
 * 系负责人用户表
 */
@DatabaseTable(tableName = "TableUserDepartmentHead")
public class TableUserDepartmentHead extends Person{

    public TableUserDepartmentHead() {
    }

    public TableUserDepartmentHead(String workNumber, String password, String name, String sex,
                                   String birthday, String telephone, String email, String department) {
        super(workNumber, password, name, sex, birthday, telephone, email, department);
    }


}
