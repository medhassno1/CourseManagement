package com.ftd.schaepher.coursemanagement.tools;

import android.content.Context;
import android.test.AndroidTestCase;

import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;

/**
 * Created by Schaepher on 2015/11/13.
 */
public class ServerToolsTest extends AndroidTestCase {

    Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = this.getContext();
    }

    public void testServerTool() {
        ServerTools serverTools = new ServerTools(context);
        serverTools.postTableToServer(TableUserTeacher.class,ConstantTools.ID_TEACHER,ServerTools.INSERT_TABLE);
    }

}
