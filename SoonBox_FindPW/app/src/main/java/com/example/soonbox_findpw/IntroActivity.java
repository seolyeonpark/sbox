package com.example.soonbox_findpw;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

//앱 시작 인트로 부분
public class IntroActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

   /* //로그인 되어있으면 메인 홈으로 이동, 아니면 홈으로 이동
     firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
        } 개발 과정중 불편할테니 주석 처리*/


        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

}
