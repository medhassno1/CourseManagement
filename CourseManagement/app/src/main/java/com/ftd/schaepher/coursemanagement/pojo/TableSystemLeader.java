package com.ftd.schaepher.coursemanagement.pojo;
import net.tsz.afinal.annotation.sqlite.Id;
/**
 * Created by lenovo on 2015/11/2.
 */
public class TableSystemLeader {
    @Id
    private String workNumber;
    private String psw;
    private String name;
    private String telephone;

    public String getNumber() {
        return workNumber;
    }

    public void setNumber(String number) {
        this.workNumber = number;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}
