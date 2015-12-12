package com.ftd.schaepher.coursemanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;
import com.ftd.schaepher.coursemanagement.tools.ConstantStr;
import com.ftd.schaepher.coursemanagement.tools.Loger;

/**
 * Created by sxq on 2015/11/2.
 * 教师信息界面
 */
public class TeacherDetailActivity extends AppCompatActivity {
    //  删除教师还没做
    private EditText edtTxTeacherNumber;
    private EditText edtTxPassword;
    private EditText edtTxTeacherName;
    private EditText edtTxPhoneNumber;
    private EditText edtTxDepartment;
    private EditText edtTxMajor;
    private String userIdentity;

    private CourseDBHelper dbHelper;
    private String queryWorkNumber;
    private String queryIdentity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_detail);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("用户信息");
        dbHelper = new CourseDBHelper(TeacherDetailActivity.this);

        edtTxTeacherNumber = (EditText) findViewById(R.id.edtTx_teacher_detail_workNumber);
        edtTxPassword = (EditText) findViewById(R.id.edtTx_teacher_detail_password);
        edtTxTeacherName = (EditText) findViewById(R.id.edtTx_teacher_detail_name);
        edtTxPhoneNumber = (EditText) findViewById(R.id.edtTx_teacher_detail_phone_number);
        edtTxDepartment = (EditText) findViewById(R.id.edtTx_teacher_detail_department);
        edtTxMajor = (EditText) findViewById(R.id.edtTx_teacher_detail_major);

        initTeacherData();
        initUserPermission();
    }

    /**
     * 初始化教师数据
     */
    private void initTeacherData() {
        SharedPreferences sharedPre = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE);
        userIdentity = sharedPre.getString(ConstantStr.USER_IDENTITY, "");

        Intent intent = getIntent();
        boolean isQueryingSelf = intent.getBooleanExtra("isQueryingSelf", true);

        if (isQueryingSelf) {
            queryIdentity = userIdentity;
            queryWorkNumber = sharedPre.getString(ConstantStr.USER_WORKNUMBER, "");
        } else {
            queryIdentity = intent.getStringExtra("teacherIdentity");
            queryWorkNumber = intent.getStringExtra("teacherID");
            Loger.i("str2", "工号" + queryWorkNumber + "身份" + queryIdentity);
        }

        setUserData(queryIdentity, queryWorkNumber);
    }

    private void setUserData(String queryIdentity, String queryWorkNumber) {
        switch (queryIdentity) {
            case ConstantStr.ID_TEACHING_OFFICE: {
                TableUserTeachingOffice Information =
                        dbHelper.findById(queryWorkNumber, TableUserTeachingOffice.class);
                if (Information != null) {
                    edtTxTeacherNumber.setText(Information.getWorkNumber());
                    edtTxPassword.setText(Information.getPassword());
                    edtTxTeacherName.setText(Information.getName());
                    edtTxPhoneNumber.setText(Information.getTelephone());
                    edtTxDepartment.setVisibility(View.GONE);
                }
            }
            break;
            case ConstantStr.ID_DEPARTMENT_HEAD: {
                TableUserDepartmentHead Information =
                        dbHelper.findById(queryWorkNumber, TableUserDepartmentHead.class);
                if (Information != null) {
                    edtTxTeacherNumber.setText(Information.getWorkNumber());
                    edtTxPassword.setText(Information.getPassword());
                    edtTxTeacherName.setText(Information.getName());
                    edtTxPhoneNumber.setText(Information.getTelephone());
                    edtTxDepartment.setText(Information.getDepartment());
                    edtTxMajor.setVisibility(View.VISIBLE);
                }
            }
            break;
            case ConstantStr.ID_TEACHER: {
                TableUserTeacher Information =
                        dbHelper.findById(queryWorkNumber, TableUserTeacher.class);
                if (Information != null) {
                    edtTxTeacherNumber.setText(Information.getWorkNumber());
                    edtTxPassword.setText(Information.getPassword());
                    edtTxTeacherName.setText(Information.getName());
                    edtTxPhoneNumber.setText(Information.getTelephone());
                    edtTxDepartment.setText(Information.getDepartment());
                }
            }
            break;
            default:
                break;
        }
    }

    private void initUserPermission() {
        if (!userIdentity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            edtTxDepartment.setEnabled(false);
            edtTxMajor.setEnabled(false);
            edtTxTeacherNumber.setEnabled(false);
        }
    }

    /**
     * 从界面获取数据
     */
    private Object getUserData() {

        switch (queryIdentity) {
            case ConstantStr.ID_TEACHING_OFFICE: {
                TableUserTeachingOffice office = new TableUserTeachingOffice();
                office.setWorkNumber(edtTxTeacherNumber.getText().toString().trim());
                office.setPassword(edtTxPassword.getText().toString().trim());
                office.setName(edtTxTeacherName.getText().toString().trim());
                office.setTelephone(edtTxPhoneNumber.getText().toString().trim());
                return office;
            }
            case ConstantStr.ID_DEPARTMENT_HEAD: {
                TableUserDepartmentHead department = new TableUserDepartmentHead();
                department.setWorkNumber(edtTxTeacherNumber.getText().toString().trim());
                department.setPassword(edtTxPassword.getText().toString().trim());
                department.setName(edtTxTeacherName.getText().toString().trim());
                department.setDepartment(edtTxDepartment.getText().toString().trim());
                department.setTelephone(edtTxPhoneNumber.getText().toString().trim());
                return department;
            }
            case ConstantStr.ID_TEACHER: {
                TableUserTeacher teacher = new TableUserTeacher();
                teacher.setWorkNumber(edtTxTeacherNumber.getText().toString().trim());
                teacher.setPassword(edtTxPassword.getText().toString().trim());
                teacher.setName(edtTxTeacherName.getText().toString().trim());
                teacher.setDepartment(edtTxDepartment.getText().toString().trim());
                teacher.setTelephone(edtTxPhoneNumber.getText().toString().trim());
                return teacher;
            }
            default:
                return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_detail_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_modify_infomation:
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否确认修改")
                        .setPositiveButton
                                (android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (queryIdentity.equals(userIdentity)) {
                                            String tvName =
                                                    edtTxTeacherName.getText().toString().trim();

                                            SharedPreferences.Editor editor =
                                                    getSharedPreferences(ConstantStr.USER_INFORMATION,
                                                            MODE_PRIVATE).edit();
                                            editor.putString(ConstantStr.USER_NAME,tvName);
                                            editor.apply();
                                        }

                                        Object user = getUserData();

                                        dbHelper.update(user);
//                              这里需要发送更新到服务器
                                        finish();
                                    }
                                })
                        .setNegativeButton
                                (android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
