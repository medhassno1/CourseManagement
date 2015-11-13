package com.ftd.schaepher.coursemanagement.pojo;

import android.util.Log;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by lenovo on 2015/10/31.
 * 教学办用户表
 */
@Table(name="TableTeachingDepartment")
public class TableTeachingDepartment {
    @Id
    private String number;
    private String pwd;
    private String name;
    private String telephone;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
