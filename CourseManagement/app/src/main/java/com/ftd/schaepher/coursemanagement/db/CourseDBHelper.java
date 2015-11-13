package com.ftd.schaepher.coursemanagement.db;

import android.content.Context;

import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;

import net.tsz.afinal.FinalDb;

import java.util.List;


public class CourseDBHelper {
    public FinalDb db;

    public CourseDBHelper(Context context) {
        db = FinalDb.create(context, "teacherclass.db");
    }

    public CourseDBHelper() {
    }

    //创建数据库
    public void creatDataBase(Context context) {
        db = FinalDb.create(context, "teacherclass.db");
    }

    public void creatTableTeacher() {
        TableUserTeacher teacher = new TableUserTeacher();
        db.save(teacher);
    }

    /**
     * 功能：
     * @param
     * @return
     */
    public void creatTableTeachingDepartment() {
        TableUserTeachingOffice tableTeachingDepartmentr = new TableUserTeachingOffice();
        db.save(tableTeachingDepartmentr);
    }

    public void creatTableSystemLeader(){
        TableUserDepartmentHead tableUserDepartmentHead =new TableUserDepartmentHead();
        db.save(tableUserDepartmentHead);
    }

    public void creatTableClass(){
        TableCourseMultiline tableCourseMultiline =new TableCourseMultiline();
        db.save(tableCourseMultiline);
    }

    public void createTableTask(){
        TableTaskInfo tableTaskInfo = new TableTaskInfo();
        db.save(tableTaskInfo);
    }

    //插入数据
    public void insert(Object entity) {
        db.save(entity);

    }
    //,删除数据，class为id为表的主键
    public void delete(Class<?> clazz,String id){
        db.deleteById(clazz, id);
    }
    //改
    public void update(Object entity){
        db.update(entity);
    }
    //查

    public Object findById(String id,Class<?> clazz){
       return db.findById(id,clazz);
    }
    public List findall(Class<?> clazz){
         return db.findAll(clazz);
    }



}





