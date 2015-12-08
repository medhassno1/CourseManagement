package com.ftd.schaepher.coursemanagement;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.tools.ConstantTools;
import com.ftd.schaepher.coursemanagement.tools.JsonTools;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by Schaepher on 2015/11/13.
 */
public class NetworkManagerTest extends InstrumentationTestCase {

    Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
    }

    @Override
    protected void runTest() throws Throwable {

    }
}
