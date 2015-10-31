package com.ftd.schaepher.coursemanagement.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.ftd.schaepher.coursemanagement.R;

/**
 * Created by sxq on 2015/10/31.
 * 发布任务界面
 */
public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("发布报课任务");
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }
}
