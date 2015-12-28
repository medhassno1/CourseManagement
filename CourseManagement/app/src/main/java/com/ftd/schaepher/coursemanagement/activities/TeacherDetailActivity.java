package com.ftd.schaepher.coursemanagement.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import java.util.ArrayList;
import java.util.List;

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
                    edtTxEmail.setText(Information.getEmail());
                    edtTxDepartment.setVisibility(View.GONE);
                }
            }
            break;
            case ConstantStr.ID_DEPARTMENT_HEAD: {
                TableUserDepartmentHead Information =
                        dbHelper.findById(queryWorkNumber, TableUserDepartmentHead.class);
                List<TableManageMajor> manageMajorList =
                        dbHelper.findAllByWhere(TableManageMajor.class, "workNumber = '" + queryWorkNumber + "'");
                String majorText = "";
                for (int i = 0; i < manageMajorList.size(); i++) {
                    String major = transferMajorNameToChinese(manageMajorList.get(i).getMajor());
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
        // 都不允许修改工号
        edtTxTeacherNumber.setEnabled(false);
        if (!userIdentity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            edtTxDepartment.setEnabled(false);
            edtTxMajor.setEnabled(false);
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
    private List<TableManageMajor> getUIManageMajorData() {
        List manageMajorList = new ArrayList();
        String majorText = edtTxMajor.getText().toString().trim();
        String workNumber = edtTxTeacherNumber.getText().toString().trim();
        String[] majors = majorText.split("\n");
        for (int i = 0; i < majors.length; i++) {
            if (majors[i] != null && majors[i].length() != 0) {
                TableManageMajor manageMajor = new TableManageMajor();
                manageMajor.setWorkNumber(workNumber);
                manageMajor.setMajor(majors[i]);

                manageMajorList.add(manageMajor);
            }
        }
        return manageMajorList;
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

                                        ProgressDialog progress = new ProgressDialog(TeacherDetailActivity.this);
                                        progress.setMessage("更新中...");
                                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progress.setCancelable(false);
                                        progress.show();

                                        submitToServer();

                                        progress.cancel();

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
                        Loger.i("updateTeacher", "开始发送服务器");
                        String result = NetworkManager.updateUserData(ConstantStr.TABLE_USER_TEACHER,
                                JsonTools.getJsonString(user), null, NetworkManager.UPDATE_USER_DATA);
                        Loger.i("updateTeacher", result);
                        dbHelper.update(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (queryIdentity.equals(ConstantStr.ID_DEPARTMENT_HEAD)) {
                    List<TableManageMajor> manageMajorList = getUIManageMajorData();
                    try {
                        // 系负责人表
                        String result = NetworkManager.updateUserData(ConstantStr.TABLE_USER_DEPARTMENT_HEAD,
                                JsonTools.getJsonString(user),
                                JsonTools.getJsonString(manageMajorList), NetworkManager.UPDATE_USER_DATA);
                        Loger.i("updateTeacher", result);
                        dbHelper.update(user);
                        dbHelper.updateAll(manageMajorList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (queryIdentity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
                    try {
                        // 教学办
                        String result = NetworkManager.updateUserData(ConstantStr.TABLE_USER_TEACHING_OFFICE,
                                JsonTools.getJsonString(user), null, NetworkManager.UPDATE_USER_DATA);
                        Loger.i("updateTeacher", result);
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

    // 专业名映射为中文
    public String transferMajorNameToChinese(String string) {
        switch (string) {
            case "tc_com_exc":
                return "计算机（卓越班）";
            case "tc_com_nor":
                return "计算机专业";
            case "tc_com_ope":
                return "计算机（实验班）";
            case "tc_inf_sec":
                return "信息安全专业";
            case "tc_math_nor":
                return "数学类";
            case "tc_math_ope":
                return "数学类（实验班）";
            case "tc_net_pro":
                return "网络工程专业";
            case "tc_soft_pro":
                return "软件工程专业";
            default:
                return string;
        }
    }

}
