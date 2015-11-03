package com.ftd.schaepher.coursemanagement.db;
import net.tsz.afinal.annotation.sqlite.Id;
/**
 * Created by lenovo on 2015/10/31.
 */
public class TeachingDepartment {
    @Id
    private String number;
    private String pwd;
    private String name;
    private String phonenumber;

    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }
    public String getnumber() {
        return number;
    }
    public void setnumber(String name) {
        this.name = name;
    }
    public String getPsd() {
        return pwd;
    }
    public void setPsd(String name) {
        this.pwd = name;
    }
    public String getphonenumber() {
        return phonenumber;
    }
    public void setphonenumber(String name) {
        this.phonenumber = name;
    }

}
