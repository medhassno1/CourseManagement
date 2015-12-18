package com.ftd.schaepher.coursemanagement.tools;

import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Schaepher on 2015/11/20.
 */
public class JsonToolsTest extends TestCase {
    String jsonString;
    List<TableUserTeacher> expectedList;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        jsonString = "[{\"workNumber\":\"00001\",\"password\":\"00001\"," +
                "\"name\":\"西瓜\",\"sex\":\"男\",\"birthday\":\"1994\"," +
                "\"department\":\"\",\"telephone\":\"\",\"email\":\"\"}," +
                "{\"workNumber\":\"00002\",\"password\":\"00002\"," +
                "\"name\":\"南瓜\",\"sex\":\"男\",\"birthday\":\"1994\"," +
                "\"department\":\"\",\"telephone\":\"\",\"email\":\"\"}]";

        expectedList = new ArrayList<>();

        TableUserTeacher teacher1 = new TableUserTeacher();
        teacher1.setWorkNumber("00001");
        teacher1.setPassword("00001");
        teacher1.setName("西瓜");
        teacher1.setSex("男");
        teacher1.setBirthday("1994");
        teacher1.setDepartment("");
        teacher1.setTelephone("");
        teacher1.setEmail("");
        expectedList.add(teacher1);

        TableUserTeacher teacher2 = new TableUserTeacher();
        teacher2.setWorkNumber("00002");
        teacher2.setPassword("00002");
        teacher2.setName("南瓜");
        teacher2.setSex("男");
        teacher2.setBirthday("1994");
        teacher2.setDepartment("");
        teacher2.setTelephone("");
        teacher2.setEmail("");
        expectedList.add(teacher2);
    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
    }

    public void testGetJsonList() {
        List<TableUserTeacher> actualList = JsonTools.getJsonList(jsonString, TableUserTeacher.class);
        Assert.assertEquals(expectedList.toString(), actualList.toString());
    }

    public void testGetJsonArrayString() {
        String jsonString = JsonTools.getJsonString(expectedList);

        List<TableUserTeacher> actualList = JsonTools.getJsonList(jsonString, TableUserTeacher.class);
        Assert.assertEquals(expectedList.toString(), actualList.toString());
    }

    public void testGetJsonObject() {
        String jsonString = "{\"workNumber\":\"00001\",\"password\":\"00001\"," +
                "\"name\":\"西瓜\",\"sex\":\"男\",\"birthday\":\"1994\"," +
                "\"department\":\"\",\"telephone\":\"\",\"email\":\"\"}";
        TableUserDepartmentHead expectHead = new TableUserDepartmentHead();
        expectHead.setWorkNumber("00001");
        expectHead.setPassword("00001");
        expectHead.setName("西瓜");
        expectHead.setSex("男");
        expectHead.setBirthday("1994");
        expectHead.setDepartment("");
        expectHead.setTelephone("");
        expectHead.setEmail("");

        TableUserDepartmentHead actualHead = JsonTools.getJsonObject(jsonString,TableUserDepartmentHead.class);
        Assert.assertEquals(expectHead.toString(),actualHead.toString());
    }

    public void testGetJsonObjectString(){
        TableUserTeachingOffice expectOffice = new TableUserTeachingOffice();
        expectOffice.setWorkNumber("00001");
        expectOffice.setPassword("00001");
        expectOffice.setName("南瓜");
        expectOffice.setTelephone("119");
        expectOffice.setEmail("123459876@qq.com");
        String jsonArrayString = JsonTools.getJsonString(expectOffice);

        List<TableUserTeachingOffice> actualList  =
                JsonTools.getJsonList(jsonArrayString, TableUserTeachingOffice.class);
        Assert.assertEquals(expectOffice.toString(),actualList.get(0).toString());
    }



}
