package com.johnnghia.scanned;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.johnnghia.scanned.models.objects.User;

public class RegisterActivity extends Activity {
    EditText edtFirstName, edtLastName, edtUser, edtPass, edtRePass;
    Button btnSignup;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();
        ConnectWidgets();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(edtUser.getText().toString(), edtPass.getText().toString(), edtFirstName.getText().toString(), edtLastName.getText().toString());
                switch (user.checkInfor()) {
                    case 1:
                        if(edtRePass.getText().toString().equals(user.getPass())){
                            mAuth.createUserWithEmailAndPassword(user.getUser(), user.getPass())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){

                                                // TO-DO: Create information in database


                                                Toast.makeText(RegisterActivity.this, "Create new account successful.", Toast.LENGTH_SHORT).show();
                                                Intent intentSignin = new Intent(RegisterActivity.this, LoginActivity.class);

                                                // TO-DO: Delete this activity in stack

                                                startActivity(intentSignin);
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Some thing went wrong.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Your re-password is not same as password.", Toast.LENGTH_SHORT).show();
                        }
                        break;
//                    case 0: khong ton tai truong hop 0
//                        Toast.makeText(RegisterActivity.this, "System error.", Toast.LENGTH_SHORT).show();
//                        break;
                    case -1:
                        Toast.makeText(RegisterActivity.this, "Username is empty.", Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(RegisterActivity.this, "Username's length is not able.", Toast.LENGTH_SHORT).show();
                        break;
                    case -3:
                        Toast.makeText(RegisterActivity.this, "Username is not a email.", Toast.LENGTH_SHORT).show();
                        break;
                    case -4:
                        Toast.makeText(RegisterActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                        break;
                    case -5:
                        Toast.makeText(RegisterActivity.this, "Password's length is too long or short.", Toast.LENGTH_SHORT).show();
                        break;
                    case -6:
                        Toast.makeText(RegisterActivity.this, "First name is empty.", Toast.LENGTH_SHORT).show();
                        break;
                    case -7:
                        Toast.makeText(RegisterActivity.this, "Last name is empty.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
            }
        });
    }

    void ConnectWidgets(){
        edtFirstName    = findViewById(R.id.register_edt_firstname);
        edtLastName     = findViewById(R.id.register_edt_lastname);
        edtUser         = findViewById(R.id.register_edt_username);
        edtPass         = findViewById(R.id.register_edt_password);
        edtRePass       = findViewById(R.id.register_edt_repassword);

        btnSignup       = findViewById(R.id.register_btn_signin);
    }
}
