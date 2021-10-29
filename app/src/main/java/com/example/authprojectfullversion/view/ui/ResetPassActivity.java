package com.example.authprojectfullversion.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authprojectfullversion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPassActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ResetPassActivity";
    private EditText editTextEmail;
    private Button btnContinue;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private String cUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        mBind();
        mInit();
        mSetOnclick();

    }

    private void mInit() {
        mAuth = FirebaseAuth.getInstance();
        cUId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    }

    private void mSetOnclick() {
        btnContinue.setOnClickListener(this);
    }

    private void mBind() {
        progressBar = findViewById(R.id.progressBar);
        editTextEmail = findViewById(R.id.edtTxtEmail);
        btnContinue = findViewById(R.id.btnContinue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                String email = editTextEmail.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                mResetPass(email);
                break;
        }
    }

    private void mResetPass(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Link sent to email address", Toast.LENGTH_SHORT).show();
                    Intent intentLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentLoginActivity);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}