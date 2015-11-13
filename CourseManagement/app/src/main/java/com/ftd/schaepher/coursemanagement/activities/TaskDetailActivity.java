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
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;

/**
 * Created by sxq on 2015/10/31.
 * 任务详情页面
 */
public class TaskDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cardvTaskDetail;
    private TextView tvDepartmentDeadline;
    private TextView tvTeacherDeadline;
    private TextView tvTaskRemark;
    private TextView tvTaskState;
    private TextView tvTaskName;
    private TextView tvTaskTerm;

    private String identity;
    private String taskId;
    private TableTaskInfo task;
    private CourseDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_task_detail);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("报课任务详情");
        mActionBar.setDisplayHomeAsUpEnabled(true);

        taskId = getIntent().getStringExtra("taskId");

        dbHelper = new CourseDBHelper(this);
        initWidgetValue();
    }

    //初始化控件数据
    private void initWidgetValue() {
        tvTaskTerm = (TextView) findViewById(R.id.tv_task_detail_term);
        tvTeacherDeadline = (TextView) findViewById(R.id.tv_task_detail_teacher_deadline);
        tvDepartmentDeadline = (TextView) findViewById(R.id.tv_task_detail_department_deadline);
        tvTaskRemark = (TextView) findViewById(R.id.tv_task_detail_remark);
        tvTaskState = (TextView) findViewById(R.id.tv_task_detail_state);
        tvTaskName = (TextView) findViewById(R.id.tv_task_detail_name);
        cardvTaskDetail = (CardView) findViewById(R.id.cardv_task_detail);
        cardvTaskDetail.setOnClickListener(this);

        Log.d("TASKID",taskId);
        task = (TableTaskInfo) dbHelper.findById(taskId,TableTaskInfo.class);
        Log.d("TAG", task.toString());
        String taskTerm = task.getYear()+task.getSemester();
        tvTaskTerm.setText(taskTerm);
        tvDepartmentDeadline.setText(task.getDepartmentDeadline());
        tvTeacherDeadline.setText(task.getTeacherDeadline());
        tvTaskRemark.setText(task.getRemark());
        tvTaskState.setText(TaskListActivity.taskStateMap(task.getTaskState()));
        tvTaskName.setText(TaskListActivity.taskNameChineseMapEnglisg(task.getRelativeTable()));
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

    //标题栏图标点击事件
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
