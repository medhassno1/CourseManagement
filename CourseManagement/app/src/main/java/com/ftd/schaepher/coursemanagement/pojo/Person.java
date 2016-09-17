package com.ftd.schaepher.coursemanagement.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;

/**
 * Created by Schaepher on 2016/9/16.
 */
public class Person extends BaseDaoEnabled {

    @DatabaseField(id = true)
    protected String workNumber;
    @DatabaseField
    protected String password;
    @DatabaseField
    protected String name;
    @DatabaseField
    protected String sex;
    @DatabaseField
    protected String birthday;
    @DatabaseField
    protected String telephone;
    @DatabaseField
    protected String email;
    @DatabaseField
    protected String department;

    public Person(){}

    public Person(String workNumber, String password, String name, String sex, String birthday, String telephone, String email, String department) {
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
        return "Person{" +
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
