package com.ftd.schaepher.coursemanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.nio.charset.Charset;

import org.apache.http.Header;

/**
 * Created by sxq on 2015/10/28.
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogin;
    private EditText edtTxUserName;
    private EditText edtTxPassWord;
    private RadioGroup rdoGroup;

    private String userName;
    private String password;
    private String identity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtTxUserName = (EditText)findViewById(R.id.edtTx_login_username);
        edtTxPassWord = (EditText)findViewById(R.id.edtTx_login_password);
        rdoGroup = (RadioGroup)findViewById(R.id.rdoGroup_check_identity);
        btnLogin = (Button) findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                userName = edtTxUserName.getText().toString().trim();
                password = edtTxPassWord.getText().toString().trim();
                for(int i=0;i<rdoGroup.getChildCount();i++){
                    RadioButton rdoBtnIdent = (RadioButton)rdoGroup.getChildAt(i);
                    if(rdoBtnIdent.isChecked()){
                        identity = rdoBtnIdent.getText().toString().trim();
                        if(identity.equals("教师")){  //由于服务端暂时只有教师和负责人两种身份,
                            identity="teacher";        //这里暂时也只有这两种身份，后期再修改
                        }else{
                            identity="manager";
                        }
                    }
                }
                if(checkEdit()){
                   Login();
                }

                break;
            default:
                break;
        }
    }

    /*检查账号密码*/
    private boolean checkEdit(){
        if(userName.equals("")){
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else if(password.equals("")){
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else{
            return true;
        }
        return false;
    }

    //处理登录逻辑
    public void Login() {
        RequestParams params = new RequestParams();
        params.add("login-user", userName);
        params.add("login-password", password);
        params.add("ident",identity);
        try {
            NetworkManager.post(NetworkManager.URL_LOGIN, params, new AsyncHttpResponseHandler() {

                 @Override
                public void onSuccess(int statusCode,Header[] headers, byte[] response) {
                    Log.i("str","网络连接成功");
                    Log.i("statusCode:",statusCode+"");
                    Charset charset = Charset.forName("UTF-8");
                    String html = new String(response, charset);
                    int logResult = html.indexOf("alert");
                    //打印获得的网页
                    Log.w("first post=", html);
                    if (logResult == -1) {
                        //跳转
                        Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                        LoginActivity.this.finish();
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "账号或密码错误",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] response, Throwable throwable) {
                    Toast.makeText(LoginActivity.this, "请检查网络" + String.valueOf(statusCode),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e) {
            Log.e("Login()", e.toString());
        }
    }



}
