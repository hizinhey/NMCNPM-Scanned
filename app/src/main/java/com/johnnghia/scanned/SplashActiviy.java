package com.johnnghia.scanned;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActiviy extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
           intent = new Intent(SplashActiviy.this, LoginActivity.class);
        }
        else{
            intent = new Intent(SplashActiviy.this,MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
