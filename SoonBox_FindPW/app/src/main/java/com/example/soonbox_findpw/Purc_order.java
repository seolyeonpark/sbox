package com.example.soonbox_findpw;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soonbox_findpw.data.Posts;
import com.example.soonbox_findpw.data.SellerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Purc_order extends AppCompatActivity {

    private ArrayList<Posts> arrayList;
    private ArrayList<String> postIdList;
    private SellerAdapter sellerAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase mdatabase;
    private DatabaseReference databaseRef;
    private DatabaseReference postDatabaseRef;
    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();

    int idx = email.indexOf("@");

    String mailid = email.substring(0, idx);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purc_order_one);


        recyclerView = (RecyclerView) findViewById(R.id.order_recyclerview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        postIdList = new ArrayList<>();

        mdatabase = FirebaseDatabase.getInstance(); //파이어베이스 DB연동
        databaseRef = mdatabase.getReference("users").child(mailid).child("uploaded");//DB테이블 연결


        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아옵니다.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출

                    String posts = snapshot.getValue(String.class); //만들어둔 Posts 객체에 데이터를 담는다.

                    postIdList.add(posts);
                    getPosts(postIdList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비 가져오던 중 에러 발생시
                Log.e("MainActivity", String.valueOf(error.toException())); //에러문 출력
            }
        });

        sellerAdapter = new SellerAdapter(arrayList, this, mailid);
        //sellerAdapter = new SellerAdapter(arrayList, this);
        recyclerView.setAdapter(sellerAdapter);
    }//onCreate

    void getPosts(final List<String> postIdList) {
        postDatabaseRef = mdatabase.getReference("Posts");

        postDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (String postId : postIdList) {
                    Posts post = dataSnapshot.child(postId).getValue(Posts.class);

                    arrayList.add(post);
                }

                sellerAdapter.notifyDataSetChanged();//리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }//Oncreate


}
