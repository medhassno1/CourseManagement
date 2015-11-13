package com.ftd.schaepher.coursemanagement;

import android.app.Activity;
import android.test.InstrumentationTestCase;

import com.ftd.schaepher.coursemanagement.tools.ServerTools;

/**
 * Created by Schaepher on 2015/11/13.
 */
public class ServerToolsTest extends InstrumentationTestCase {
    Activity activity = new Activity();
    ServerTools serverTools = new ServerTools(activity.getApplicationContext());
}
