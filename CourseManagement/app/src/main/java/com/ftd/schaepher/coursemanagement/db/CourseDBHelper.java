package com.ftd.schaepher.coursemanagement.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableManageMajor;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;
import com.ftd.schaepher.coursemanagement.tools.Loger;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;


public class CourseDBHelper {
    private FinalDb db;
    private Context context;
    private SQLiteDatabase database;

    public CourseDBHelper(Context context) {
        this.context = context;
        db = FinalDb.create(context, "teacherclass.db");
    }

    public static final String CREATE_TABLE_COURSE_MULTILINE = "CREATE TABLE TableCourseMultiline "
            + "( insertTime text primary key, "
            + "workNumber text, "
            + "grade text, "
            + "major text , "
            + "people text, "
            + "courseName text, "
            + "courseType text, "
            + "courseCredit text, "
            + "courseHour text, "
            + "practiceHour text, "
            + "onMachineHour text, "
            + "timePeriod text,"
            + "teacherName text,"
            + "remark text)";

    // 创建数据库
    public void createDataBase(Context context) {
        db = FinalDb.create(context, "teacherclass.db");
    }

    public void createTableTeacher() {
        TableUserTeacher teacher = new TableUserTeacher();
        db.save(teacher);
    }

    public void createTableTeachingDepartment() {
        TableUserTeachingOffice tableTeachingDepartmentr = new TableUserTeachingOffice();
        db.save(tableTeachingDepartmentr);
    }

    public void createTableSystemLeader() {
        TableUserDepartmentHead tableUserDepartmentHead = new TableUserDepartmentHead();
        db.save(tableUserDepartmentHead);
    }

    public void createTableClass() {
        TableCourseMultiline tableCourseMultiline = new TableCourseMultiline();
        db.save(tableCourseMultiline);
    }

    public void createTableTask() {
        TableTaskInfo tableTaskInfo = new TableTaskInfo();
        db.save(tableTaskInfo);
    }

    public void createTableMajor() {
        TableManageMajor tableManageMajor = new TableManageMajor();
        db.save(tableManageMajor);
    }

    // 插入数据
    public void insert(Object entity) {
        db.save(entity);
    }

    public void insertAll(List list) {
        for (Object obj : list) {
            db.save(obj);
        }
    }

    // 删除数据，class为id为表的主键
    public void delete(Class<?> clazz, String id) {
        db.deleteById(clazz, id);
    }

    public void deleteAll(Class<?> clazz) {
        db.deleteByWhere(clazz, null);
    }

    // 改
    public void update(Object entity) {
        db.update(entity);
    }

    // 查
    public <T> T findById(String id, Class<T> clazz) {
        return db.findById(id, clazz);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        return db.findAll(clazz);
    }

    public <T> List<T> findAllOrder(Class<T> clazz) {
        return db.findAll(clazz, "year DESC,semester DESC");
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String where) {
        return db.findAllByWhere(clazz, where);
    }

    public SQLiteDatabase getDb() {
        if (database == null) {
            database = context.openOrCreateDatabase("teacherclass.db", Context.MODE_PRIVATE, null);
        }
        return database;
    }

    public List<String> getSemesterList() {
        String[] columns = new String[]{
                "year", "semester"
        };
        String orderBy = "year DESC,semester DESC";
        Cursor cursor = getDb()
                .query(true, "TableTaskInfo", columns, null, null, null, null, orderBy, null);
        List<String> semester = new ArrayList<>();
        while (cursor.moveToNext()) {
            semester.add(cursor.getString(0) + cursor.getString(1));
        }
        cursor.close();
        Loger.i("semester",semester.toString());
        return semester;
    }

}





