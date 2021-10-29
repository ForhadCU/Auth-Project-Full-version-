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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Click test";

    private EditText editTextUserName, editTextEmail, editTextPass, editTextPhone;
    private Button buttonRegister;
    private ProgressBar progressBar;

    private ActionCodeSettings actionCodeSettings;
    private FirebaseAuth mAuth;
    private FirebaseUser cUser;
    private FirebaseFirestore firebaseFirestore;

    private String cUId;
    HashMap<String, String> hashMapUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Registration");

        mInit();
        mBind();
        mSetOnclickListener();
//        mBuildActionCodeSetting();
    }

    private void mInit() {
        mAuth = FirebaseAuth.getInstance();
//        cUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
//        cUId = cUser.getUid();
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
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                Log.d(TAG, "onClick: " + "Clicked Register button");
                String email = editTextEmail.getText().toString().trim();
                String pass = editTextPass.getText().toString().trim();
                String name = editTextUserName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                mRegister(email, pass, name, phone);
                break;
        }
    }

    private void mRegister(String email, String pass, String name, String phone) {
        hashMapUserDetails = new HashMap<>();
        hashMapUserDetails.put("email", email);
        hashMapUserDetails.put("pass", pass);
        hashMapUserDetails.put("name", name);
        hashMapUserDetails.put("phone", phone);

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mEmailVerification();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
                progressBar.setVisibility(View.GONE);
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

    private void mStoreCredentials() {
        firebaseFirestore.collection("USERS")
                .document(mAuth.getUid())
                .set(hashMapUserDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent loginScr = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(loginScr);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
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

    private void mEmailVerification() {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Verification email has been sent", Toast.LENGTH_SHORT).show();
                    mStoreCredentials();
                } else {
                    Toast.makeText(getApplicationContext(), "Email hasn't sent!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
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