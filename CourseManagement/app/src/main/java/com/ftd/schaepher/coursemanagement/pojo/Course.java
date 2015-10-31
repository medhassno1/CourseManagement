package com.ftd.schaepher.coursemanagement.pojo;

/**
 * Created by Administrator on 2015/9/28.
 *   课程信息类
 */
public class Course {

    private String grade;//年级
    private String major;//专业
    private String sum;//人数
    private String courseName;//课程名
    private String type;//选修类型
    private String credit;//学分
    private String classHour;//学时
    private String experimentHour;//实验学时
    private String computerHour;//上机学时
    private String fromToEnd;//起讫周序
    private String teacher;//教师
    private String note;//备注

    public Course(){super();}

    public Course(String grade, String major, String sum,
                        String courseName, String type, String credit, String classHour,
                        String experimentHour, String computerHour, String fromToEnd,
                        String teacher, String note) {
        super();
        this.grade = grade;
        this.major = major;
        this.sum = sum;
        this.courseName = courseName;
        this.type = type;
        this.credit = credit;
        this.classHour = classHour;
        this.experimentHour = experimentHour;
        this.computerHour = computerHour;
        this.fromToEnd = fromToEnd;
        this.teacher = teacher;
        this.note = note;
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

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getClassHour() {
        return classHour;
    }

    public void setClassHour(String classHour) {
        this.classHour = classHour;
    }
    public String getExperimentHour() {
        return experimentHour;
    }

    public void setExperimentHour(String experimentHour) {
        this.experimentHour = experimentHour;
    }

    public String getComputerHour() {
        return computerHour;
    }

    public void setComputerHour(String computerHour) {
        this.computerHour = computerHour;
    }

    public String getFromToEnd() {
        return fromToEnd;
    }

    public void setFromToEnd(String fromToEnd) {
        this.fromToEnd = fromToEnd;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
