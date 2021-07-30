package com.example.soonbox_findpw;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soonbox_findpw.data.ConsumerAdapter;
import com.example.soonbox_findpw.data.Posts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Purc_application extends AppCompatActivity {

    private ArrayList<Posts> arrayList;
    private ArrayList<String> purchasedList; //Purc_order 의 private ArrayList<String> postIdList; 와 동일
    private ConsumerAdapter consumerAdapter; //새로 추가한 어댑터 ; 기존 CustomAdapter는 메인화면 리사이클러뷰를 데려오는 애라 코드 구조가 다름
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase mdatabase;
    private DatabaseReference databaseRef;
    private DatabaseReference postDatabaseRef;

    String randompw;//랜덤 변수 불러와서 ConsumerAdapter넣을 변수

    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();

    int idx = email.indexOf("@");

    String mailid = email.substring(0, idx);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purc_application_one);


        recyclerView = (RecyclerView) findViewById(R.id.application_recyclerview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        purchasedList = new ArrayList<>();

        mdatabase = FirebaseDatabase.getInstance(); //파이어베이스 DB연동

        /////비번 파베에서 읽어서 어댑터에 넣어줄 변수
        databaseRef = mdatabase.getReference("users").child(mailid).child("postpw");

        ValueEventListener pwListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                randompw = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        databaseRef.addValueEventListener(pwListener);

        databaseRef = mdatabase.getReference("users").child(mailid).child("purchased");//DB테이블 연결

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아옵니다.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출

                    String posts = snapshot.getValue(String.class); //만들어둔 Posts 객체에 데이터를 담는다.

                    purchasedList.add(posts);
                    getPosts(purchasedList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비 가져오던 중 에러 발생시
                Log.e("MainActivity", String.valueOf(error.toException())); //에러문 출력
            }
        });

        consumerAdapter = new ConsumerAdapter(arrayList, this, randompw);
        //sellerAdapter = new SellerAdapter(arrayList, this);
        recyclerView.setAdapter(consumerAdapter);
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

                consumerAdapter.notifyDataSetChanged();//리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


      /*  randompw = (Button) findViewById(R.id.random_pw);

        recyclerView = (RecyclerView) findViewById(R.id.order_recyclerview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        purchasedList = new ArrayList<>();

        mdatabase = FirebaseDatabase.getInstance(); //파이어베이스 DB연동
        databaseRef = mdatabase.getReference("users").child(mailid).child("purchased");//DB테이블 연결


        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아옵니다.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출

                    String posts = snapshot.getValue(String.class); //만들어둔 Posts 객체에 데이터를 담는다.
                    purchasedList.add(posts);

                }
                getPosts(purchasedList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비 가져오던 중 에러 발생시
                Log.e("MainActivity", String.valueOf(error.toException())); //에러문 출력
            }
        });

        CustomAdapter = new CustomAdapter(arrayList, this);
        //CustomAdapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(CustomAdapter);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        setContentView(R.layout.purc_application_two);
        randompw = (Button) findViewById(R.id.random_pw);
        randompw.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 버튼 누르면 수행 할 명령
                AlertDialog.Builder builder = new AlertDialog.Builder(Purc_application.this);
                builder.setTitle("구매");
                builder.setMessage("비밀번호는 "+randompw+" 입니다./n 확인 완료시 예 버튼을 눌러주세요.");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show(); //팝업창 보여줌
            }
        });
    }//onCreate

    void getPosts(final ArrayList<String> purchasedList) {
        postDatabaseRef = mdatabase.getReference("Posts");

        postDatabaseRef.orderByChild("purchased").addChildEventListener(new ChildEventListener() {
            void getPosts(final ArrayList<String> purchasedList) {
                postDatabaseRef = mdatabase.getReference("Posts");

                postDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();

                        for (String purchased : purchasedList) {

                            // TODO: 2020-10-24 Posts가 Null 값을 갖고 있음 (데이터 호출 방식이 이게 아닌 것 같음)
                            Posts post = dataSnapshot.child(purchased).getValue(Posts.class);
                            arrayList.add(post);
                        }

                        CustomAdapter.notifyDataSetChanged();//리스트 저장 및 새로고침
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }//Oncreate




   /* randompw = (Button) findViewById(R.id.random_pw);
        randompw.setOnClickListener(new View.OnClickListener() {
    @Override
        public void onClick(View View) {
            switch (View.getId()){
                case R.id.random_pw:

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("purchased");
                    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                   //DB테이블 연결
                    DatabaseReference myRef = database.getReference("postpw");
                    final String purchased = reference.push().getKey();
                    String postpw = postpw[];

                    AlertDialog.Builder builder = new AlertDialog.Builder(Purc_application.this);
                    builder.setTitle("구매");
                    builder.setMessage("비밀번호는 "+postpw+" 입니다./n 확인 완료시 예 버튼을 눌러주세요.");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show(); //팝업창 보여줌
                    break;
            }

        }
    });*/


    /*TextView receiveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purc_application_two);

        ImageView imageview = (ImageView)findViewById(R.id.iv_thumbnail);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = getIntent().getByteArrayExtra("postimage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        Intent intent = getIntent();

        String productStr = intent.getExtras().getString("product"); //전달한 값을 받을 때
        receiveView = (TextView)findViewById(R.id.apply_product);
        receiveView.setText(productStr);

        String priceStr = intent.getExtras().getString("price");
        receiveView = (TextView)findViewById(R.id.apply_price);
        receiveView.setText(priceStr);

        String nameStr = intent.getExtras().getString("name");
        receiveView = (TextView)findViewById(R.id.apply_Custom);
        receiveView.setText(nameStr);

        imageview.setImageBitmap(bitmap);
    }*/

}