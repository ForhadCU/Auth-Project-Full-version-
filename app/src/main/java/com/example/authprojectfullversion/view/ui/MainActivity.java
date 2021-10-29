package com.example.authprojectfullversion.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.authprojectfullversion.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnLogOut;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInit();
        mBind();
        mSetOnclick();
    }

    private void mInit() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void mSetOnclick() {
        btnLogOut.setOnClickListener(this);
    }

    private void mBind() {
        btnLogOut = findViewById(R.id.btnLogOut);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnLogOut:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}