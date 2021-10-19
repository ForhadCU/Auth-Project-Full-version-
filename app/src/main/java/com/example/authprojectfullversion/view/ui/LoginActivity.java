package com.example.authprojectfullversion.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authprojectfullversion.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Click test";

    private EditText editTextEmail, editTextPass;
    private Button btnLogin;
    private TextView textViewGotoReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Login Screen");

        mBind();

        mSetOnClickListener();
    }

    private void mSetOnClickListener() {
        btnLogin.setOnClickListener(this);
        textViewGotoReg.setOnClickListener(this);
    }

    private void mBind() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPass);
        btnLogin = findViewById(R.id.btnLogin);
        textViewGotoReg =findViewById(R.id.tvGotoRegister);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnLogin:
                Log.d(TAG, "onClick: "+ "Click Login Btn");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.tvGotoRegister:
                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivity(intent1);
                Log.d(TAG, "onClick: "+ "Click go to Register");

        }
    }
}