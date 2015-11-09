package com.ftd.schaepher.coursemanagement.db;

import android.content.Context;

import com.ftd.schaepher.coursemanagement.pojo.TableClass;
import com.ftd.schaepher.coursemanagement.pojo.TableMajor;
import com.ftd.schaepher.coursemanagement.pojo.TableSystem;
import com.ftd.schaepher.coursemanagement.pojo.TableSystemLeader;
import com.ftd.schaepher.coursemanagement.pojo.TableTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableTeachingDepartment;

import net.tsz.afinal.FinalDb;

import java.util.List;


public class CourseDBHelper {
    FinalDb db;
//创建数据库
    public void creatDataBase(Context context) {
        db = FinalDb.create(context, "teacherclass.db");
    }

    public void creatTableTeacher() {
        TableTeacher teacher = new TableTeacher();
        db.save(teacher);
    }

    /**
     * 功能：
     * @param
     * @return
     */
    public void creatTableTeachingDepartment() {
        TableTeachingDepartment tableTeachingDepartmentr = new TableTeachingDepartment();
        db.save(tableTeachingDepartmentr);
    }

    public void creatTableMajor(){
        TableMajor tableMajor = new TableMajor();
        db.save(tableMajor);
    }

    public void creatTableSystem(){
        TableSystem tableSystem =new TableSystem();
        db.save(tableSystem);
    }

    public void creatTableSystemLeader(){
        TableSystemLeader tableSystemLeader =new TableSystemLeader();
        db.save(tableSystemLeader);
    }

    public void creatTableClass(){
        TableClass tableClass =new TableClass();
        db.save(tableClass);
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





