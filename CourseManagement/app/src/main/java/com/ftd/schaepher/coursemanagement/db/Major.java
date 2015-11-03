package com.ftd.schaepher.coursemanagement.db;

import net.tsz.afinal.annotation.sqlite.Id;

/**
 * Created by lenovo on 2015/11/2.
 */
public class Major {
    @Id
    private String majornumber;
    private String majorname;

    public String getMajorname() {
        return majorname;
    }

    public void setMajorname(String majorname) {
        this.majorname = majorname;
    }

    public String getMajornumber() {
        return majornumber;
    }

    public void setMajornumber(String majornumber) {
        this.majornumber = majornumber;
    }
}


