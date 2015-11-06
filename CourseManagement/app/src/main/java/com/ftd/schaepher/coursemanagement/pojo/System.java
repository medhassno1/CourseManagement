package com.ftd.schaepher.coursemanagement.pojo;
import net.tsz.afinal.annotation.sqlite.Id;
/**
 * Created by lenovo on 2015/11/2.
 */
public class System {
    @Id
    private String systemNumber;
    private String sysytemName;

    public String getSystemNumber() {
        return systemNumber;
    }

    public void setSystemNumber(String systemNumber) {
        this.systemNumber = systemNumber;
    }

    public String getSysytemName() {
        return sysytemName;
    }

    public void setSysytemName(String sysytemName) {
        this.sysytemName = sysytemName;
    }
}
