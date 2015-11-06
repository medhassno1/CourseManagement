package com.ftd.schaepher.coursemanagement.pojo;
import net.tsz.afinal.annotation.sqlite.Id;
/**
 * Created by lenovo on 2015/10/31.
 */
public class TableTeachingDepartment {
    @Id
    private String workNumber;
    private String pwd;
    private String name;
    private String telephone;

    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }
    public String getnumber() {
        return workNumber;
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
        return telephone;
    }
    public void setphonenumber(String name) {
        this.telephone = name;
    }

}
