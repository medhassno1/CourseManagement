package com.ftd.schaepher.coursemanagement.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Schaepher on 2016/09/16.
 * 系负责人及其所负责的专业
 */
@DatabaseTable(tableName = "TableManageMajor")
public class TableManageMajor {
    @DatabaseField(id = true)
    private transient int id;
    @DatabaseField
    private String workNumber;
    @DatabaseField
    private String major;

    public TableManageMajor() {

    }

    public TableManageMajor(String major, String workNumber) {
        this.major = major;
        this.workNumber = workNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    @Override
    public String toString() {
        return "TableManageMajor{" +
                "major='" + major + '\'' +
                ", workNumber='" + workNumber + '\'' +
                '}';
    }
}
