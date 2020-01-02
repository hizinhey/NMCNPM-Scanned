package com.johnnghia.scanned;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.johnnghia.scanned.models.services.MCFirebaseResourceTool;

public class SplashActiviy extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        mAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

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
