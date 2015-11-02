package com.ftd.schaepher.coursemanagement.db;
import net.tsz.afinal.annotation.sqlite.Id;
/**
 * Created by lenovo on 2015/11/2.
 */
public class System {
    @Id
    private String systemnumber;
    private String systemname;

    public String getSystemnumber() {
        return systemnumber;
    }

    public void setSystemnumber(String systemnumber) {
        this.systemnumber = systemnumber;
    }

    public String getSystemname() {
        return systemname;
    }

    public void setSystemname(String systemname) {
        this.systemname = systemname;
    }
}
