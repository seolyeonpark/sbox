package com.example.soonbox_findpw;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soonbox_findpw.data.CustomAdapter;
import com.example.soonbox_findpw.data.MyItemDecoration;
import com.example.soonbox_findpw.data.Posts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Frag1 extends Fragment {
    private View view;

    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Posts> arrayList;


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    //엑티비티는 onCreate로 만들면되는데 fragment는 onCreateView로 함
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag1, container, false);
        arrayList = new ArrayList<>(); //post 객체를 담을 어레이 리스트 어댑터쪽으로

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능강화

        //xml이 gridview로 보이게 하기 위해 GridLayoutManager 사용.
        GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 3); //3개씩 보이게
        recyclerView.setLayoutManager(gridManager);
        recyclerView.addItemDecoration(new MyItemDecoration(this));

        //recyclerView.scrollToPosition(0);
        adapter = new CustomAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }//onCreateView


    //이 부분을 추가하면 Fragment 위에서도 인텐트 등이 Activity처럼 잘 작동하는것 같습니다.
    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance(); //파이어베이스 DB연동
        databaseReference = database.getReference("Posts");//DB테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아옵니다.
                arrayList.clear();//기존 배열 리스트가 존재하지 않게 초기화
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ // 반복문으로 데이터 List를 추출
                    Posts posts = snapshot.getValue(Posts.class); //만들어둔 Posts 객체에 데이터를 담는다.
                    arrayList.add(posts);//담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비 가져오던 중 에러 발생시
                Log.e("MainActivity", String.valueOf(error.toException())); //에러문 출력
            }
        });
        //initDataset();
    }


}
