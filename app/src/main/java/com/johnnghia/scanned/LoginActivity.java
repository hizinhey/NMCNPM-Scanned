package com.johnnghia.scanned;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.johnnghia.scanned.models.objects.User;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;

    Button btnLogin;
    EditText edtUser, edtPass;
    TextView linkForgot, linkSignup;

    private int countFault = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Connect Element
        ConnectWidgets();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(edtUser.getText().toString(), edtPass.getText().toString());
                switch (user.checkUser()) {
                    case 1:
                        mAuth.signInWithEmailAndPassword(user.getUser(), user.getPass())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Task completed successfully
                                            AuthResult result = task.getResult();
                                            Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        } else {
                                            // Task failed with an exception
                                            Exception exception = task.getException();
                                            Toast.makeText(LoginActivity.this, "Username or password is not corrected.", Toast.LENGTH_SHORT).show();
                                            countFault++;
                                            if(countFault >= 5){
                                                btnLogin.setEnabled(false);
                                                CountDownTimer count = new CountDownTimer((long)Math.pow(2, countFault)*1000, 1000) {
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        // nothing
                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        btnLogin.setEnabled(true);
                                                    }
                                                }.start();
                                            }
                                        }
                                    }
                                });
                        break;
//                    case 0: khong ton tai truong hop 0
//                        Toast.makeText(LoginActivity.this, "System error.", Toast.LENGTH_SHORT).show();
//                        break;
                    case -1:
                        Toast.makeText(LoginActivity.this, "Username is empty.", Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(LoginActivity.this, "Username's length is not able.", Toast.LENGTH_SHORT).show();
                        break;
                    case -3:
                        Toast.makeText(LoginActivity.this, "Username is not a email.", Toast.LENGTH_SHORT).show();
                        break;
                    case -4:
                        Toast.makeText(LoginActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                        break;
                    case -5:
                        Toast.makeText(LoginActivity.this, "Password's length is too long or short.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
            }
        });

        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignup = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentSignup);
            }
        });
    }

    private void ConnectWidgets() {
        btnLogin = findViewById(R.id.login_btn_signin);
        edtUser = findViewById(R.id.login_edt_username);
        edtPass = findViewById(R.id.login_edt_password);
        linkSignup = findViewById(R.id.login_txt_signup);
        linkForgot = findViewById(R.id.login_txt_forgot);
    }
}
