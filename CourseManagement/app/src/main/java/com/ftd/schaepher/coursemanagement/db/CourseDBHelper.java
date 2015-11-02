package com.ftd.schaepher.coursemanagement.db;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import java.lang.*;
import java.util.Date;
import java.util.List;

public class CourseDBHelper {
    FinalDb db;
//创建数据库
    public void creatDataBase(Context context) {
        db = FinalDb.create(context, "teacherclass.db");
    }

    public void creatTableTeacher() {
        Teacher teacher = new Teacher();
        db.save(teacher);
    }

    public void creatTableTeachingDepartment() {
        TeachingDepartment teachingDepartmentr = new TeachingDepartment();
        db.save(teachingDepartmentr);
    }

    public void creatTableMajor(){
        Major major = new Major();
        db.save(major);
    }

    public void creatTableSystem(){
        System system =new System();
        db.save(system);
    }

    private void creatTableSystemLeader(){
        SystemLeader systemLeader =new SystemLeader();
        db.save(systemLeader);
    }
    private void creatTableClassTable(){
        ClassTable classTable =new ClassTable();
        db.save(classTable);
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

    public void findById(String id,Class<?> clazz){
        db.findById(id,clazz);
    }
    public void findall(Class<?> clazz){
        db.findAll(clazz);
    }



}





