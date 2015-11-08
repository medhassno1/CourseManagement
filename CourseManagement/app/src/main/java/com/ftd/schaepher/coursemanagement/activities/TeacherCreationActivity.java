package com.ftd.schaepher.coursemanagement.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ftd.schaepher.coursemanagement.R;
import com.rey.material.app.SimpleDialog;

/**
 * Created by sxq on 2015/11/2.
 * 教师添加界面
 */
public class TeacherCreationActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
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

        edtTxDepartment = (EditText) findViewById(R.id.edtTx_teacher_creation_department);
        edtTxMajor = (EditText) findViewById(R.id.edtTx_teacher_creation_major);
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
        return super.onOptionsItemSelected(item);
    }

    //设置弹出系和专业可供选择
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
}
