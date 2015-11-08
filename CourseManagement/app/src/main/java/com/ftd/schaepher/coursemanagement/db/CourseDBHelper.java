package com.ftd.schaepher.coursemanagement.db;
import android.content.Context;

import com.ftd.schaepher.coursemanagement.pojo.SystemLeader;
import com.ftd.schaepher.coursemanagement.pojo.TableClass;
import com.ftd.schaepher.coursemanagement.pojo.TableMajor;
import com.ftd.schaepher.coursemanagement.pojo.TableTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableTeachingDepartment;

import net.tsz.afinal.FinalDb;


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

    public void creatTableTeachingDepartment() {
        TableTeachingDepartment tableTeachingDepartmentr = new TableTeachingDepartment();
        db.save(tableTeachingDepartmentr);
    }

    public void creatTableMajor(){
        TableMajor tableMajor = new TableMajor();
        db.save(tableMajor);
    }

    public void creatTableSystem(){
        com.ftd.schaepher.coursemanagement.pojo.System system =new com.ftd.schaepher.coursemanagement.pojo.System();
        db.save(system);
    }

    private void creatTableSystemLeader(){
        SystemLeader systemLeader =new SystemLeader();
        db.save(systemLeader);
    }
    private void creatTableClassTable(){
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

    public void findById(String id,Class<?> clazz){
        db.findById(id,clazz);
    }
    public void findall(Class<?> clazz){
        db.findAll(clazz);
    }



}





