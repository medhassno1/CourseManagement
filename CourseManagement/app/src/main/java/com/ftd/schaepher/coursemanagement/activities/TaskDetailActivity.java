package com.ftd.schaepher.coursemanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private String identity;

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
        identity = getSharedPreferences("userInformation", MODE_PRIVATE).getString("identity", null);
        getMenuInflater().inflate(R.menu.task_detail_activity_actions, menu);
        if (identity.equals("teacher")) {
            menu.removeItem(R.id.action_export_file);
            menu.findItem(R.id.action_commit_task).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_export_file:
                Log.d("TAG","export file");
                //点击导出文件逻辑
                return true;
            case R.id.action_commit_task:
                Log.d("TAG","commit task");
                //点击提交报课逻辑
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //点击查看文件跳转逻辑
    @Override
    public void onClick(View v) {
        startActivity(new Intent(TaskDetailActivity.this,ExcelDisplayActivity.class));
    }
}
