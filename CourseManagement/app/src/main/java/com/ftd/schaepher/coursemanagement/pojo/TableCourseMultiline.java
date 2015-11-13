package com.ftd.schaepher.coursemanagement.pojo;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by lenovo on 2015/11/2.
 * 开课表，多行
 */
@Table(name = "TableCourseMultiline")
public class TableCourseMultiline {
    @Id
    private String insertTime;
    private String workNumber;
    private String grade;
    private String major;
    private String people;
    private String courseName;
    private String courseType;
    private String courseCredit;
    private String courseHours;
    private String practiceHour;
    private String onMachineHour;
    private String timePeriod;
    private String teacherName;
    private String remark;

    public TableCourseMultiline() {
    }

    public TableCourseMultiline(  String grade, String major, String people, String courseName, String courseType, String courseCredit, String courseHours, String practiceHour, String onMachineHour, String timePeriod, String teacherName, String remark) {
        this.grade = grade;
        this.major = major;
        this.people = people;
        this.courseName = courseName;
        this.courseType = courseType;
        this.courseCredit = courseCredit;
        this.courseHours = courseHours;
        this.practiceHour = practiceHour;
        this.onMachineHour = onMachineHour;
        this.timePeriod = timePeriod;
        this.teacherName = teacherName;
        this.remark = remark;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
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

    public String getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(String courseHours) {
        this.courseHours = courseHours;
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
        return "TableCourseMultiline{" +
                "insertTime='" + insertTime + '\'' +
                ", workNumber='" + workNumber + '\'' +
                ", grade='" + grade + '\'' +
                ", major='" + major + '\'' +
                ", people='" + people + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseType='" + courseType + '\'' +
                ", courseCredit='" + courseCredit + '\'' +
                ", courseHours='" + courseHours + '\'' +
                ", practiceHour='" + practiceHour + '\'' +
                ", onMachineHour='" + onMachineHour + '\'' +
                ", timePeriod='" + timePeriod + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
