package com.ftd.schaepher.coursemanagement.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Schaepher on 2016/09/16.
 * 开课表，多行
 */
@DatabaseTable(tableName = "TableCourseMultiline")
public class TableCourseMultiline {
    @DatabaseField(id = true)
    private String courseName;
    @DatabaseField
    private String insertTime;
    @DatabaseField
    private String workNumber;
    @DatabaseField
    private String grade;
    @DatabaseField
    private String major;
    @DatabaseField
    private String people;
    @DatabaseField
    private String courseType;
    @DatabaseField
    private String courseCredit;
    @DatabaseField
    private String courseHour;
    @DatabaseField
    private String practiceHour;
    @DatabaseField
    private String onMachineHour;
    @DatabaseField
    private String timePeriod;
    @DatabaseField
    private String teacherName;
    @DatabaseField
    private String remark;

    public TableCourseMultiline() {
    }

    public TableCourseMultiline(String insertTime, String grade, String major, String people,
                                String courseName, String courseType, String courseCredit,
                                String courseHour, String practiceHour, String onMachineHour,
                                String timePeriod, String teacherName, String remark) {
        this.insertTime = insertTime;
        this.grade = grade;
        this.major = major;
        this.people = people;
        this.courseName = courseName;
        this.courseType = courseType;
        this.courseCredit = courseCredit;
        this.courseHour = courseHour;
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
        return "TableCourseMultiline{" +
                "courseCredit='" + courseCredit + '\'' +
                ", insertTime='" + insertTime + '\'' +
                ", workNumber='" + workNumber + '\'' +
                ", grade='" + grade + '\'' +
                ", major='" + major + '\'' +
                ", people='" + people + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseType='" + courseType + '\'' +
                ", courseCredit='" + courseCredit + '\'' +
                ", courseHour='" + courseHour + '\'' +
                ", practiceHour='" + practiceHour + '\'' +
                ", onMachineHour='" + onMachineHour + '\'' +
                ", timePeriod='" + timePeriod + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
