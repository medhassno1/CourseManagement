package com.ftd.schaepher.coursemanagement.pojo;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Schaepher on 2016/09/16.
 * 教学办用户表
 */
@DatabaseTable(tableName  = "TableUserTeachingOffice")
public class TableUserTeachingOffice extends Person{

    public TableUserTeachingOffice() {
    }

    public TableUserTeachingOffice(String workNumber, String password, String name,
                                   String telephone, String email) {
        this.workNumber = workNumber;
        this.password = password;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "TableUserTeachingOffice{" +
                "workNumber='" + workNumber + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
//                ", sex='" + sex + '\'' +
//                ", birthday='" + birthday + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
