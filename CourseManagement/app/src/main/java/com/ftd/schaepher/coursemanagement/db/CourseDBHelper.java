package com.ftd.schaepher.coursemanagement.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ftd.schaepher.coursemanagement.pojo.Course;
import com.ftd.schaepher.coursemanagement.tools.ExcelTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Schaepher on 2015/10/27.
 */
public class CourseDBHelper {

    public Context context;
    private DBOpenHelper helper ;
    private SQLiteDatabase db ;
    private static final String TABLE_NAME = "tb_course";//数据库中表的名称
    //private String filePath="/storage/emulated/0/tencent/QQfile_recv/course.xls";
    public String filePath = "mnt/sdcard/course.xls";

    public class DBOpenHelper extends SQLiteOpenHelper {
        private static final int VERSION = 1;// 定义数据库版本号
        private static final String DBNAME = "school.db";// 定义数据库名

        public DBOpenHelper(Context context) {// 定义构造函数
            super(context, DBNAME, null, VERSION);// 重写基类的构造函数
        }

        //数据库第一次被创建时onCreate会被调用
        @Override
        public void onCreate(SQLiteDatabase db) {// 创建课程信息表
            db.execSQL("create table IF NOT EXISTS tb_course ( grade text,major text,sum text,"
                    +"courseName text primary key,type text,credit text,classHour text,"
                    +"experimentHour text,computerHour text,fromToEnd text,"
                    +"teacher text,note text)");

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // 覆写基类的onUpgrade方法，以便数据库版本更新
        }
    }

    public CourseDBHelper(Context con) {
        context = con;
        helper = new DBOpenHelper(context);
        Log.i("string", "DBManager构造函数");
//        init();
    }

    public  void init(){ //将excel表格中的数据导入数据库
        ExcelTools ex = new ExcelTools(context);
        //  ex.setPath(filePath);
        List<Course> list = ex.readExcel();
        for(int i=0;i<list.size();i++){//循环导入数据
            Course course = list.get(i);
            insert(course);
        }
        Log.i("string", "表初始化成功");

    }

    public String[] allName(){//返回所有课程名
        String courseName="";
        ArrayList<String> list = new ArrayList<>();

        if(helper==null )
            Log.i("string", "helper为空指针");
        else
            Log.i("string", "helper不为空指针");
        db = helper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"courseName"},null,null,null,null,null);

        Log.i("string",String.valueOf(cursor.getColumnCount()));
        Log.i("string",String.valueOf(cursor.getCount()));
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//遍历记录
            courseName = cursor.getString(cursor.getColumnIndex("courseName"));
            list.add(courseName);
        }
        cursor.close();
        String[] names =list.toArray(new String[0]);//将list转换成String[ ]
        return names;
    }

    public Course find(String Name){//根据课程名查询数据
        Course course = new Course();
        db = helper.getWritableDatabase();

        //query只要有条件查询会返回空值，查询失败
        //临时解决办法：查询所有课程，在遍历匹配课程
        Log.i("string", "开始查询");
        Cursor cursor = db.query(TABLE_NAME,null,null,null,null,null,null);
        Log.i("string", String.valueOf(cursor.getColumnCount()));
        Log.i("string", String.valueOf(cursor.getCount()));
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//遍历记录
            String courseName = cursor.getString(cursor.getColumnIndex("courseName"));
            //返回值courseName存在全角空格
            //直接用半角空格直接替换调字符串中所有的全角空格，然后再用trim()方法去掉首尾的空格
            courseName=courseName.replaceAll("[ |　]", " ").trim();
            Log.i("string","["+courseName+"]"+"  "+(courseName.equals(Name))+"["+Name+"]");
            if(courseName.contains(Name)) break;  //找到所求课程就退出
        }

        if(!cursor.isAfterLast()){

            course.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
            course.setMajor(cursor.getString(cursor.getColumnIndex("major")));
            course.setSum(cursor.getString(cursor.getColumnIndex("sum")));
            course.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
            course.setType(cursor.getString(cursor.getColumnIndex("type")));
            course.setCredit(cursor.getString(cursor.getColumnIndex("credit")));
            course.setClassHour(cursor.getString(cursor.getColumnIndex("classHour")));
            course.setExperimentHour(cursor.getString(cursor.getColumnIndex("experimentHour")));
            course.setComputerHour(cursor.getString(cursor.getColumnIndex("computerHour")));
            course.setFromToEnd(cursor.getString(cursor.getColumnIndex("fromToEnd")));
            course.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
            course.setNote(cursor.getString(cursor.getColumnIndex("note")));

            cursor.close();
            return course;

        }else
            Log.i("string","查询返回为空");
        cursor.close();
        return null;
    }


    public void insert(Course course){  //向表插入数据
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();//ContentValues用来储存数据；

        values.put("grade",course.getGrade().trim());
        values.put("major",course.getMajor().trim());
        values.put("sum",course.getSum().trim());
        values.put("courseName",course.getCourseName().trim());
        values.put("type",course.getType().trim());
        values.put("credit",course.getCredit().trim());
        values.put("classHour",course.getClassHour().trim());
        values.put("experimentHour",course.getExperimentHour().trim());
        values.put("computerHour",course.getComputerHour().trim());
        values.put("fromToEnd",course.getFromToEnd().trim());
        values.put("teacher",course.getTeacher().trim());
        values.put("note", course.getNote().trim());

        if(db.insert(TABLE_NAME, null, values)!=-1){
            Log.i("string1",course.getCourseName()+"**导入成功**");}
        else
            Log.i("string1",course.getCourseName()+"**导入失败**");
    }


}
