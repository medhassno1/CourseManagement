package com.ftd.schaepher.coursemanagement.tools;

import android.test.InstrumentationTestCase;

import java.io.IOException;

/**
 * Created by Schaepher on 2015/11/13.
 */
public class NetworkManagerTest extends InstrumentationTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();

        try {
            NetworkManager manager = new NetworkManager();
            String jsonString = manager.getJsonString(ConstantTools.TABLE_DEPARTMENT_HEAD);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
