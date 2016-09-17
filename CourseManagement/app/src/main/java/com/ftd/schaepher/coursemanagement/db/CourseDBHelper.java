package com.ftd.schaepher.coursemanagement.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableManageMajor;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Schaepher on 2016/9/16.
 */
public class CourseDBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "teacher_class.db";
    private static final int DATABASE_VERSION = 1;

    private static CourseDBHelper instance;
    private static Dao<TableUserTeacher, String> teacherDao = null;
    private static Dao<TableUserDepartmentHead, String> departmentHeadDao = null;
    private static Dao<TableUserTeachingOffice, String> officeDao = null;
    private static Dao<TableTaskInfo, String> taskInfoDao = null;
    private static Dao<TableManageMajor, Integer> manageMajorDao = null;
    private static Dao<TableCourseMultiline, String> courseMultilineDao = null;

    private CourseDBHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public CourseDBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }


    public static CourseDBHelper getInstance(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            instance = new CourseDBHelper(context);
        }
        return instance;
    }

    public Dao<TableUserTeacher, String> getTeacherDao() throws SQLException {
        if (teacherDao == null) {
            teacherDao = getDao(TableUserTeacher.class);
        }
        return teacherDao;
    }

    public Dao<TableUserDepartmentHead, String> getDepartmentHeadDao() throws SQLException {
        if (departmentHeadDao == null) {
            departmentHeadDao = getDao(TableUserDepartmentHead.class);
        }
        return departmentHeadDao;
    }

    public Dao<TableUserTeachingOffice, String> getOfficeDao() throws SQLException {
        if (officeDao == null) {
            officeDao = getDao(TableUserTeachingOffice.class);
        }
        return officeDao;
    }

    public Dao<TableTaskInfo, String> getTaskInfoDao() throws SQLException {
        if (taskInfoDao == null) {
            taskInfoDao = getDao(TableTaskInfo.class);
        }
        return taskInfoDao;
    }

    public Dao<TableManageMajor, Integer> getManageMajorDao() throws SQLException {
        if (manageMajorDao == null) {
            manageMajorDao = getDao(TableManageMajor.class);
        }
        return manageMajorDao;
    }

    public Dao<TableCourseMultiline, String> getCourseMultilineDao() throws SQLException {
        if (courseMultilineDao == null) {
            courseMultilineDao = getDao(TableCourseMultiline.class);
        }
        return courseMultilineDao;
    }

    public void createNewCourseTable() throws SQLException {
        TableUtils.createTable(connectionSource, TableCourseMultiline.class);
    }

    public void changeTableName(String from, String to) throws SQLException {
        getCourseMultilineDao().executeRaw("ALTER TABLE " + from + " RENAME TO " + to);
    }

    public void dropTable(String tableName) throws SQLException {
        getCourseMultilineDao().executeRaw("Drop table if exists " + tableName);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, TableUserTeacher.class);
            TableUtils.createTable(connectionSource, TableUserDepartmentHead.class);
            TableUtils.createTable(connectionSource, TableUserTeachingOffice.class);
            TableUtils.createTable(connectionSource, TableTaskInfo.class);
            TableUtils.createTable(connectionSource, TableManageMajor.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

    @Override
    public void close() {
        super.close();
        teacherDao = null;
        departmentHeadDao = null;
        officeDao = null;
        taskInfoDao = null;
        manageMajorDao = null;
        courseMultilineDao = null;
    }
}
