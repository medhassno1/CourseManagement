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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.Initialize;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
import com.ftd.schaepher.coursemanagement.tools.ParseJson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.ProgressView;

import org.apache.http.Header;
import org.json.JSONArray;

import java.nio.charset.Charset;

//import com.rey.material.widget.ProgressView;

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
     //   loginTest();
    }

    /**
     * 自动保存用户名
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
    private void isFirstInit(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun) {
            Log.d("debug", "第一次运行");
            editor.putBoolean("isFirstRun", false);
            editor.commit();

            Initialize initialize = new Initialize();//初始化数据库
            initialize.init(this);
        }else{
            Log.d("debug", "不是第一次运行");
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userName = edtTxUserName.getText().toString().trim();
                password = edtTxPassWord.getText().toString().trim();
                for (int i = 0; i < rdoGroup.getChildCount(); i++) {
                    RadioButton rdoBtnId = (RadioButton) rdoGroup.getChildAt(i);
                    if (rdoBtnId.isChecked()) {
                        identity = rdoBtnId.getText().toString().trim();
                        // 由于服务端暂时只有教师和负责人两种身份,这里暂时也只有这两种身份，后期再修改
                        if (identity.equals("教师")) {
                            identity = "teacher";
                        } else {
                            identity = "manager";
                        }
                    }
                }
                if (isTrueForm()) {
                    proBarLogin.setVisibility(View.VISIBLE);
                    login();
//                    loginTest();
                }

                break;
            default:
                break;
        }
    }

    /*检查账号密码*/
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

    //处理登录逻辑
    public void login() {
        RequestParams params = new RequestParams();
        params.add("login-user", userName);
        params.add("login-password", password);
        params.add("ident", identity);
        try {
            NetworkManager.post(NetworkManager.URL_LOGIN, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    Log.i("str", "网络连接成功");
                    Log.i("statusCode:", statusCode + "");
                    Charset charset = Charset.forName("UTF-8");
                    String html = new String(response, charset);
                    int logResult = html.indexOf("alert");
                    //打印获得的网页
                    Log.w("first post=", html);
                    if (logResult == -1) {
                        //跳转,同时将选择登录的身份信息存储在本地，方便下一个界面根据不同身份做相应修改
                        proBarLogin.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                        ownInformationSaveEditor.putString("identity", identity);//保存用户名、身份
                        ownInformationSaveEditor.putString("userName", userName);
                        ownInformationSaveEditor.commit();

                        isFirstInit();//判断是否是第一次操作，并执行相关操作

                        LoginActivity.this.finish();
                        startActivity(intent);


                    } else {
                        proBarLogin.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "账号或密码错误",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //修改网络出错的提示信息
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

    //处理输入错误提示
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (!edtTxUserName.getText().toString().equals("")) {
            layoutUserName.setError(null);
        }

        if (!edtTxPassWord.getText().toString().equals("")) {
            layoutPassWord.setError(null);
        }
    }


    // 用来测试login的类
    public void loginTest() {
        ownInformationSaveEditor.putString("userName", userName);
        ownInformationSaveEditor.commit();
        NetworkManager.post(NetworkManager.URL_JSON, null, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, String s, Object o) {
                Log.w(TAG, s);
                ParseJson parseJson = new ParseJson();
                parseJson.toTeacher(s);
            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, String s, Object o) {

            }

            @Override
            protected Object parseResponse(String s, boolean b) throws Throwable {
                JSONArray array = new JSONArray(s);
                return array;
            }
        });
    }
}
