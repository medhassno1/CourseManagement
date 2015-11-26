package com.ftd.schaepher.coursemanagement.pojo;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by Schaepher on 2015/11/20.
 * 系负责人及其所负责的专业
 */
@Table(name = "TableManageMajor")
public class TableManageMajor {
    @Id
    private String workNumber;
    private String major;

    public TableManageMajor(){

    }

    public TableManageMajor(String major, String workNumber) {
        this.major = major;
        this.workNumber = workNumber;
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
