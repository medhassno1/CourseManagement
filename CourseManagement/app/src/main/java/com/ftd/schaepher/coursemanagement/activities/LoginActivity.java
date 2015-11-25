package com.ftd.schaepher.coursemanagement.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.Initialize;
import com.ftd.schaepher.coursemanagement.tools.ConstantTools;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;

import java.io.IOException;

/**
 * Created by sxq on 2015/10/28.
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "LoginActivity";

    private Button btnLogin;
    private EditText edtTxUserName;
    private EditText edtTxPassWord;
    private RadioGroup rdoGroup;
    private TextInputLayout layoutUserName;
    private TextInputLayout layoutPassWord;
    private ProgressDialog progress;

    private String userName;
    private String password;
    private String identity;

    private SharedPreferences.Editor ownInformationSaveEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtTxUserName = (EditText) findViewById(R.id.edtTx_login_username);
        edtTxPassWord = (EditText) findViewById(R.id.edtTx_login_password);
        rdoGroup = (RadioGroup) findViewById(R.id.rdoGroup_check_identity);
        btnLogin = (Button) findViewById(R.id.btn_login);
        layoutUserName = (TextInputLayout) findViewById(R.id.inputLayout_login_username);
        layoutPassWord = (TextInputLayout) findViewById(R.id.inputLayout_login_password);

        ownInformationSaveEditor = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).edit();

        edtTxUserName.setOnFocusChangeListener(this);
        edtTxPassWord.setOnFocusChangeListener(this);
        btnLogin.setOnClickListener(this);

        autoSetUserName();
//        initDatabaseData();
    }


    /**
     * 自动输入保存的用户名
     */
    private void autoSetUserName() {
        userName = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getString(ConstantTools.USER_ACCOUNT, "");
        if (!userName.equals("")) {
            edtTxUserName.setText(userName);
        }
    }

    /**
     * 第一次登陆的操作，即初始化数据库
     */
    private void initDatabaseData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun) {
            Log.v("初始化数据库", "正在初始化");
            editor.putBoolean("isFirstRun", false);
            editor.apply();

            Initialize initialize = new Initialize(); // 初始化数据库
            initialize.init(this);
            Log.v("初始化数据库", "初始化完成");
        } else {
            Log.v("初始化数据库", "已初始化过");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userName = edtTxUserName.getText().toString().trim();
                password = edtTxPassWord.getText().toString().trim();
                switch (rdoGroup.getCheckedRadioButtonId()) {
                    case R.id.rdoBtn_teacher:
                        identity = ConstantTools.ID_TEACHER;
                        break;
                    case R.id.rdoBtn_department_head:
                        identity = ConstantTools.ID_DEPARTMENT_HEAD;
                        break;
                    case R.id.rdoBtn_teaching_office:
                        identity = ConstantTools.ID_TEACHING_OFFICE;
                        break;
                    default:
                        break;
                }

                if (isTrueForm()) {
                    progress = new ProgressDialog(LoginActivity.this);
                    progress.setMessage("登录中...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setCancelable(true);
                    progress.show();
                    login();
//                    loginOffLine();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 检查账号密码
     */
    private boolean isTrueForm() {
        if (userName.equals("") || password.equals("")) {
            if (userName.equals("")) {
                layoutUserName.setError(getString(R.string.nullUserName));
            }
            if (password.equals("")) {
                layoutPassWord.setError(getString(R.string.nullPassWord));
            }
        } else {
            layoutUserName.setError(null);
            layoutPassWord.setError(null);
            return true;
        }
        return false;
    }

    public void login() {
        NetworkManager manager = new NetworkManager();
        try {

            String result = manager.login(userName,password,identity);


            if (result.equals("true")) {
                ownInformationSaveEditor.putString(ConstantTools.USER_IDENTITY, identity);//保存用户名、身份
                ownInformationSaveEditor.putString(ConstantTools.USER_ACCOUNT, userName);
                ownInformationSaveEditor.apply();
                
                Intent intend = new Intent();
                intend.setClass(LoginActivity.this, TaskListActivity.class);
                LoginActivity.this.finish();
                startActivity(intend);
            } else if (result.equals("false")){
                progress.cancel();
                Toast.makeText(LoginActivity.this, "账号或密码错误",
                        Toast.LENGTH_SHORT).show();
            } else {
                progress.cancel();
                Toast.makeText(LoginActivity.this, "请求服务器失败",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginOffLine() {
        ownInformationSaveEditor.putString(ConstantTools.USER_IDENTITY, identity);//保存用户名、身份
        ownInformationSaveEditor.putString(ConstantTools.USER_ACCOUNT, userName);
        ownInformationSaveEditor.apply();

        Intent intend = new Intent();
        intend.setClass(LoginActivity.this, TaskListActivity.class);
        LoginActivity.this.finish();
        startActivity(intend);
    }

    // 处理输入错误提示
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!edtTxUserName.getText().toString().equals("")) {
            layoutUserName.setError(null);
        }

        if (!edtTxPassWord.getText().toString().equals("")) {
            layoutPassWord.setError(null);
        }
    }
}
