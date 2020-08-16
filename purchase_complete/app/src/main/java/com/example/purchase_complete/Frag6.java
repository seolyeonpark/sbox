package com.example.purchase_complete;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Frag6 extends Fragment {

    private View view;
    private ListView list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag1, container, false);

        list=(ListView) view.findViewById(R.id.list);

        List<String> data= new ArrayList<>();

        ArrayAdapter<String> adapter= new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);

        data.add("상품 등록은 어떻게 하는건가요?");
        data.add("상품이 업로드 되지 않아요");
        data.add("내 정보는 어떻게 바꾸나요?");
        adapter.notifyDataSetChanged();

        return view;
    }


}
