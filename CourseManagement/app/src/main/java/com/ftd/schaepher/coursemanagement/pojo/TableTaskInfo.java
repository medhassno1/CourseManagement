package com.ftd.schaepher.coursemanagement.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Schaepher on 2016/09/16.
 * 任务详情表
 */
@DatabaseTable(tableName = "TableTaskInfo")
public class TableTaskInfo {
    @DatabaseField(id = true)
    private String relativeTable;
    @DatabaseField
    private String year;
    @DatabaseField
    private String semester;
    @DatabaseField
    private String departmentDeadline;
    @DatabaseField
    private String teacherDeadline;
    @DatabaseField
    private String taskState;

    public TableTaskInfo(TableTaskInfo task) {
        this.relativeTable = task.relativeTable;
        this.year = task.year;
        this.semester = task.semester;
        this.departmentDeadline = task.departmentDeadline;
        this.teacherDeadline = task.teacherDeadline;
        this.taskState = task.taskState;
    }

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
                ", taskState='" + taskState + '\'' +
                '}';
    }
}
