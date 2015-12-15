package com.ftd.schaepher.coursemanagement.pojo;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by sxq on 2015/10/31.
 * 任务详情表
 */
@Table(name = "TableTaskInfo")
public class TableTaskInfo {
    @Id
    private String relativeTable;
    private String year;
    private String semester;
    private String departmentDeadline;
    private String teacherDeadline;
    //    private String remark;
    private String taskState;

    public TableTaskInfo() {
    }

    public TableTaskInfo(String taskState, String relativeTable) {
        this.taskState = taskState;
        this.relativeTable = relativeTable;
    }

    public String getRelativeTable() {
        return relativeTable;
    }

    public void setRelativeTable(String relativeTable) {
        this.relativeTable = relativeTable;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDepartmentDeadline() {
        return departmentDeadline;
    }

    public void setDepartmentDeadline(String departmentDeadline) {
        this.departmentDeadline = departmentDeadline;
    }

    public String getTeacherDeadline() {
        return teacherDeadline;
    }

    public void setTeacherDeadline(String teacherDeadline) {
        this.teacherDeadline = teacherDeadline;
    }

//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    @Override
    public String toString() {
        return "TableTaskInfo{" +
                ", relativeTable='" + relativeTable + '\'' +
                ", year='" + year + '\'' +
                ", semester='" + semester + '\'' +
                ", departmentDeadline='" + departmentDeadline + '\'' +
                ", teacherDeadline='" + teacherDeadline + '\'' +
//                ", remark='" + remark + '\'' +
                ", taskState='" + taskState + '\'' +
                '}';
    }
}
