package com.ftd.schaepher.coursemanagement.db;
import net.tsz.afinal.annotation.sqlite.Id;
/**
 * Created by lenovo on 2015/11/2.
 */
public class ClassTable {
    @Id
    private String classname;
    private String grade;
    private String numberofpeaple;
    private String majorname;
    private String type;
    private String credit;
    private String time;
    private String expertime;
    private String practicetime;

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getPracticetime() {
        return practicetime;
    }

    public void setPracticetime(String practicetime) {
        this.practicetime = practicetime;
    }

    public String getExpertime() {
        return expertime;
    }

    public void setExpertime(String expertime) {
        this.expertime = expertime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMajorname() {
        return majorname;
    }

    public void setMajorname(String majorname) {
        this.majorname = majorname;
    }

    public String getNumberofpeaple() {
        return numberofpeaple;
    }

    public void setNumberofpeaple(String numberofpeaple) {
        this.numberofpeaple = numberofpeaple;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
