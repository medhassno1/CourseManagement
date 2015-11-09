package com.ftd.schaepher.coursemanagement.pojo;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by lenovo on 2015/11/6.
 */
@Table(name="TableTeacher") //表名
public class TableTeacher {
    @Id
    private String workNumber;
    private String pwd;
    private String name;
    private String telephone;

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "TableTeacher{" +
                "workNumber='" + workNumber + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
