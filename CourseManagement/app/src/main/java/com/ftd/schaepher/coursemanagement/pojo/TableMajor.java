package com.ftd.schaepher.coursemanagement.pojo;

import net.tsz.afinal.annotation.sqlite.Id;

/**
 * Created by lenovo on 2015/11/2.
 */
public class TableMajor {
    @Id
    private String major;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }


}


