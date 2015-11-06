package com.ftd.schaepher.coursemanagement.pojo;
import net.tsz.afinal.annotation.sqlite.Id;
/**
 * Created by lenovo on 2015/11/2.
 */
public class TableClass {
    @Id
    private String className;
    private String grade;
    private String num;
    private String major;
    private String classType;
    private String classCredit;
    private String classTime;
    private String opTime;
    private String prTime;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPrTime() {
        return prTime;
    }

    public void setPrTime(String prTime) {
        this.prTime = prTime;
    }

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }

    public String getTime() {
        return classTime;
    }

    public void setTime(String time) {
        this.classTime = time;
    }

    public String getClassCredit() {
        return classCredit;
    }

    public void setClassCredit(String classCredit) {
        this.classCredit = classCredit;
    }

    public String getType() {
        return classType;
    }

    public void setType(String type) {
        this.classType = type;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
