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
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.rey.material.app.SimpleDialog;

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
    private EditText edtTxMajor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_creation);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_creation);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("添加用户");

//        控件初始化最好封装到一个方法里面
        edtTxTeacherNumber = (EditText) findViewById(R.id.edtTx_teacher_creation_workNumber);
        edtTxPassword = (EditText) findViewById(R.id.edtTx_teacher_creation_password);
        edtTxTeacherName = (EditText) findViewById(R.id.edtTx_teacher_creation_name);
        edtTxPhoneNumber = (EditText) findViewById(R.id.edtTx_teacher_creation_phone_number);
        edtTxDepartment = (EditText) findViewById(R.id.edtTx_teacher_creation_department);
        edtTxMajor = (EditText) findViewById(R.id.edtTx_teacher_creation_major);

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
               if(isAllWrite()){
                    new AlertDialog.Builder(this).setTitle("提示").setMessage("是否确认添加").
                            setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CourseDBHelper dbHelper = new CourseDBHelper(TeacherCreationActivity.this);
//                                    这里判断要添加哪种身份
                                    TableUserTeacher teacher = getTeacherData();
                                    try {
//                                        这里添加网络请求
                                        dbHelper.insert(teacher);
                                    } catch (Exception e) {
                                        Toast.makeText(TeacherCreationActivity.this,
                                                "该工号已存在，请删除后再尝试", Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                }
                            }).setNegativeButton
                            (android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
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
//                            这里要调整，尝试通过序号来判断选了哪些。否则无法插入数据库
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

    private boolean isAllWrite(){
        if(edtTxTeacherNumber.getText().toString().trim().equals("")){
            edtTxTeacherNumber.setError("工号不能为空");
        }else if(edtTxPassword.getText().toString().trim().equals("")){
            edtTxPassword.setError("密码不能为空");
        }else if(edtTxTeacherName.getText().toString().trim().equals("")){
            edtTxTeacherName.setError("姓名不能为空");
        }else {
            return true;
        }
        return  false;
    }
}
