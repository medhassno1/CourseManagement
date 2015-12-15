package com.ftd.schaepher.coursemanagement.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

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
    private FinalDb finalDb;
    private SQLiteDatabase database;

    public CourseDBHelper(Context context) {
        finalDb = FinalDb.create(context, "teacherclass.db");
        database = context.openOrCreateDatabase("teacherclass.db", Context.MODE_PRIVATE, null);
//        initTable();
    }

    private void initTable() {
        TableUserTeacher teacher = new TableUserTeacher();
        finalDb.save(teacher);

        TableUserDepartmentHead tableUserDepartmentHead = new TableUserDepartmentHead();
        finalDb.save(tableUserDepartmentHead);

        TableUserTeachingOffice teachingOffice = new TableUserTeachingOffice();
        finalDb.save(teachingOffice);

        TableTaskInfo tableTaskInfo = new TableTaskInfo();
        finalDb.save(tableTaskInfo);

        TableManageMajor tableManageMajor = new TableManageMajor();
        finalDb.save(tableManageMajor);
    }

    public void createNewCourseTable() {
        String createTableCourseMultiline = "CREATE TABLE TableCourseMultiline "
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
        database.execSQL(createTableCourseMultiline);
    }

    // 增
    public void insert(Object entity) {
        finalDb.save(entity);
    }

    public void insertAll(List list) {
        for (Object obj : list) {
            insertOrUpdate(obj);
        }
    }

    public void insertOrUpdate(Object entity) {
        try {
            finalDb.save(entity);
        } catch (SQLiteException e) {
            finalDb.update(entity);
        }
    }

    // 删
    public void deleteByID(Class<?> clazz, String id) {
        finalDb.deleteById(clazz, id);
    }

    public void deleteAll(Class<?> clazz) {
        finalDb.deleteByWhere(clazz, null);
    }

    // 改
    public void update(Object entity) {
        finalDb.update(entity);
    }

    // 查
    public <T> T findById(String id, Class<T> clazz) {
        return finalDb.findById(id, clazz);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        return finalDb.findAll(clazz);
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String where) {
        return finalDb.findAllByWhere(clazz, where);
    }

    public List<String> getSemesterList() {
        String[] columns = new String[]{
                "year", "semester"
        };
        String orderBy = "year DESC,semester DESC";
        Cursor cursor = database
                .query(true, "TableTaskInfo", columns, null, null, null, null, orderBy, null);
        List<String> semester = new ArrayList<>();
        while (cursor.moveToNext()) {
            semester.add(cursor.getString(0) + cursor.getString(1));
        }
        cursor.close();
        Loger.i("semester", semester.toString());
        return semester;
    }

    public void changeTableName(String from, String to) {
        database.execSQL("ALTER TABLE " + from + " RENAME TO " + to);
    }

    public void dropTable(String tableName) {
        database.execSQL("Drop table if exists " + tableName);
    }

    public boolean getCommitState(String workNumber) {
        List<TableCourseMultiline> list =
                finalDb.findAllByWhere(TableCourseMultiline.class, "workNumber = \"" + workNumber + "\"");
//            Loger.d("isfinish", String.valueOf(list.size()));
        if (list != null && list.size() > 1) {
            return true;
        } else {
            return false;
        }
    }

    public void close() {
        database.close();
    }

}





