package com.ftd.schaepher.coursemanagement.activities;

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
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.tools.ConstantTools;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
import com.ftd.schaepher.coursemanagement.tools.ServerTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.ProgressView;

import org.apache.http.Header;

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
    private ProgressView proBarLogin;

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
        proBarLogin = (ProgressView) findViewById(R.id.proBar_login);

        ownInformationSaveEditor = getSharedPreferences("userInformation", MODE_PRIVATE).edit();

        edtTxUserName.setOnFocusChangeListener(this);
        edtTxPassWord.setOnFocusChangeListener(this);
        btnLogin.setOnClickListener(this);

        autoSetUserName();
        initDatabaseData();

    }


    /**
     * 自动输入保存的用户名
     */
    private void autoSetUserName() {
        userName = getSharedPreferences("userInformation", MODE_PRIVATE).getString("userName", "");
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
                    proBarLogin.setVisibility(View.VISIBLE);
                    login();
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

    // 处理登录逻辑
    public void login() {
        RequestParams params = new RequestParams();
        params.add("login-user", userName);
        params.add("login-password", password);
        params.add("ident", identity);
        try {
            NetworkManager.post(NetworkManager.URL_LOGIN, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    String html = new String(response);
                    Log.w("Login收到的数据", html); // 服务器返回的文本
                    if (html.equals("true")) {
                        // 跳转,同时将选择登录的身份信息存储在本地，方便下一个界面根据不同身份做相应修改
                        proBarLogin.setVisibility(View.INVISIBLE);

                        ownInformationSaveEditor.putString("identity", identity);//保存用户名、身份
                        ownInformationSaveEditor.putString("userName", userName);
                        ownInformationSaveEditor.apply();

                        Intent intend = new Intent();
                        intend.setClass(LoginActivity.this, TaskListActivity.class);
                        LoginActivity.this.finish();
                        startActivity(intend);
                    } else {
                        proBarLogin.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "账号或密码错误",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] response, Throwable throwable) {
                    Toast.makeText(LoginActivity.this, "登录失败，请检查网络状况",
                            Toast.LENGTH_SHORT).show();
                    proBarLogin.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void login2() {
        proBarLogin.setVisibility(View.INVISIBLE);

        ownInformationSaveEditor.putString("identity", identity);//保存用户名、身份
        ownInformationSaveEditor.putString("userName", userName);
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
