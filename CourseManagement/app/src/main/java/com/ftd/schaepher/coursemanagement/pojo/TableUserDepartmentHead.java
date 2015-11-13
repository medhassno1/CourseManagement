package com.ftd.schaepher.coursemanagement.pojo;
import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
/**
 * Created by lenovo on 2015/11/2.
 * 系负责人用户表
 */
@Table(name="TableUserDepartmentHead")
public class TableUserDepartmentHead {
    @Id
    private String workNumber;
    private String password;
    private String name;
    private String sex;
    private String birthday;
    private String telephone;
    private String email;
    private String department;
    private String managedMajor;

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

    public String getManagedMajor() {
        return managedMajor;
    }

    public void setManagedMajor(String managedMajor) {
        this.managedMajor = managedMajor;
    }

    @Override
    public String toString() {
        return "TableUserDepartmentHead{" +
                "workNumber='" + workNumber + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", managedMajor='" + managedMajor + '\'' +
                '}';
    }
}
