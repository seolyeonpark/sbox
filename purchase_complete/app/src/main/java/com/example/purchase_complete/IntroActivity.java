package com.example.purchase_complete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//앱 시작 인트로 부분
class IntroActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

}
