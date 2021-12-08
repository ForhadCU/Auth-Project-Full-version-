package com.example.authprojectfullversion.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authprojectfullversion.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private static final String TAG_1 = "Facebook_Auth";
    private EditText editTextEmail, editTextPass;
    private Button btnLogin;
    private TextView textViewGotoReg, tvForgotPass;
    private ProgressBar progressBar;
    private LoginButton loginButtonFb;

    private FirebaseAuth mAuth;
    private FirebaseUser cUser;
    private String cUId;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Login Screen");

        mInit();
        mCheckCurrentUser();
        mBind();
        mSetOnClickListener();
        mClickLoginBtnFb();
    }

    private void mClickLoginBtnFb() {
        loginButtonFb.setReadPermissions("email", "public_profile");
        loginButtonFb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG_1, "onSuccess: "+loginResult);
                mHandleFbToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG_1, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG_1, "FacebookException: "+ error.toString());
            }
        });
    }

    private void mHandleFbToken(AccessToken accessToken) {
        Log.d(TAG_1, "mHandleFbToken: "+ accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Log.d(TAG_1, "Sign in with fb-credential: Successful.");
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                } else {
                    Log.d(TAG_1, "SignIn with credential: Unsuccessful!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG_1, "onFailure: "+ e.toString());
            }
        });
    }

    private void mCheckCurrentUser() {
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified())
        {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }

    private void mInit() {
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
    }

    private void mSetOnClickListener() {
        tvForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        textViewGotoReg.setOnClickListener(this);
//        loginButtonFb.setOnClickListener(this);
    }

    private void mBind() {
        tvForgotPass = findViewById(R.id.tvForgotPass);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPass);
        btnLogin = findViewById(R.id.btnLogin);
        textViewGotoReg = findViewById(R.id.tvGotoRegister);
        progressBar = findViewById(R.id.progressBar);
        loginButtonFb = findViewById(R.id.login_button_fb);
    }

    //jtOnclick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                Log.d(TAG, "onClick: " + "Click Login Btn");
                String email = editTextEmail.getText().toString().trim();
                String pass = editTextPass.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                mLogin(email, pass);
                
                break;

            case R.id.tvGotoRegister:
                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivity(intent1);
                Log.d(TAG, "onClick: " + "Click go to Register");
                break;

            case R.id.tvForgotPass:
                Intent intent2 = new Intent(this, ResetPassActivity.class);
                startActivity(intent2);
        }
    }

    private void mLogin(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    if (mAuth.getCurrentUser().isEmailVerified())
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email isn't verified yet", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
        mAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }
}