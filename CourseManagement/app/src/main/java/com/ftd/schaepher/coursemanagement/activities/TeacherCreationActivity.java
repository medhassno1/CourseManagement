package com.ftd.schaepher.coursemanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import com.rey.material.app.SimpleDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxq on 2015/11/2.
 * 教师添加界面
 */
public class TeacherCreationActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private EditText edtTxTeacherNumber;
    private EditText edtTxPassword;
    private EditText edtTxTeacherName;
    private EditText edtTxPhoneNumber;
    private EditText edtTxDepartment;
    private EditText edtTxEmail;
    private EditText edtTxMajor;
    private RadioGroup rdoGroup;
    private String selectedIdentity;
    private RadioButton rdoBtnTeacher;
    private RadioButton rdoBtnDepartment;
    private RadioButton rdoBtnOffice;
    private String identity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_creation);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_creation);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("添加用户");

        identity = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE)
                .getString(ConstantStr.USER_IDENTITY, null);

        initView();

        if(identity.equals(ConstantStr.ID_DEPARTMENT_HEAD)){
            rdoGroup.setVisibility(View.GONE);
            edtTxDepartment.setVisibility(View.VISIBLE);
            edtTxMajor.setVisibility(View.GONE);
            selectedIdentity = ConstantStr.ID_TEACHER;
        }else{
            rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rdoBtn = (RadioButton) findViewById(checkedId);
                    if (rdoBtn == rdoBtnOffice) {
                        edtTxDepartment.setVisibility(View.GONE);
                        edtTxMajor.setVisibility(View.GONE);
                        selectedIdentity = ConstantStr.ID_TEACHING_OFFICE;
                    } else if (rdoBtn == rdoBtnDepartment) {
                        edtTxDepartment.setVisibility(View.VISIBLE);
                        edtTxMajor.setVisibility(View.VISIBLE);
                        selectedIdentity = ConstantStr.ID_DEPARTMENT_HEAD;
                    } else {
                        edtTxDepartment.setVisibility(View.VISIBLE);
                        edtTxMajor.setVisibility(View.GONE);
                        selectedIdentity = ConstantStr.ID_TEACHER;
                    }
                }
            });
            rdoBtnTeacher.setChecked(true);
        }


    }

    private void initView() {
        edtTxTeacherNumber = (EditText) findViewById(R.id.edtTx_teacher_creation_workNumber);
        edtTxPassword = (EditText) findViewById(R.id.edtTx_teacher_creation_password);
        edtTxTeacherName = (EditText) findViewById(R.id.edtTx_teacher_creation_name);
        edtTxPhoneNumber = (EditText) findViewById(R.id.edtTx_teacher_creation_phone_number);
        edtTxDepartment = (EditText) findViewById(R.id.edtTx_teacher_creation_department);
        edtTxMajor = (EditText) findViewById(R.id.edtTx_teacher_creation_major);
        edtTxEmail = (EditText) findViewById(R.id.edtTx_teacher_creation_email);
        rdoGroup = (RadioGroup) findViewById(R.id.rdoGroup_create_identity);
        rdoBtnTeacher = (RadioButton) findViewById(R.id.rdoBtn_create_teacher);
        rdoBtnDepartment = (RadioButton) findViewById(R.id.rdoBtn_create_department_head);
        rdoBtnOffice = (RadioButton) findViewById(R.id.rdoBtn_create_teaching_office);

        edtTxTeacherNumber.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED); // 工号输入框的格式
        edtTxDepartment.setInputType(InputType.TYPE_NULL);
        edtTxMajor.setInputType(InputType.TYPE_NULL);
        edtTxDepartment.setOnClickListener(this);
        edtTxMajor.setOnClickListener(this);
        edtTxDepartment.setOnFocusChangeListener(this);
        edtTxMajor.setOnFocusChangeListener(this);
        edtTxMajor.setSingleLine(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_creation_activity_actions, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_confim_add_teacher:
                if (isAllWrite()) {
                    new AlertDialog
                            .Builder(this)
                            .setTitle("提示")
                            .setMessage("是否确认添加")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
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
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 设置弹出系和专业可供选择
    @Override
    public void onClick(View v) {
        final SimpleDialog simpleDialog = new SimpleDialog(TeacherCreationActivity.this);
        switch (v.getId()) {
            case R.id.edtTx_teacher_creation_department:
                simpleDialog.items(new String[]{"计算机系", "软件工程系", "数学系", "网络工程系", "信息安全系"}, 0)
                        .title("所属系")
                        .positiveAction("确认")
                        .negativeAction("取消")
                        .show();
                simpleDialog.positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxDepartment.setText(simpleDialog.getSelectedValue());
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

            case R.id.edtTx_teacher_creation_major:
//                这里尝试取消默认选择第一个
                simpleDialog.multiChoiceItems(new String[]
                        {"计算机（实验班）", "计算机（卓越班）", "计算机专业", "软件工程专业",
                                "数学类（实验班）", "数学类", "网络工程专业", "信息安全专业"}, 0)
                        .title("所负责专业")
                        .positiveAction("确认")
                        .negativeAction("取消")
                        .show();
                simpleDialog.positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence[] values = simpleDialog.getSelectedValues();
                        if (values != null) {
                            StringBuffer stringBuffer = new StringBuffer();
                            for (int i = 0; i < values.length; i++) {
                                stringBuffer.append(values[i]).append(i == values.length - 1 ? "" : "\n");
                            }
                            edtTxMajor.setText(stringBuffer);
                        } else {
                            edtTxMajor.setText("");
                        }
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

            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edtTx_teacher_creation_department:
                if (hasFocus) {
                    onClick(v);
                }
                break;
            case R.id.edtTx_teacher_creation_major:
                if (hasFocus) {
                    onClick(v);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获得界面数据-教师
     */
    private TableUserTeacher getUITeacherData() {
        TableUserTeacher teacher = new TableUserTeacher();
        String workNumber = edtTxTeacherNumber.getText().toString().trim();
        teacher.setWorkNumber(workNumber);
        String password = edtTxPassword.getText().toString().trim();
        if (password.isEmpty()) {
            password = workNumber;
        }
        teacher.setPassword(password);
        teacher.setName(edtTxTeacherName.getText().toString().trim());
        teacher.setDepartment(edtTxDepartment.getText().toString().trim());
        teacher.setTelephone(edtTxPhoneNumber.getText().toString().trim());
        teacher.setEmail(edtTxEmail.getText().toString().trim());
        return teacher;
    }

    /**
     * 获得界面数据-系负责人
     */
    private TableUserDepartmentHead getUIDepartmentHeadData() {
        TableUserDepartmentHead departmentHead = new TableUserDepartmentHead();
        String workNumber = edtTxTeacherNumber.getText().toString().trim();
        departmentHead.setWorkNumber(workNumber);
        String password = edtTxPassword.getText().toString().trim();
        if (password.isEmpty()) {
            password = workNumber;
        }
        departmentHead.setPassword(password);
        departmentHead.setName(edtTxTeacherName.getText().toString().trim());
        departmentHead.setDepartment(edtTxDepartment.getText().toString().trim());
        departmentHead.setTelephone(edtTxPhoneNumber.getText().toString().trim());
        departmentHead.setEmail(edtTxEmail.getText().toString().trim());
        return departmentHead;
    }

    /**
     * 获得界面数据-系负责人及其负责专业
     */
    private List<TableManageMajor> getUIManageMajorData() {
        List<TableManageMajor> manageMajorList = new ArrayList<>();
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

    /**
     * 获得界面数据-教学办
     */
    private TableUserTeachingOffice getUITeachingOfficeData() {
        TableUserTeachingOffice teachingOffice = new TableUserTeachingOffice();
        String workNumber = edtTxTeacherNumber.getText().toString().trim();
        teachingOffice.setWorkNumber(workNumber);
        String password = edtTxPassword.getText().toString().trim();
        if (password.isEmpty()) {
            password = workNumber;
        }
        teachingOffice.setPassword(password);
        teachingOffice.setName(edtTxTeacherName.getText().toString().trim());
        teachingOffice.setTelephone(edtTxPhoneNumber.getText().toString().trim());
        teachingOffice.setEmail(edtTxEmail.getText().toString().trim());
        return teachingOffice;
    }

    private boolean isAllWrite() {
        boolean isAllOk = true;

        if (edtTxTeacherNumber.getText().toString().trim().equals("")) {
            edtTxTeacherNumber.setError("工号不能为空");
            isAllOk = false;
        }

//        if (edtTxPassword.getText().toString().trim().equals("")) {
//            edtTxPassword.setError("密码不能为空");
//            isAllOk = false;
//        }

        if (edtTxTeacherName.getText().toString().trim().equals("")) {
            edtTxTeacherName.setError("姓名不能为空");
            isAllOk = false;
        }
        return isAllOk;
    }

    //提交数据到服务器
    private void submitToServer() {
        new Thread() {
            @Override
            public void run() {
                CourseDBHelper dbHelper = new CourseDBHelper(TeacherCreationActivity.this);
                if (selectedIdentity.equals(ConstantStr.ID_TEACHER)) {
                    TableUserTeacher teacher = getUITeacherData();
                    try {
                        Loger.i("createTeacher", "开始发送服务器");
                        NetworkManager.postToServerSync(ConstantStr.TABLE_USER_TEACHER,
                                JsonTools.getJsonString(teacher), NetworkManager.INSERT_TABLE);
                        Loger.i("createTeacher", "发送服务器结束，开始插入本地数据库");
                        dbHelper.insert(teacher);
                        Loger.i("createTeacher", "插入本地数据库结束");
                    } catch (Exception e) {
                        Toast.makeText(TeacherCreationActivity.this,
                                "该工号已存在，请删除后再尝试", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    //系负责人
                } else if (selectedIdentity.equals(ConstantStr.ID_DEPARTMENT_HEAD)) {
                    TableUserDepartmentHead departmentHead = getUIDepartmentHeadData();
                    List<TableManageMajor> manageMajorList = getUIManageMajorData();
                    try {
                        //系负责人表
                        NetworkManager.postToServerSync(ConstantStr.TABLE_USER_DEPARTMENT_HEAD,
                                JsonTools.getJsonString(departmentHead), NetworkManager.INSERT_TABLE);
                        dbHelper.insert(departmentHead);
                        //系负责人专业表
                        NetworkManager.postToServerSync(ConstantStr.TABLE_MANAGE_MAJOR,
                                JsonTools.getJsonString(manageMajorList), NetworkManager.INSERT_TABLE);
                        dbHelper.insertAll(manageMajorList);
                    } catch (Exception e) {
                        Toast.makeText(TeacherCreationActivity.this,
                                "该工号已存在，请删除后再尝试", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    //教学办
                } else {
                    TableUserTeachingOffice teachingOffice = getUITeachingOfficeData();
                    Loger.w("TeacherCreationActivity", teachingOffice.toString());
                    try {
                        NetworkManager.postToServerSync(ConstantStr.TABLE_USER_TEACHING_OFFICE,
                                JsonTools.getJsonString(teachingOffice), NetworkManager.INSERT_TABLE);
                        dbHelper.insert(teachingOffice);
                    } catch (Exception e) {
                        Toast.makeText(TeacherCreationActivity.this,
                                "该工号已存在，请删除后再尝试", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }
        }.start();
        //教师
    }
}
