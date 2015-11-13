package com.ftd.schaepher.coursemanagement.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;

import java.util.Calendar;

/**
 * Created by sxq on 2015/10/31.
 * 发布任务界面
 */
public class TaskCreationActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private EditText edtTxDepartmentDeadline;
    private EditText edtTxTeacherDeadline;
    private EditText edtTxTaskName;
    private EditText edtTxTaskTeam;
    private EditText edtTxTaskRemark;
    private Button btnImportFile;

    private CourseDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_add_task);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("发布报课任务");

        initWidgetAndListener();
    }

    //初始化控件及绑定监听事件
    private void initWidgetAndListener() {
        edtTxDepartmentDeadline = (EditText) findViewById(R.id.edtTx_add_task_department_deadline);
        edtTxTeacherDeadline = (EditText) findViewById(R.id.edtTx_add_task_teacher_deadline);
        edtTxTaskName = (EditText) findViewById(R.id.edtTx_add_task_name);
        edtTxTaskTeam = (EditText) findViewById(R.id.edtTx_add_task_team);
        edtTxTaskRemark = (EditText) findViewById(R.id.edtTx_add_task_note);
        btnImportFile = (Button) findViewById(R.id.btn_add_task_import_file);

        edtTxTeacherDeadline.setOnFocusChangeListener(this);
        edtTxDepartmentDeadline.setOnFocusChangeListener(this);
        edtTxTaskName.setOnFocusChangeListener(this);

        edtTxTaskName.setOnClickListener(this);
        btnImportFile.setOnClickListener(this);
        edtTxDepartmentDeadline.setOnClickListener(this);
        edtTxTeacherDeadline.setOnClickListener(this);
        edtTxDepartmentDeadline.setInputType(InputType.TYPE_NULL);
        edtTxTeacherDeadline.setInputType(InputType.TYPE_NULL);
        edtTxTaskName.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_creation_activity_actions, menu);
        return true;
    }

    //标题栏图标的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_release_task:
                final SimpleDialog notificationDialog = new SimpleDialog(TaskCreationActivity.this);
                notificationDialog.title("是否确认发布此任务？")
                        .positiveAction("确认")
                        .negativeAction("取消")
                        .show();
                notificationDialog.positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbHelper = new CourseDBHelper(TaskCreationActivity.this);
                        dbHelper.insert(getNewTaskInfomation());
                        finish();
                    }
                });
                notificationDialog.negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificationDialog.cancel();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //获取即将发布的任务的信息
    private TableTaskInfo getNewTaskInfomation() {
        TableTaskInfo newTask = new TableTaskInfo();
        newTask.setYear("2015");
        newTask.setSemester("02");
        newTask.setTaskState("0");
        newTask.setDepartmentDeadline(edtTxDepartmentDeadline.getText().toString());
        newTask.setTeacherDeadline(edtTxTeacherDeadline.getText().toString());
        newTask.setRemark(edtTxTaskRemark.getText().toString());
        newTask.setRelativeTable(taskNameChineseMapEnglisg(edtTxTaskName.getText().toString()) + "201502");
        return newTask;
    }

    //控件的焦点监听事件
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edtTx_add_task_department_deadline:
                if (hasFocus) {
                    onClick(v);
                }
                break;
            case R.id.edtTx_add_task_teacher_deadline:
                if (hasFocus) {
                    onClick(v);
                }
                break;
            case R.id.edtTx_add_task_name: {
                if (hasFocus) {
                    onClick(v);
                }
            }
            default:
                break;
        }
    }

    //控件的点击事件
    @Override
    public void onClick(View v) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDialog simpleDialog = new SimpleDialog(TaskCreationActivity.this);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        switch (v.getId()) {
            case R.id.edtTx_add_task_department_deadline:
                new DatePickerDialog(TaskCreationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtTxDepartmentDeadline.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, month, day).show();
                break;

            case R.id.edtTx_add_task_teacher_deadline:
                new DatePickerDialog(TaskCreationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtTxTeacherDeadline.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, month, day).show();
                break;

            case R.id.edtTx_add_task_name:
                simpleDialog.items(new String[]{"计算机（实验班）", "计算机（卓越班）", "计算机专业",
                        "软件工程专业", "数学类（实验班）", "数学类", "网络工程专业", "信息安全专业"}, 0)
                        .title("选择专业")
                        .positiveAction("确认")
                        .negativeAction("取消")
                        .show();
                simpleDialog.positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxTaskName.setText(simpleDialog.getSelectedValue());
                        simpleDialog.cancel();
                    }
                });
                simpleDialog.negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        simpleDialog.cancel();
                    }
                });
                break;

            case R.id.btn_add_task_import_file:
                startActivity(new Intent(TaskCreationActivity.this, FileSelectActivity.class));
                break;

            default:
                break;
        }
    }

    //任务名映射
    public String taskNameChineseMapEnglisg(String string) {
        Log.d("TAG", string);
        switch (string) {
            case "计算机（卓越班）":
                return "tc_com_exc";
            case "计算机专业":
                return "tc_com_nor";
            case "计算机（实验班）":
                return "tc_com_ope";
            case "信息安全专业":
                return "tc_inf_sec";
            case "数学类":
                return "tc_math_nor";
            case "数学类（实验班）":
                return "tc_math_ope";
            case "网络工程专业":
                return "tc_net_pro";
            case "软件工程专业":
                return "tc_soft_pro";
            default:
                return null;
        }
    }
}
