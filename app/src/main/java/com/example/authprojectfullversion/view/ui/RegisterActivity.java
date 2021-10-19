package com.example.authprojectfullversion.view.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.authprojectfullversion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Click test";

    private EditText editTextUserName, editTextEmail, editTextPass, editTextPhone;
    private Button buttonRegister;

    private ActionCodeSettings actionCodeSettings;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Registration");

        mInitialization();
        mBind();
        mSetOnclickListener();
        mBuildActionCodeSetting();
    }

    private void mInitialization() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void mBuildActionCodeSetting() {
        actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://www.myfirstlink.com")
                .setHandleCodeInApp(true)
                .setIOSBundleId("com.example.ios")
                .setAndroidPackageName(
                        "com.example.android",
                        true,
                        "12"
                )
                .build();
    }

    private void mSetOnclickListener() {
        buttonRegister.setOnClickListener(this);
    }

    private void mBind() {

        editTextUserName = findViewById(R.id.editTxtUserName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPass);
        editTextPhone = findViewById(R.id.editTxtPhone);
        buttonRegister = findViewById(R.id.btnRegister);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                Log.d(TAG, "onClick: " + "Clicked Register button");
                String email = editTextEmail.getText().toString();
                String pass = editTextPass.getText().toString();
                mRegister(email, pass);

                break;
        }
    }

    private void mRegister(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser cUser = mAuth.getCurrentUser();
                    cUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
/*
        mAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Verification link has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
*/
    }
}