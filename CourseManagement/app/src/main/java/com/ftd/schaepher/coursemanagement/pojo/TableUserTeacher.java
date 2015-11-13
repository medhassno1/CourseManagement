package com.ftd.schaepher.coursemanagement.pojo;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by lenovo on 2015/11/6.
 * 教师用户表
 */
@Table(name = "TableUserTeacher") //表名
public class TableUserTeacher {
    @Id
    private String workNumber;
    private String password;
    private String name;
    private String sex;
    private String birthday;
    private String telephone;
    private String email;
    private String department;

    public TableUserTeacher() {
    }

    public TableUserTeacher(String workNumber, String password, String name, String sex,
                            String birthday, String telephone, String email, String department) {
        this.workNumber = workNumber;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.telephone = telephone;
        this.email = email;
        this.department = department;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "TableUserTeacher{" +
                "workNumber='" + workNumber + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
