package com.ftd.schaepher.coursemanagement;

/**
 * Created by sxq on 2015/10/31.
 */
public class Task {
    private String taskState;
    private String taskName;

    public Task(String taskState, String taskName) {
        this.taskState = taskState;
        this.taskName = taskName;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
