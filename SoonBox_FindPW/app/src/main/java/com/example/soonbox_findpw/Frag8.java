package com.example.soonbox_findpw;

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

public class Frag8 extends Fragment {
    private View view;
    private ListView list3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag3, container, false);

        list3=(ListView) view.findViewById(R.id.list3);

        List<String> data= new ArrayList<>();

        ArrayAdapter<String> adapter= new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, data);
        list3.setAdapter(adapter);

        data.add("욕설같은 비매너 행위는 안돼요");
        data.add("제품 가격을 정확히 등록하세요");
        data.add("중복 게시글이나 도배는 안돼요");
        data.add("구매가 확정되면 판매상품은 꼭 순박스에 넣어주세요");
        adapter.notifyDataSetChanged();

        return view;
    }
}
