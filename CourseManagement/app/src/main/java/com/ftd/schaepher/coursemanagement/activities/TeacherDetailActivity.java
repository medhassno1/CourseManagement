package com.ftd.schaepher.coursemanagement.activities;

import android.app.ProgressDialog;
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
import com.ftd.schaepher.coursemanagement.tools.TransferUtils;

import java.util.ArrayList;
import java.util.List;

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
    private EditText edtTxEmail;

    private String userIdentity;
    private String queryIdentity;

    private CourseDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_teacher_detail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("用户信息");
        }

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

        String queryWorkNumber;
        if (isQueryingSelf) {
            queryIdentity = userIdentity;
            queryWorkNumber = sharedPre.getString(ConstantStr.USER_WORK_NUMBER, "");
        } else {
            queryIdentity = intent.getStringExtra("teacherIdentity");
            queryWorkNumber = intent.getStringExtra("teacherID");
            Loger.i("str2", "工号" + queryWorkNumber + "身份" + queryIdentity);
        }

        setUserData(queryIdentity, queryWorkNumber);
    }

    /**
     * 将用户的信息显示到界面
     *
     * @param queryIdentity   当前用户的身份
     * @param queryWorkNumber 被查询信息的用户的工号
     */
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
                    edtTxEmail.setText(Information.getEmail());
                    edtTxDepartment.setVisibility(View.GONE);
                }
                break;
            }

            case ConstantStr.ID_DEPARTMENT_HEAD: {
                TableUserDepartmentHead Information =
                        dbHelper.findById(queryWorkNumber, TableUserDepartmentHead.class);
                List<TableManageMajor> manageMajorList =
                        dbHelper.findAllByWhere(TableManageMajor.class,
                                "workNumber = '" + queryWorkNumber + "'");

                String majorText = "";
                for (int i = 0; i < manageMajorList.size(); i++) {
                    String major = TransferUtils.en2Zh(manageMajorList.get(i).getMajor());
                    majorText = majorText + major + "\n";
                }

                if (Information != null) {
                    edtTxTeacherNumber.setText(Information.getWorkNumber());
                    edtTxPassword.setText(Information.getPassword());
                    edtTxTeacherName.setText(Information.getName());
                    edtTxPhoneNumber.setText(Information.getTelephone());
                    edtTxDepartment.setText(Information.getDepartment());
                    edtTxMajor.setVisibility(View.VISIBLE);

                    edtTxMajor.setText(majorText);
                }
                break;
            }

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
                break;
            }
            default:
                break;
        }
    }

    private void initUserPermission() {
        // 任何身份都不允许修改工号
        edtTxTeacherNumber.setEnabled(false);

        // 只有教学办能修改系负责人所在专业和所负责专业
        if (!userIdentity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            edtTxDepartment.setEnabled(false);
            edtTxMajor.setEnabled(false);
        }
    }

    /**
     * 从界面获取数据
     *
     * @return Object对象，根据情况再进行转换
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
    private List<TableManageMajor> getUIManageMajorData() {
        List<TableManageMajor> manageMajorList = new ArrayList<>();
        String majorText = edtTxMajor.getText().toString().trim();
        String workNumber = edtTxTeacherNumber.getText().toString().trim();
        String[] majors = majorText.split("\n");
        for (String major : majors) {
            if (major != null && major.length() != 0) {
                TableManageMajor manageMajor = new TableManageMajor();
                manageMajor.setWorkNumber(workNumber);
                manageMajor.setMajor(major);

                manageMajorList.add(manageMajor);
            }
        }
        return manageMajorList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_detail_activity_actions, menu);
        if (!userIdentity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            menu.removeItem(R.id.action_modify_information);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_modify_information: {
                // 修改自己的信息时，需要同时更新到导航菜单上
                if (queryIdentity.equals(userIdentity)) {
                    String tvName =
                            edtTxTeacherName.getText().toString().trim();

                    SharedPreferences.Editor editor =
                            getSharedPreferences(ConstantStr.USER_INFORMATION,
                                    MODE_PRIVATE).edit();
                    editor.putString(ConstantStr.USER_NAME, tvName);
                    editor.apply();
                }

                final ProgressDialog progress = new ProgressDialog(TeacherDetailActivity.this);
                progress.setMessage("更新中...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setCancelable(false);
                progress.show();

                new Thread() {
                    @Override
                    public void run() {
                        submitToServerAndUpdateLocal(queryIdentity);
                        progress.cancel();
                        setResult(1);
                        finish();
                    }
                }.start();

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * 提交需要更新的用户数据到服务器
     *
     * @param queryIdentity 要更新的用户的身份
     */
    private void submitToServerAndUpdateLocal(String queryIdentity) {
        Object user = getUserData();
        switch (queryIdentity) {

            case ConstantStr.ID_TEACHER:
                try {
                    Loger.i("updateTeacher", "开始发送服务器");
                    String result = NetworkManager.updateUserData(ConstantStr.TABLE_USER_TEACHER,
                            JsonTools.getJsonString(user), null, NetworkManager.UPDATE_USER_DATA);
                    Loger.i("updateTeacher", result);
                    dbHelper.update(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ConstantStr.ID_DEPARTMENT_HEAD:
                List<TableManageMajor> manageMajorList = getUIManageMajorData();
                try {
                    String result = NetworkManager.updateUserData(ConstantStr.TABLE_USER_DEPARTMENT_HEAD,
                            JsonTools.getJsonString(user),
                            JsonTools.getJsonString(manageMajorList), NetworkManager.UPDATE_USER_DATA);
                    Loger.i("updateTeacher", result);
                    dbHelper.update(user);
                    dbHelper.updateAll(manageMajorList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ConstantStr.ID_TEACHING_OFFICE:
                try {
                    String result = NetworkManager.updateUserData(ConstantStr.TABLE_USER_TEACHING_OFFICE,
                            JsonTools.getJsonString(user), null, NetworkManager.UPDATE_USER_DATA);
                    Loger.i("updateTeacher", result);
                    dbHelper.update(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

}
