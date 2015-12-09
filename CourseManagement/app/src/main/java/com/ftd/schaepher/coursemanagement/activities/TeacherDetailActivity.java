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

    private String workNumber;
    private String identity;

    private CourseDBHelper dbHelper;

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
     * 初始化界面数据
     */
    private void initTeacherData() {
        Intent intent = getIntent();

        SharedPreferences sharedPre = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE);
        identity = sharedPre.getString(ConstantStr.USER_IDENTITY, "");
        if (intent.getBooleanExtra("isQueryOwnInfomation", true)) {
            workNumber = sharedPre.getString(ConstantStr.USER_WORKNUMBER, "");
            switch (identity) {
                case ConstantStr.ID_TEACHER:
                    TableUserTeacher teacher =
                            dbHelper.findById(workNumber, TableUserTeacher.class);
                    if (teacher != null) {
                        edtTxTeacherNumber.setText(teacher.getWorkNumber());
                        edtTxPassword.setText(teacher.getPassword());
                        edtTxTeacherName.setText(teacher.getName());
                        edtTxPhoneNumber.setText(teacher.getTelephone());
                        edtTxDepartment.setText(teacher.getDepartment());
                    }
                    break;

                case ConstantStr.ID_DEPARTMENT_HEAD:
                    TableUserDepartmentHead departmentHead =
                            dbHelper.findById(workNumber, TableUserDepartmentHead.class);
                    if (departmentHead != null) {
                        edtTxTeacherNumber.setText(departmentHead.getWorkNumber());
                        edtTxPassword.setText(departmentHead.getPassword());
                        edtTxTeacherName.setText(departmentHead.getName());
                        edtTxPhoneNumber.setText(departmentHead.getTelephone());
                        edtTxDepartment.setText(departmentHead.getDepartment());
                        edtTxMajor.setVisibility(View.VISIBLE);
//                        edtTxMajor.setText(departmentHead.getManagedMajor());
                    }
                    break;

                case ConstantStr.ID_TEACHING_OFFICE:
                    TableUserTeachingOffice office =
                            dbHelper.findById(workNumber, TableUserTeachingOffice.class);
                    if (office != null) {
                        edtTxTeacherNumber.setText(office.getWorkNumber());
                        edtTxPassword.setText(office.getPassword());
                        edtTxTeacherName.setText(office.getName());
                        edtTxPhoneNumber.setText(office.getTelephone());
                        edtTxDepartment.setVisibility(View.GONE);
                    }
                    break;

                default:
                    break;
            }
        } else {
            String queryIdentity = intent.getStringExtra("teacherIdentity");
            workNumber = intent.getStringExtra("teacherID");
            Loger.i("str2", "工号" + workNumber + "身份" + queryIdentity);

            switch (queryIdentity) {
                case ConstantStr.ID_TEACHING_OFFICE: {
                    TableUserTeachingOffice Information =
                            dbHelper.findById(workNumber, TableUserTeachingOffice.class);
                    if (Information != null) {
                        edtTxTeacherNumber.setText(Information.getWorkNumber());
                        edtTxPassword.setText(Information.getPassword());
                        edtTxTeacherName.setText(Information.getName());
                        edtTxPhoneNumber.setText(Information.getTelephone());
                    }
                }
                break;
                case ConstantStr.ID_DEPARTMENT_HEAD: {
                    TableUserDepartmentHead Information =
                            dbHelper.findById(workNumber, TableUserDepartmentHead.class);
                    if (Information != null){
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
                            dbHelper.findById(workNumber, TableUserTeacher.class);
                    if (Information != null){
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
    }

    private void initUserPermission() {
        if (!identity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            edtTxDepartment.setEnabled(false);
            edtTxMajor.setEnabled(false);
            edtTxTeacherNumber.setEnabled(false);
        }
    }

    /**
     * 获得界面数据
     */
    private TableUserTeacher getTeacherData() {
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_modify_infomation:
                new AlertDialog.Builder(this).setTitle("提示").setMessage("是否确认修改").setPositiveButton
                        (android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TableUserTeacher teacher = getTeacherData();
                                dbHelper.update(teacher);

                                finish();
                            }
                        }).setNegativeButton
                        (android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
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
