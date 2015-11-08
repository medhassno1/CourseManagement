package com.ftd.schaepher.coursemanagement.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ftd.schaepher.coursemanagement.R;

import java.util.Calendar;

/**
 * Created by sxq on 2015/10/31.
 * 发布任务界面
 */
public class TaskCreationActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private EditText edtTxDepartmentDeadline;
    private EditText edtTxTeacherDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_add_task);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("发布报课任务");

        edtTxDepartmentDeadline = (EditText) findViewById(R.id.edtTx_add_task_department_deadline);
        edtTxTeacherDeadline = (EditText) findViewById(R.id.edtTx_add_task_teacher_deadline);
        edtTxTeacherDeadline.setOnFocusChangeListener(this);
        edtTxDepartmentDeadline.setOnFocusChangeListener(this);
        edtTxDepartmentDeadline.setOnClickListener(this);
        edtTxTeacherDeadline.setOnClickListener(this);
        edtTxDepartmentDeadline.setInputType(InputType.TYPE_NULL);
        edtTxTeacherDeadline.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_creation_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

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
            default:
                break;
        }
    }

    //弹出时间选择器供选择
    @Override
    public void onClick(View v) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        switch (v.getId()) {
            case R.id.edtTx_add_task_department_deadline:
                new DatePickerDialog(TaskCreationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtTxDepartmentDeadline.setText(String.format("%d-%d-%d", year, monthOfYear, dayOfMonth));
                    }
                }, year, month, day).show();
                break;

            case R.id.edtTx_add_task_teacher_deadline:
                new DatePickerDialog(TaskCreationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtTxTeacherDeadline.setText(String.format("%d-%d-%d", year, monthOfYear, dayOfMonth));
                    }
                }, year, month, day).show();
                break;

            default:
                break;
        }
    }
}
