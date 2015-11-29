package com.ftd.schaepher.coursemanagement.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

    private SharedPreferences.Editor ownInformationSaveEditor;

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

        ownInformationSaveEditor = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).edit();

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
        workNumber = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getString(ConstantTools.USER_WORKNUMBER, "");
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
                workNumber = edtTxWorkNumber.getText().toString().trim();
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
        final NetworkManager manager = new NetworkManager();

        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(TAG,"RUN");
                    String result = manager.login(workNumber, password, identity);
                    Log.d(TAG,result);
                    if (result.equals("true")) {
                        ownInformationSaveEditor.putString(ConstantTools.USER_IDENTITY, identity);//保存账号、身份
                        ownInformationSaveEditor.putString(ConstantTools.USER_WORKNUMBER, workNumber);
                        ownInformationSaveEditor.apply();

                        Intent intend = new Intent();
                        intend.setClass(LoginActivity.this, TaskListActivity.class);
                        LoginActivity.this.finish();
                        startActivity(intend);
                    } else if (result.equals("false")) {
                        Message msg = new Message();
                        msg.what = RESULT_FALSE;
                        mHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = RESULT_OTHER;
                        mHandler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void loginOffLine() {
        ownInformationSaveEditor.putString(ConstantTools.USER_IDENTITY, identity);//保存用户名、身份
        ownInformationSaveEditor.putString(ConstantTools.USER_WORKNUMBER, workNumber);
        ownInformationSaveEditor.apply();

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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESULT_OTHER:
                    progress.cancel();
                    Toast.makeText(LoginActivity.this, "请求服务器失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_FALSE:
                    progress.cancel();
                    Toast.makeText(LoginActivity.this, "账号或密码错误",
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

}
