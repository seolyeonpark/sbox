package com.example.purchase_complete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Soldout_MainActivity extends AppCompatActivity {

    ArrayList<List> Soldoutlist;
    LayoutInflater layoutInflater;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soldout__main);

        Soldoutlist = new ArrayList<>();
        List list1 = new List("리빙랩1", "1000");
        List list2 = new List("리빙랩2", "2000");
        List list3 = new List("리빙랩3", "3000");
        List list4 = new List("리빙랩4", "4000");
        List list5 = new List("리빙랩5", "5000");
        List list6 = new List("리빙랩6", "6000");
        List list7 = new List("리빙랩7", "7000");
        List list8 = new List("리빙랩8", "8000");
        List list9 = new List("리빙랩9", "9000");


        Soldoutlist.add(list1);
        Soldoutlist.add(list2);
        Soldoutlist.add(list3);
        Soldoutlist.add(list4);
        Soldoutlist.add(list5);
        Soldoutlist.add(list6);
        Soldoutlist.add(list7);
        Soldoutlist.add(list8);
        Soldoutlist.add(list9);

        layoutInflater = LayoutInflater.from(Soldout_MainActivity.this);
        container = findViewById(R.id.container);

        for(int i = 0; i<Soldoutlist.size(); i++) {
            View view = layoutInflater.inflate(R.layout.putlist, null, false);
            TextView name = view.findViewById(R.id.name);
            TextView price = view.findViewById(R.id.price);
            ImageView image = view.findViewById(R.id.image);

            name.setText(Soldoutlist.get(i).name);
            price.setText(Soldoutlist.get(i).price);
            image.setImageResource(R.drawable.ic_launcher_foreground);

            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Soldout_MainActivity.this, purcomplt_SecondActivity.class);
                    intent.putExtra("user_name",Soldoutlist.get(finalI).name);
                    intent.putExtra("user_price",Soldoutlist.get(finalI).price);
                    startActivity(intent);
                }
            });
            container.addView(view);
        }
    }
}