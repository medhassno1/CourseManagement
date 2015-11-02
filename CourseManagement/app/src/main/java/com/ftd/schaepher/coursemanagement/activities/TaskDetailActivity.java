package com.ftd.schaepher.coursemanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ftd.schaepher.coursemanagement.R;

/**
 * Created by sxq on 2015/10/31.
 * 任务详情页面
 */
public class TaskDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cardvTaskDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_task_detail);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("报课任务详情");
        mActionBar.setDisplayHomeAsUpEnabled(true);


        cardvTaskDetail = (CardView) findViewById(R.id.cardv_task_detail);
        cardvTaskDetail.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_detail_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //点击查看文件跳转逻辑
    @Override
    public void onClick(View v) {
        startActivity(new Intent(TaskDetailActivity.this,ExcelDisplayActivity.class));
    }
}
