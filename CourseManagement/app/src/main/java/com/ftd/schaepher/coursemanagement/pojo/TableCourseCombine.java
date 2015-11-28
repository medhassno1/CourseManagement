package com.ftd.schaepher.coursemanagement.pojo;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by Schaepher on 2015/11/13.
 * 开课表，合成一行
 */
@Table(name = "TableCourseCombine")
public class TableCourseCombine {
    @Id
    private String insertTime;
    private String grade;
    private String major;
    private String people;
    private String courseName;
    private String courseType;
    private String courseCredit;
    private String courseHour;
    private String practiceHour;
    private String onMachineHour;
    private String timePeriod;
    private String teacherName;
    private String remark;

    public TableCourseCombine() {
    }

    public TableCourseCombine(String insertTime, String courseCredit,
                              String courseHour, String courseName, String courseType,
                              String grade, String major, String onMachineHour, String people,
                              String practiceHour, String remark, String teacherName,
                              String timePeriod) {
        this.insertTime = insertTime;
        this.courseCredit = courseCredit;
        this.courseHour = courseHour;
        this.courseName = courseName;
        this.courseType = courseType;
        this.grade = grade;
        this.major = major;
        this.onMachineHour = onMachineHour;
        this.people = people;
        this.practiceHour = practiceHour;
        this.remark = remark;
        this.teacherName = teacherName;
        this.timePeriod = timePeriod;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(String courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getCourseHour() {
        return courseHour;
    }

    public void setCourseHour(String courseHour) {
        this.courseHour = courseHour;
    }

    public String getPracticeHour() {
        return practiceHour;
    }

    public void setPracticeHour(String practiceHour) {
        this.practiceHour = practiceHour;
    }

    public String getOnMachineHour() {
        return onMachineHour;
    }

    public void setOnMachineHour(String onMachineHour) {
        this.onMachineHour = onMachineHour;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TableCourseCombine{" +
                "courseCredit='" + courseCredit + '\'' +
                ", insertTime='" + insertTime + '\'' +
                ", grade='" + grade + '\'' +
                ", major='" + major + '\'' +
                ", people='" + people + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseType='" + courseType + '\'' +
                ", courseHour='" + courseHour + '\'' +
                ", practiceHour='" + practiceHour + '\'' +
                ", onMachineHour='" + onMachineHour + '\'' +
                ", timePeriod='" + timePeriod + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
