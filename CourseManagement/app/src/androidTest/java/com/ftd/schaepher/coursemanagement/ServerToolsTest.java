package com.ftd.schaepher.coursemanagement;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.ftd.schaepher.coursemanagement.tools.ServerTools;

/**
 * Created by Schaepher on 2015/11/13.
 */
public class ServerToolsTest extends InstrumentationTestCase {

    Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
    }

    public void testServerTool() {
        ServerTools serverTools = new ServerTools(context);
    }

}
