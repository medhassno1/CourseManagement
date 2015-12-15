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
import com.ftd.schaepher.coursemanagement.pojo.TableManageMajor;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;
import com.ftd.schaepher.coursemanagement.tools.ConstantStr;
import com.ftd.schaepher.coursemanagement.tools.JsonTools;
import com.ftd.schaepher.coursemanagement.tools.Loger;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;

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
    private EditText edtTxEmail;
    private ActionBar mActionBar;
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
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("用户信息");
        dbHelper = new CourseDBHelper(TeacherDetailActivity.this);

        edtTxTeacherNumber = (EditText) findViewById(R.id.edtTx_teacher_detail_workNumber);
        edtTxPassword = (EditText) findViewById(R.id.edtTx_teacher_detail_password);
        edtTxTeacherName = (EditText) findViewById(R.id.edtTx_teacher_detail_name);
        edtTxPhoneNumber = (EditText) findViewById(R.id.edtTx_teacher_detail_phone_number);
        edtTxDepartment = (EditText) findViewById(R.id.edtTx_teacher_detail_department);
        edtTxMajor = (EditText) findViewById(R.id.edtTx_teacher_detail_major);
        edtTxEmail = (EditText) findViewById(R.id.edtTx_teacher_detail_email);

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
            queryWorkNumber = sharedPre.getString(ConstantStr.USER_WORK_NUMBER, "");
        } else {
            queryIdentity = intent.getStringExtra("teacherIdentity");
            queryWorkNumber = intent.getStringExtra("teacherID");
            Loger.i("str2", "工号" + queryWorkNumber + "身份" + queryIdentity);
        }

        queryIdentity = intent.getStringExtra("teacherIdentity");
        queryWorkNumber = intent.getStringExtra("teacherID");

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
                office.setEmail(edtTxEmail.getText().toString().trim());
                return office;
            }
            case ConstantStr.ID_DEPARTMENT_HEAD: {
                TableUserDepartmentHead department = new TableUserDepartmentHead();
                department.setWorkNumber(edtTxTeacherNumber.getText().toString().trim());
                department.setPassword(edtTxPassword.getText().toString().trim());
                department.setName(edtTxTeacherName.getText().toString().trim());
                department.setDepartment(edtTxDepartment.getText().toString().trim());
                department.setTelephone(edtTxPhoneNumber.getText().toString().trim());
                department.setEmail(edtTxEmail.getText().toString().trim());
                return department;
            }
            case ConstantStr.ID_TEACHER: {
                TableUserTeacher teacher = new TableUserTeacher();
                teacher.setWorkNumber(edtTxTeacherNumber.getText().toString().trim());
                teacher.setPassword(edtTxPassword.getText().toString().trim());
                teacher.setName(edtTxTeacherName.getText().toString().trim());
                teacher.setDepartment(edtTxDepartment.getText().toString().trim());
                teacher.setTelephone(edtTxPhoneNumber.getText().toString().trim());
                teacher.setEmail(edtTxEmail.getText().toString().trim());
                return teacher;
            }
            default:
                return null;
        }
    }

    /**
     * 从界面获取系负责人专业表数据
     */
    private TableManageMajor getManageMajorData() {
        TableManageMajor manageMajor = new TableManageMajor();
        manageMajor.setWorkNumber(edtTxTeacherNumber.getText().toString().trim());
        manageMajor.setMajor(edtTxMajor.getText().toString().trim());
        return manageMajor;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_detail_activity_actions, menu);
        if (!userIdentity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            menu.removeItem(R.id.action_modify_infomation);
        }
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
                                            editor.putString(ConstantStr.USER_NAME, tvName);
                                            editor.apply();
                                        }

                                        submitToServer();
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

    /**
     * 提交数据到服务器
     */
    private void submitToServer() {
        new Thread() {
            @Override
            public void run() {
                Object user = getUserData();
                if (queryIdentity.equals(ConstantStr.ID_TEACHER)) {
                    try {
                        Loger.i("updateteacher", "开始发送服务器");
                        NetworkManager.postToServerSync(ConstantStr.TABLE_USER_TEACHER,
                                JsonTools.getJsonString(user), NetworkManager.UPDATE_USER_TEACHER);
                        Loger.i("updateteacher", "发送服务器结束，开始插入本地数据库");
                        dbHelper.update(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (queryIdentity.equals(ConstantStr.ID_DEPARTMENT_HEAD)) {
                    TableManageMajor manageMajor = getManageMajorData();
                    try {
                        //系负责人表
                        NetworkManager.postToServerSync(ConstantStr.TABLE_USER_DEPARTMENT_HEAD,
                                JsonTools.getJsonString(user), NetworkManager.UPDATE_USER_DEPARTMENT);
                        dbHelper.update(user);
                        //系负责人专业表
                        NetworkManager.postToServerSync(ConstantStr.TABLE_MANAGE_MAJOR,
                                JsonTools.getJsonString(manageMajor), NetworkManager.UPDATE_MANAGER_MAJOR);
                        dbHelper.update(manageMajor);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //教学办
                } else {
                    try {
                        NetworkManager.postToServerSync(ConstantStr.TABLE_USER_TEACHING_OFFICE,
                                JsonTools.getJsonString(user), NetworkManager.UPDATE_USER_OFFICE);
                        dbHelper.update(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
