package com.ftd.schaepher.coursemanagement.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.db.Initialize;
import com.ftd.schaepher.coursemanagement.tools.ConstantStr;
import com.ftd.schaepher.coursemanagement.tools.Loger;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * Created by sxq on 2015/10/28.
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "LoginActivity";
    private static final int RESULT_OTHER = 1;
    private static final int RESULT_FALSE = 0;

    private Button btnLogin;
    private EditText edtTxWorkNumber;
    private EditText edtTxPassWord;
    private RadioGroup rdoGroup;
    private TextInputLayout layoutWorkNumber;
    private TextInputLayout layoutPassWord;
    private ProgressDialog progress;

    private String workNumber;
    private String password;
    private String identity;

    private SharedPreferences.Editor informationEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtTxWorkNumber = (EditText) findViewById(R.id.edtTx_login_workNumber);
        edtTxPassWord = (EditText) findViewById(R.id.edtTx_login_password);
        rdoGroup = (RadioGroup) findViewById(R.id.rdoGroup_check_identity);
        btnLogin = (Button) findViewById(R.id.btn_login);
        layoutWorkNumber = (TextInputLayout) findViewById(R.id.inputLayout_login_workNumber);
        layoutPassWord = (TextInputLayout) findViewById(R.id.inputLayout_login_password);

        informationEditor = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE).edit();

        edtTxWorkNumber.setOnFocusChangeListener(this);
        edtTxPassWord.setOnFocusChangeListener(this);
        btnLogin.setOnClickListener(this);

        autoSetWorkNumber();
        initDatabaseData();
    }


    /**
     * 自动输入保存的账号
     */
    private void autoSetWorkNumber() {
        workNumber = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE).getString(ConstantStr.USER_WORKNUMBER, "");
        if (!workNumber.equals("")) {
            edtTxWorkNumber.setText(workNumber);
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
            Loger.v("初始化数据库", "正在初始化");
            editor.putBoolean("isFirstRun", false);
            editor.apply();

            Initialize initialize = new Initialize(); // 初始化数据库
            initialize.init(this);
            Loger.v("初始化数据库", "初始化完成");
        } else {
            Loger.v("初始化数据库", "已初始化过");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                workNumber = edtTxWorkNumber.getText().toString().trim();
                password = edtTxPassWord.getText().toString().trim();
                switch (rdoGroup.getCheckedRadioButtonId()) {
                    case R.id.rdoBtn_teacher:
                        identity = ConstantStr.ID_TEACHER;
                        break;
                    case R.id.rdoBtn_department_head:
                        identity = ConstantStr.ID_DEPARTMENT_HEAD;
                        break;
                    case R.id.rdoBtn_teaching_office:
                        identity = ConstantStr.ID_TEACHING_OFFICE;
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
        if (workNumber.equals("") || password.equals("")) {
            if (workNumber.equals("")) {
                layoutWorkNumber.setError(getString(R.string.nullWorkNumber));
            }
            if (password.equals("")) {
                layoutPassWord.setError(getString(R.string.nullPassWord));
            }
        } else {
            layoutWorkNumber.setError(null);
            layoutPassWord.setError(null);
            return true;
        }
        return false;
    }

    public void login() {
        try {
            NetworkManager.login(workNumber, password, identity, new NetworkManager.ResponseCallback() {
                @Override
                public void onResponse(Response response) throws IOException {
                    String result = response.body().string();
                    Loger.d(TAG, result);
                    progress.cancel();

                    switch (result) {
//                        这里应该改为获取服务器个人数据，并存储到数据库中
                        case "true":
                            informationEditor.putString(ConstantStr.USER_IDENTITY, identity);
                            informationEditor.putString(ConstantStr.USER_WORKNUMBER, workNumber);
                            informationEditor.apply();

                            Intent intend = new Intent();
                            intend.setClass(LoginActivity.this, TaskListActivity.class);
                            LoginActivity.this.finish();
                            startActivity(intend);
                            break;
                        case "false":
                            sendToast("账号或密码错误");
                            break;
                        default:
                            break;
                    }

                }

                @Override
                public void onFailure(Request request, IOException e) {
                    progress.cancel();
                    sendToast("请求服务器失败");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginOffLine() {
        informationEditor.putString(ConstantStr.USER_IDENTITY, identity);//保存用户名、身份
        informationEditor.putString(ConstantStr.USER_WORKNUMBER, workNumber);
        informationEditor.apply();

        Intent intend = new Intent();
        intend.setClass(LoginActivity.this, TaskListActivity.class);
        LoginActivity.this.finish();
        startActivity(intend);
    }

    // 处理输入错误提示
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!edtTxWorkNumber.getText().toString().equals("")) {
            layoutWorkNumber.setError(null);
        }

        if (!edtTxPassWord.getText().toString().equals("")) {
            layoutPassWord.setError(null);
        }
    }
}