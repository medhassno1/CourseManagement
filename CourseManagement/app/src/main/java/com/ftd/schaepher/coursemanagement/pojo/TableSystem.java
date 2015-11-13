package com.ftd.schaepher.coursemanagement.pojo;
import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by lenovo on 2015/11/2.
 * 系表
 */
@Table(name = "TableSystem")
public class TableSystem {
    @Id
    private String sysytemName;

    public String getSysytemName() {
        return sysytemName;
    }

    public void setSysytemName(String sysytemName) {
        this.sysytemName = sysytemName;
    }
}
