package com.ftd.schaepher.coursemanagement.db;

import android.bluetooth.BluetoothClass;
import android.content.Context;


import com.ftd.schaepher.coursemanagement.pojo.TableClass;
import com.ftd.schaepher.coursemanagement.pojo.TableMajor;
import com.ftd.schaepher.coursemanagement.pojo.TableSystem;
import com.ftd.schaepher.coursemanagement.pojo.TableSystemLeader;
import com.ftd.schaepher.coursemanagement.pojo.TableTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableTeachingDepartment;

import net.tsz.afinal.FinalDb;

/**
 * Created by lenovo on 2015/11/6.
 */
public class Initialize {
    CourseDBHelper dbHelper =new CourseDBHelper();


    public void init(Context context){
        dbHelper.creatDataBase(context);

        dbHelper.creatTableMajor();
        TableMajor major =new TableMajor();
        major.setMajor("计算机");
        dbHelper.insert(major);
        major.setMajor("数学");
        dbHelper.insert(major);
        major.setMajor("软件工程");
        dbHelper.insert(major);
        major.setMajor("网络工程");
        dbHelper.insert(major);
        major.setMajor("计算机实验班");
        dbHelper.insert(major);
        major.setMajor("数学实验班");
        dbHelper.insert(major);
        major.setMajor("信息安全");
        dbHelper.insert(major);

        dbHelper.creatTableSystem();
        TableSystem tableSystem =new TableSystem();
        tableSystem.setSysytemName("计算机系");
        dbHelper.insert(tableSystem);
        tableSystem.setSysytemName("软件工程系");
        dbHelper.insert(tableSystem);
        tableSystem.setSysytemName("数学系");
        dbHelper.insert(tableSystem);
        tableSystem.setSysytemName("信息安全系");
        dbHelper.insert(tableSystem);

        dbHelper.creatTableSystemLeader();
        TableSystemLeader tableSystemLeader =new TableSystemLeader();
        tableSystemLeader.setWorkNumber("00000");
        tableSystemLeader.setPsw("00000");
        tableSystemLeader.setName("张栋");
        tableSystemLeader.setTelephone("00000000000");
        dbHelper.insert(tableSystemLeader);
        tableSystemLeader.setWorkNumber("00001");
        tableSystemLeader.setPsw("00001");
        tableSystemLeader.setName("张冻");
        tableSystemLeader.setTelephone("00000000001");
        dbHelper.insert(tableSystemLeader);
        tableSystemLeader.setWorkNumber("00002");
        tableSystemLeader.setPsw("00002");
        tableSystemLeader.setName("张洞");
        tableSystemLeader.setTelephone("00000000002");
        dbHelper.insert(tableSystemLeader);
        tableSystemLeader.setWorkNumber("00003");
        tableSystemLeader.setPsw("00003");
        tableSystemLeader.setName("张动");
        tableSystemLeader.setTelephone("00000000003");
        dbHelper.insert(tableSystemLeader);
        tableSystemLeader.setWorkNumber("00004");
        tableSystemLeader.setPsw("00004");
        tableSystemLeader.setName("张咚");
        tableSystemLeader.setTelephone("00000000004");
        dbHelper.insert(tableSystemLeader);

        dbHelper.creatTableTeacher();
        TableTeacher tableTeacher =new TableTeacher();
        tableTeacher.setWorkNumber("02501");
        tableTeacher.setPwd("02501");
        tableTeacher.setName("陈楠楠");
        tableTeacher.setTelephone("00000000005");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02502");
        tableTeacher.setPwd("02502");
        tableTeacher.setName("陈世发");
        tableTeacher.setTelephone("00000000006");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02503");
        tableTeacher.setPwd("02503");
        tableTeacher.setName("黄星");
        tableTeacher.setTelephone("00000000006");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02504");
        tableTeacher.setPwd("02504");
        tableTeacher.setName("洪耀坤");
        tableTeacher.setTelephone("00000000007");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02505");
        tableTeacher.setPwd("02505");
        tableTeacher.setName("黄晓雄");
        tableTeacher.setTelephone("00000000008");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02506");
        tableTeacher.setPwd("02506");
        tableTeacher.setName("黄晓辉");
        tableTeacher.setTelephone("00000000009");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02507");
        tableTeacher.setPwd("02507");
        tableTeacher.setName("林培新");
        tableTeacher.setTelephone("00000000010");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02508");
        tableTeacher.setPwd("02508");
        tableTeacher.setName("黄云龙");
        tableTeacher.setTelephone("00000000011");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02509");
        tableTeacher.setPwd("02509");
        tableTeacher.setName("李洪");
        tableTeacher.setTelephone("000000000012");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02510");
        tableTeacher.setPwd("02510");
        tableTeacher.setName("李小龙");
        tableTeacher.setTelephone("000000000013");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02511");
        tableTeacher.setPwd("02511");
        tableTeacher.setName("苏钦辉");
        tableTeacher.setTelephone("00000000014");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02512");
        tableTeacher.setPwd("02512");
        tableTeacher.setName("吴伟坤");
        tableTeacher.setTelephone("00000000015");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02513");
        tableTeacher.setPwd("02513");
        tableTeacher.setName("钱仁法");
        tableTeacher.setTelephone("00000000016");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02514");
        tableTeacher.setPwd("02514");
        tableTeacher.setName("宋一博");
        tableTeacher.setTelephone("00000000017");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02515");
        tableTeacher.setPwd("02515");
        tableTeacher.setName("苏世杰");
        tableTeacher.setTelephone("00000000018");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02516");
        tableTeacher.setPwd("02516");
        tableTeacher.setName("苏晓强");
        tableTeacher.setTelephone("00000000019");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02517");
        tableTeacher.setPwd("02517");
        tableTeacher.setName("孙治书");
        tableTeacher.setTelephone("00000000020");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02518");
        tableTeacher.setPwd("02518");
        tableTeacher.setName("周斌");
        tableTeacher.setTelephone("00000000021");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02519");
        tableTeacher.setPwd("02519");
        tableTeacher.setName("颜海林");
        tableTeacher.setTelephone("00000000022");
        dbHelper.insert(tableTeacher);
        tableTeacher.setWorkNumber("02520");
        tableTeacher.setPwd("02520");
        tableTeacher.setName("张海山");
        tableTeacher.setTelephone("00000000022");
        dbHelper.insert(tableTeacher);

        dbHelper.creatTableClass();
        TableClass tableClass = new TableClass();
        tableClass.setClassName("Internet技术与协议分析实验");
        tableClass.setGrade("2012");
        tableClass.setMajor("计算机科学与技术");
        tableClass.setNum("87");
        tableClass.setType("实践选修");
        tableClass.setTime("24");
        tableClass.setClassCredit("1");
        dbHelper.insert(tableClass);
        tableClass.setClassName("计算机仿真技术");
        tableClass.setGrade("2012");
        tableClass.setMajor("计算机科学与技术");
        tableClass.setNum("87");
        tableClass.setType("实践选修");
        tableClass.setTime("24");
        tableClass.setClassCredit("1");
        tableClass.setOpTime("12");
        dbHelper.insert(tableClass);
        tableClass.setClassName("EDA技术");
        tableClass.setGrade("2012");
        tableClass.setMajor("计算机科学与技术");
        tableClass.setNum("87");
        tableClass.setType("专业方向");
        tableClass.setTime("32");
        tableClass.setClassCredit("2");
        tableClass.setPrTime("12");
        dbHelper.insert(tableClass);
        tableClass.setClassName("数字逻辑电路设计");
        tableClass.setGrade("2014");
        tableClass.setMajor("计算机类");
        tableClass.setNum("191");
        tableClass.setType("实践选修");
        tableClass.setTime("48");
        tableClass.setClassCredit("2");
        tableClass.setPrTime("18");
        dbHelper.insert(tableClass);

        dbHelper.creatTableTeachingDepartment();
        TableTeachingDepartment tableTeachingDepartment =new TableTeachingDepartment();
        tableTeachingDepartment.setNumber("10000");
        tableTeachingDepartment.setPwd("10000");
        tableTeachingDepartment.setName("张琴");
        tableTeachingDepartment.setTelephone("10000000000");
        dbHelper.insert(tableTeachingDepartment);
        tableTeachingDepartment.setNumber("10001");
        tableTeachingDepartment.setPwd("10001");
        tableTeachingDepartment.setName("张秦");
        tableTeachingDepartment.setTelephone("10000000001");
        dbHelper.insert(tableTeachingDepartment);


    }


}
