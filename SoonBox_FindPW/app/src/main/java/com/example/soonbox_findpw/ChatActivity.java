package com.example.soonbox_findpw;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //String[] myDataset = {"안녕", "오늘", "뭐했어", "영화볼래?"};

    EditText etText, etName;
    Button btnSend;
    String email, chatid;
    List<Chat> mChat;
    FirebaseDatabase database;
    DatabaseReference chatRef, profile;

    private FirebaseAuth firebaseAuth;
    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    String email1= user.getEmail();
    int idx = email1.indexOf("@");
    String mailid = email1.substring(0, idx);
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        chatid = intent.getStringExtra("chatid");

        database = FirebaseDatabase.getInstance();

        //profile= database.getReference("users").child("mailid").child("profileimage").child("profileimage");
        chatRef= database.getReference("chats");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            email = user.getEmail();

        }

        etText=(EditText)findViewById(R.id.etText);
        btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String name= database.getReference("users").child(chatid).child("userName").toString();
                String stText= etText.getText().toString();
                //String profile= database.getReference("users").child(chatid).child("profileimage").child("profileiamge").toString();
                if(stText.equals("")||stText.isEmpty()){
                    Toast.makeText(ChatActivity.this,"내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(ChatActivity.this, email + "," + stText, Toast.LENGTH_SHORT).show();

                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());


                    DatabaseReference myRef = database.getReference("chats").child(chatid).child(formattedDate);


                    Hashtable<String, String> chat
                            = new Hashtable<String, String>();
                    chat.put("email", email);
                    chat.put("text", stText);
                    //chat.put("profile", profile);
                    myRef.setValue(chat);
                    etText.setText("");
                }
            }
        });

        Button btnfinish = (Button) findViewById(R.id.btnFinish);
        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mChat = new ArrayList<>();
        mAdapter = new MyAdapter(mChat, email);
        mRecyclerView.setAdapter(mAdapter);

        DatabaseReference myRef = database.getReference("chats").child(chatid);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                // A new comment has been added, add it to the displayed list
                Chat chat = dataSnapshot.getValue(Chat.class);

                // [START_EXCLUDE]
                // Update RecyclerView
                mChat.add(chat);
                mRecyclerView.scrollToPosition(mChat.size()-1);
                mAdapter.notifyItemInserted(mChat.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}