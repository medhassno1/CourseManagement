package com.ftd.schaepher.coursemanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;

/**
 * Created by sxq on 2015/11/2.
 * 教师信息界面
 */
public class TeacherDetailActivity extends AppCompatActivity {

    private EditText edtTxTeacherNumber;
    private EditText edtTxPassword;
    private EditText edtTxTeacherName;
    private EditText edtTxPhoneNumber;
    private EditText edtTxDepartment;
    private EditText edtTxMajor;

    private String workNumber;//教师工号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_detail);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("用户信息");

        edtTxTeacherNumber = (EditText)findViewById(R.id.edtTx_teacher_detail_username);
        edtTxPassword = (EditText)findViewById(R.id.edtTx_teacher_detail_password);
        edtTxTeacherName = (EditText)findViewById(R.id.edtTx_teacher_detail_name);
        edtTxPhoneNumber = (EditText)findViewById(R.id.edtTx_teacher_detail_phone_number);
        edtTxDepartment = (EditText)findViewById(R.id.edtTx_teacher_detail_department);
        edtTxMajor = (EditText)findViewById(R.id.edtTx_teacher_detail_major);

        initTeacherData();
    }

    /**
     * 初始化界面数据
     */
    private void initTeacherData(){
        Intent intent = getIntent();
        workNumber = intent.getStringExtra("teacherID");

        Log.i("str2", "工号" + workNumber);

        CourseDBHelper dbHelper = new CourseDBHelper();
        dbHelper.creatDataBase(this);
        TableUserTeacher teacher=(TableUserTeacher)dbHelper.findById(workNumber,TableUserTeacher.class);

        edtTxTeacherNumber.setText(teacher.getWorkNumber());
        edtTxPassword.setText(teacher.getPassword());
        edtTxTeacherName.setText(teacher.getName());
        edtTxPhoneNumber.setText(teacher.getTelephone());
        edtTxDepartment.setText(teacher.getDepartment());

    }

    /**
     * 获得界面数据
     */
    private TableUserTeacher getTeacherData(){
        TableUserTeacher teacher = new TableUserTeacher();
        teacher.setWorkNumber(edtTxTeacherNumber.getText().toString().trim());
        teacher.setPassword(edtTxPassword.getText().toString().trim());
        teacher.setName(edtTxTeacherName.getText().toString().trim());
        teacher.setDepartment(edtTxDepartment.getText().toString().trim());
        teacher.setTelephone(edtTxPhoneNumber.getText().toString().trim());

        return teacher;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_detail_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_modify_infomation:
                new AlertDialog.Builder(this).setTitle("提示").setMessage("是否确认修改").setPositiveButton
                        (android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CourseDBHelper dbHelper = new CourseDBHelper();
                                dbHelper.creatDataBase(TeacherDetailActivity.this);
                                TableUserTeacher teacher = getTeacherData();
                                dbHelper.update(teacher);

                                finish();
                            }
                        }).setNegativeButton
                        (android.R.string.cancel, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
