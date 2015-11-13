package com.ftd.schaepher.coursemanagement.pojo;

/**
 * Created by sxq on 2015/11/2.
 */
public class Teacher {
    private int teacherImageId;
    private String teacherName;

    public Teacher() {
    }

    public Teacher(int teacherImageId, String teacherName) {
        this.teacherImageId = teacherImageId;
        this.teacherName = teacherName;
    }

    public int getTeacherImageId() {
        return teacherImageId;
    }

    public void setTeacherImageId(int teacherImageId) {
        this.teacherImageId = teacherImageId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
