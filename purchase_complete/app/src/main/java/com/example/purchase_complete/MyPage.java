package com.example.purchase_complete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyPage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
    }

    public void btn_q(View v) {
        Intent intent = new Intent(MyPage.this, FAQActivity.class);
        startActivity(intent); //액티비티 이동
    }

    public void btn_pc(View v) {
        Intent intent = new Intent(MyPage.this, purcomplt_MainActivity.class);
        startActivity(intent); //액티비티 이동
    }

    public void btn_s(View v) {
        Intent intent = new Intent(MyPage.this, Soldout_MainActivity.class);
        startActivity(intent); //액티비티 이동
    }
       /* public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_q: {
                    Intent intent = new Intent(MyPage.this, FAQActivity.class);
                    startActivity(intent); //액티비티 이동
                }
                break;
                case R.id.btn_pc: {
                    Intent intent = new Intent(MyPage.this, purcomplt_MainActivity.class);
                    startActivity(intent); //액티비티 이동
                }
                break;
                case R.id.btn_s: {
                    Intent intent = new Intent(MyPage.this, Soldout_MainActivity.class);
                    startActivity(intent); //액티비티 이동
                }
                break;
            }
        };*/ //계속 에러가 나서 버튼 이동이 안됩니당ㅜㅜ
}