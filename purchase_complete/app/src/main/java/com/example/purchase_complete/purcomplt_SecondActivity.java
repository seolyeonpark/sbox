package com.example.purchase_complete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class purcomplt_SecondActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purcomplt_second);

        Intent intent = new Intent(this.getIntent());
        String a = intent.getStringExtra("user_name");
        String b = intent.getStringExtra("user_price");

        TextView user_name = findViewById(R.id.user_id);
        TextView user_price = findViewById(R.id.user_price);

        user_name.setText(a);
        user_price.setText(b);

    }
}