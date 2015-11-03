package com.ftd.schaepher.coursemanagement.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ftd.schaepher.coursemanagement.R;

/**
 * Created by sxq on 2015/11/2.
 * 教师信息界面
 */
public class TeacherDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_detail);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("用户信息");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_detail_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}