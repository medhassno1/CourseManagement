package com.ftd.schaepher.coursemanagement.db;

import android.content.Context;

import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;

/**
 * Created by lenovo on 2015/11/6.
 */
public class Initialize {
    CourseDBHelper dbHelper = new CourseDBHelper();


    public void init(Context context) {
        dbHelper.createDataBase(context);

        dbHelper.createTableSystemLeader();
        TableUserDepartmentHead tableUserDepartmentHead = new TableUserDepartmentHead();
        tableUserDepartmentHead.setWorkNumber("10000");
        tableUserDepartmentHead.setPassword("10000");
        tableUserDepartmentHead.setName("张栋2323");
        tableUserDepartmentHead.setTelephone("00000000000");
        dbHelper.insert(tableUserDepartmentHead);
        tableUserDepartmentHead.setWorkNumber("00000");
        tableUserDepartmentHead.setPassword("00000");
        tableUserDepartmentHead.setName("张栋");
        tableUserDepartmentHead.setTelephone("00000000000");
        dbHelper.insert(tableUserDepartmentHead);
        tableUserDepartmentHead.setWorkNumber("00001");
        tableUserDepartmentHead.setPassword("00001");
        tableUserDepartmentHead.setName("张冻");
        tableUserDepartmentHead.setTelephone("00000000001");
        dbHelper.insert(tableUserDepartmentHead);
        tableUserDepartmentHead.setWorkNumber("00002");
        tableUserDepartmentHead.setPassword("00002");
        tableUserDepartmentHead.setName("张洞");
        tableUserDepartmentHead.setTelephone("00000000002");
        dbHelper.insert(tableUserDepartmentHead);
        tableUserDepartmentHead.setWorkNumber("00003");
        tableUserDepartmentHead.setPassword("00003");
        tableUserDepartmentHead.setName("张动");
        tableUserDepartmentHead.setTelephone("00000000003");
        dbHelper.insert(tableUserDepartmentHead);
        tableUserDepartmentHead.setWorkNumber("00004");
        tableUserDepartmentHead.setPassword("00004");
        tableUserDepartmentHead.setName("张咚");
        tableUserDepartmentHead.setTelephone("00000000004");
        dbHelper.insert(tableUserDepartmentHead);

        dbHelper.createTableTeacher();
        TableUserTeacher tableUserTeacher = new TableUserTeacher();

        tableUserTeacher.setWorkNumber("10000");
        tableUserTeacher.setPassword("10000");
        tableUserTeacher.setName("陈");
        tableUserTeacher.setTelephone("00000000006");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02501");
        tableUserTeacher.setPassword("02501");
        tableUserTeacher.setName("陈楠楠");
        tableUserTeacher.setSex("男");
        tableUserTeacher.setTelephone("00000000005");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02502");
        tableUserTeacher.setPassword("02502");
        tableUserTeacher.setName("陈世发");
        tableUserTeacher.setSex("男");
        tableUserTeacher.setTelephone("00000000006");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02503");
        tableUserTeacher.setPassword("02503");
        tableUserTeacher.setName("黄星");
        tableUserTeacher.setSex("男");
        tableUserTeacher.setTelephone("00000000006");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02504");
        tableUserTeacher.setPassword("02504");
        tableUserTeacher.setName("洪耀坤");
        tableUserTeacher.setSex("男");
        tableUserTeacher.setTelephone("00000000007");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02505");
        tableUserTeacher.setPassword("02505");
        tableUserTeacher.setName("黄晓雄");
        tableUserTeacher.setTelephone("00000000008");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02506");
        tableUserTeacher.setPassword("02506");
        tableUserTeacher.setName("黄晓辉");
        tableUserTeacher.setTelephone("00000000009");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02507");
        tableUserTeacher.setPassword("02507");
        tableUserTeacher.setName("林培新");
        tableUserTeacher.setTelephone("00000000010");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02508");
        tableUserTeacher.setPassword("02508");
        tableUserTeacher.setName("黄云龙");
        tableUserTeacher.setTelephone("00000000011");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02509");
        tableUserTeacher.setPassword("02509");
        tableUserTeacher.setName("李洪");
        tableUserTeacher.setTelephone("000000000012");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02510");
        tableUserTeacher.setPassword("02510");
        tableUserTeacher.setName("李小龙");
        tableUserTeacher.setTelephone("000000000013");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02511");
        tableUserTeacher.setPassword("02511");
        tableUserTeacher.setName("苏钦辉");
        tableUserTeacher.setTelephone("00000000014");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02512");
        tableUserTeacher.setPassword("02512");
        tableUserTeacher.setName("吴伟坤");
        tableUserTeacher.setTelephone("00000000015");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02513");
        tableUserTeacher.setPassword("02513");
        tableUserTeacher.setName("钱仁法");
        tableUserTeacher.setTelephone("00000000016");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02514");
        tableUserTeacher.setPassword("02514");
        tableUserTeacher.setName("宋一博");
        tableUserTeacher.setTelephone("00000000017");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02515");
        tableUserTeacher.setPassword("02515");
        tableUserTeacher.setName("苏世杰");
        tableUserTeacher.setTelephone("00000000018");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02516");
        tableUserTeacher.setPassword("02516");
        tableUserTeacher.setName("苏晓强");
        tableUserTeacher.setTelephone("00000000019");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02517");
        tableUserTeacher.setPassword("02517");
        tableUserTeacher.setName("孙治书");
        tableUserTeacher.setTelephone("00000000020");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02518");
        tableUserTeacher.setPassword("02518");
        tableUserTeacher.setName("周斌");
        tableUserTeacher.setTelephone("00000000021");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02519");
        tableUserTeacher.setPassword("02519");
        tableUserTeacher.setName("颜海林");
        tableUserTeacher.setTelephone("00000000022");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        tableUserTeacher.setWorkNumber("02520");
        tableUserTeacher.setPassword("02520");
        tableUserTeacher.setName("张海山");
        tableUserTeacher.setTelephone("00000000022");
        tableUserTeacher.setDepartment("计算机类");
        dbHelper.insert(tableUserTeacher);

        dbHelper.createTableClass();
        TableCourseMultiline tableCourseMultiline = new TableCourseMultiline();
        tableCourseMultiline.setCourseName("Internet技术与协议分析实验");
        tableCourseMultiline.setGrade("2012");
        tableCourseMultiline.setMajor("计算机科学与技术");
        tableCourseMultiline.setPeople("87");
        tableCourseMultiline.setCourseHour("24");
        tableCourseMultiline.setCourseCredit("1");
        dbHelper.insert(tableCourseMultiline);
        tableCourseMultiline.setCourseName("计算机仿真技术");
        tableCourseMultiline.setGrade("2012");
        tableCourseMultiline.setMajor("计算机科学与技术");
        tableCourseMultiline.setPeople("87");
        tableCourseMultiline.setCourseType("实践选修");
        tableCourseMultiline.setCourseHour("24");
        tableCourseMultiline.setCourseCredit("1");
        tableCourseMultiline.setPracticeHour("12");
        dbHelper.insert(tableCourseMultiline);
        tableCourseMultiline.setCourseName("EDA技术");
        tableCourseMultiline.setGrade("2012");
        tableCourseMultiline.setMajor("计算机科学与技术");
        tableCourseMultiline.setPeople("87");
        tableCourseMultiline.setCourseType("专业方向");
        tableCourseMultiline.setCourseHour("32");
        tableCourseMultiline.setCourseCredit("2");
        tableCourseMultiline.setOnMachineHour("12");
        dbHelper.insert(tableCourseMultiline);
        tableCourseMultiline.setCourseName("数字逻辑电路设计");
        tableCourseMultiline.setGrade("2014");
        tableCourseMultiline.setMajor("计算机类");
        tableCourseMultiline.setPeople("191");
        tableCourseMultiline.setCourseType("实践选修");
        tableCourseMultiline.setCourseHour("48");
        tableCourseMultiline.setCourseCredit("2");
        tableCourseMultiline.setOnMachineHour("18");
        dbHelper.insert(tableCourseMultiline);

        dbHelper.createTableTeachingDepartment();
        TableUserTeachingOffice tableUserTeachingOffice = new TableUserTeachingOffice();
        tableUserTeachingOffice.setWorkNumber("10000");
        tableUserTeachingOffice.setPassword("10000");
        tableUserTeachingOffice.setName("张琴");
        tableUserTeachingOffice.setTelephone("10000000000");
        dbHelper.insert(tableUserTeachingOffice);
        tableUserTeachingOffice.setWorkNumber("10001");
        tableUserTeachingOffice.setPassword("10001");
        tableUserTeachingOffice.setName("张秦");
        tableUserTeachingOffice.setTelephone("10000000001");
        dbHelper.insert(tableUserTeachingOffice);

        dbHelper.createTableTask();
        TableTaskInfo task = new TableTaskInfo();
        task.setRelativeTable("tc_com_exc");
        task.setYear("2015");
        task.setSemester("01");
        task.setDepartmentDeadline("2015-11-11");
        task.setTeacherDeadline("2015-11-8");
        task.setRemark("务必在截止时间前完成");
        task.setTaskState("0");
        dbHelper.insert(task);
        task.setRelativeTable("tc_com_nor");
        task.setYear("2015");
        task.setSemester("01");
        task.setDepartmentDeadline("2015-11-11");
        task.setTeacherDeadline("2015-11-8");
        task.setRemark("务必在截止时间前完成");
        task.setTaskState("0");
        dbHelper.insert(task);
        task.setRelativeTable("tc_com_ope");
        task.setYear("2015");
        task.setSemester("01");
        task.setDepartmentDeadline("2015-11-11");
        task.setTeacherDeadline("2015-11-8");
        task.setRemark("务必在截止时间前完成");
        task.setTaskState("0");
        dbHelper.insert(task);
        task.setRelativeTable("tc_inf_sec");
        task.setYear("2015");
        task.setSemester("01");
        task.setDepartmentDeadline("2015-11-11");
        task.setTeacherDeadline("2015-11-8");
        task.setRemark("务必在截止时间前完成");
        task.setTaskState("0");
        dbHelper.insert(task);
        task.setRelativeTable("tc_math_nor");
        task.setYear("2015");
        task.setSemester("01");
        task.setDepartmentDeadline("2015-11-11");
        task.setTeacherDeadline("2015-11-8");
        task.setRemark("务必在截止时间前完成");
        task.setTaskState("0");
        dbHelper.insert(task);
        task.setRelativeTable("tc_math_ope");
        task.setYear("2015");
        task.setSemester("01");
        task.setDepartmentDeadline("2015-11-11");
        task.setTeacherDeadline("2015-11-8");
        task.setRemark("务必在截止时间前完成");
        task.setTaskState("0");
        dbHelper.insert(task);
        task.setRelativeTable("tc_net_pro");
        task.setYear("2015");
        task.setSemester("01");
        task.setDepartmentDeadline("2015-11-11");
        task.setTeacherDeadline("2015-11-8");
        task.setRemark("务必在截止时间前完成");
        task.setTaskState("0");
        dbHelper.insert(task);
        task.setRelativeTable("tc_soft_pro");
        task.setYear("2015");
        task.setSemester("01");
        task.setDepartmentDeadline("2015-11-11");
        task.setTeacherDeadline("2015-11-8");
        task.setRemark("务必在截止时间前完成");
        task.setTaskState("0");
        dbHelper.insert(task);
    }
}
