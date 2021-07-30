package com.example.soonbox_findpw;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FAQActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag6 frag6;
    private Frag7 frag7;
    private Frag8 frag8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);

        bottomNavigationView=findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_question:
                        setFrag(0);
                        break;
                    case R.id.action_phone:
                        setFrag(1);
                        break;
                    case R.id.action_list:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });
        frag6= new Frag6();
        frag7= new Frag7();
        frag8= new Frag8();
        setFrag(0);


    }

    private void setFrag(int n){
        fm= getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.main_frame, frag6);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, frag7);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, frag8);
                ft.commit();
                break;
        }
    }


}