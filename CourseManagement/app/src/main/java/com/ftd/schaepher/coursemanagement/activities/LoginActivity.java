package com.ftd.schaepher.coursemanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ftd.schaepher.coursemanagement.R;

/**
 * Created by sxq on 2015/10/28.
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
//                startActivity(new Intent(LoginActivity.this,TaskJxbActivity.class));
                break;
            default:
                break;
        }
    }
}
