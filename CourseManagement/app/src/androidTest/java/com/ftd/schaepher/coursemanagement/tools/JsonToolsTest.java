package com.ftd.schaepher.coursemanagement.tools;

import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;

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
                "\"name\":\"陈世发\",\"sex\":\"男\",\"birthday\":\"19941226\"," +
                "\"department\":\"\",\"telephone\":\"\",\"email\":\"\"}," +
                "{\"workNumber\":\"031302501\",\"password\":\"031302501\"," +
                "\"name\":\"陈楠楠\",\"sex\":\"男\",\"birthday\":\"1994\"," +
                "\"department\":\"\",\"telephone\":\"\",\"email\":\"\"}]";

        expectedList = new ArrayList<>();

        TableUserTeacher teacher1 = new TableUserTeacher();
        teacher1.setWorkNumber("00001");
        teacher1.setPassword("00001");
        teacher1.setName("陈世发");
        teacher1.setSex("男");
        teacher1.setBirthday("19941226");
        teacher1.setDepartment("");
        teacher1.setTelephone("");
        teacher1.setEmail("");
        expectedList.add(teacher1);

        TableUserTeacher teacher2 = new TableUserTeacher();
        teacher2.setWorkNumber("031302501");
        teacher2.setPassword("031302501");
        teacher2.setName("陈楠楠");
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

    public void testGetJsonString() {
        String string = JsonTools.getJsonString(expectedList);
        assertSame(jsonString, string);

    }

}
