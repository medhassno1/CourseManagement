package com.ftd.schaepher.coursemanagement.db;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;

/**
 * Created by Administrator on 2015/11/13.
 */
public class CourseDBHelperTest extends InstrumentationTestCase {

    CourseDBHelper dbHelper;
    Context context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
        dbHelper = new CourseDBHelper(context);

    }

    /*  public void testInsert()throws Exception{
          //建一个测试数据
          TableUserTeacher teacher = new TableUserTeacher();
          teacher.setWorkNumber("3443");
          teacher.setName("王五");
          dbHelper.insert(teacher);expected:<[张三]> but was:<[王五]>
      }
  */
    public void testFindById() {
        TableUserTeacher teacher2;
        String number = "3443";
        teacher2 = (TableUserTeacher) dbHelper.findById(number, TableUserTeacher.class);
        assertEquals("张三", teacher2.getName().toString());
    }

}
