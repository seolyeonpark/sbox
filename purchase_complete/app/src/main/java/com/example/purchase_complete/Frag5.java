package com.example.purchase_complete;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag5 extends Fragment {

    View view;
    Button btn_q;
    Button btn_setting;
    @Nullable
    @Override
    //엑티비티는 onCreate로 만들면되는데 fragment는 onCreateView로 함
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.activity_my_page, container, false);

        btn_q = (Button)view.findViewById(R.id.btn_q);

        btn_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), FAQActivity.class);
                startActivity(intent); //원래 액티비티 이동이 돼야하는데 지금 무슨 오류때문인지 안됩니다.. 이 부분은 제가 알게되는 즉시 수정된 코드 보내드릴테니 안 건드려도 괜찮아요!
            }
        });

        return view;

    }

/* public void btn_set (View v){
        Intent intent = new Intent(getActivity(), SettingsFragment.class);
        startActivity(intent);
    }*/
}