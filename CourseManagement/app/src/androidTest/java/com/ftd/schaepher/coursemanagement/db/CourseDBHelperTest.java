package com.ftd.schaepher.coursemanagement.db;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;

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

    /**
     * 用户相关测试
     */

    // 新建用户
    public void testCreateUser() {
        // 新建教师用户
        TableUserTeacher teacher1 = new TableUserTeacher();
        teacher1.setWorkNumber("00001");
        teacher1.setPassword("00001");
        teacher1.setName("西瓜1");
        teacher1.setSex("男");
        teacher1.setBirthday("19941226");
        teacher1.setTelephone("110");
        teacher1.setEmail("123456789@qq.com");
        teacher1.setDepartment("计算机系");
        dbHelper.insert(teacher1);

        TableUserTeacher teacher2 = new TableUserTeacher("00002","00002","西瓜2","男",
                "19941226","110","123456789@qq.com","计算机系");
        dbHelper.insert(teacher2);

        // 新建系负责人用户
        TableUserDepartmentHead head1 = new TableUserDepartmentHead();
        head1.setWorkNumber("00001");
        head1.setPassword("00001");
        head1.setName("冬瓜");
        head1.setSex("男");
        head1.setBirthday("19941226");
        head1.setTelephone("112");
        head1.setEmail("987654321@qq.com");
        head1.setDepartment("计算机系");
        dbHelper.insert(head1);

        TableUserDepartmentHead head2 = new TableUserDepartmentHead("00002","00002","冬瓜","男",
                "19941226","112","987654321@qq.com","计算机系");
        dbHelper.insert(head2);

        // 新建教学办用户
        TableUserTeachingOffice office1 = new TableUserTeachingOffice();
        office1.setWorkNumber("00001");
        office1.setPassword("00001");
        office1.setName("南瓜");
        office1.setTelephone("119");
        office1.setEmail("123459876@qq.com");
        dbHelper.insert(office1);

        TableUserTeachingOffice office2 = new TableUserTeachingOffice("00002","00002","南瓜",
                "119","123459876@qq.com");
        dbHelper.insert(office2);
    }

    // 根据工号查询用户
    public void testFindById() {
        TableUserTeacher teacher2;
        String number = "3443";
        teacher2 = dbHelper.findById(number, TableUserTeacher.class);
        assertEquals("张三", teacher2.getName());
    }

}
