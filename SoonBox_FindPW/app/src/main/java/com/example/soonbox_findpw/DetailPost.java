package com.example.soonbox_findpw;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.soonbox_findpw.data.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import static java.lang.Integer.valueOf;

public class DetailPost extends AppCompatActivity {
FirebaseDatabase database;
DatabaseReference ref;
int val_forsale;
TextView product;
TextView price;
TextView name;
TextView etc;
TextView account;
ImageButton jjim;
Button chat;
ImageView postimage;
ImageView Deletemenu;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        //텍스트뷰 변수에 xml텍스트 연결
        product = (TextView)findViewById(R.id.read_product);
        price = (TextView)findViewById(R.id.read_price);
        name = (TextView)findViewById(R.id.read_seller);
        etc = (TextView)findViewById(R.id.read_etc);
        postimage = (ImageView)findViewById(R.id.read_image);
        jjim = (ImageButton)findViewById(R.id.jjim_btn);
        chat = (Button)findViewById(R.id.seller_chat_btn);

        Deletemenu = (ImageView)findViewById(R.id.menu_delete);
        Deletemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
       

        Intent intent = getIntent();//리사이클러뷰 (Adapter에서 인텐트 가져오기)
        //각 키워드로  xml텍스트값 변경
        String str_prodouct = intent.getStringExtra("product");
        product.setText(str_prodouct);

        String str_price= intent.getStringExtra("price");
        price.setText(String.valueOf(str_price));

        String str_name = intent.getStringExtra("name");
        name.setText(str_name);

        String str_etc = intent.getStringExtra("etc");
        etc.setText(str_etc);

        final boolean itsmine = intent.getBooleanExtra("itsmine",false);//마이페이지를 통해 들어가면 true
        final boolean myself = intent.getBooleanExtra("myself",false); //자신이 등록한 글이면 ture :실제동작 : 마페를 통해 들어가면

        final String str_postid = intent.getStringExtra("postid");
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Posts").child(str_postid);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Posts post = dataSnapshot.getValue(Posts.class);
                val_forsale = post.getForsale();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        ref.addValueEventListener(postListener);

        if(val_forsale != 0 && !itsmine ){//판매중이거나, 자신이 구매하거나 올린 글이 아닐 때.
            nomorebuy();
        }
        if(val_forsale != 0 && !myself) {//판매중이거나, 자신이 구매하거나 올린 글이 아닐 때.
            nomorebuy();
        }
        // 출처: https://debugdaldal.tistory.com/110 [달달한 디버깅]



        // 인텐트에 있는 이미지는 스트링 형태 URL 주소에서 사진 가져옴
        final String url_postimage = intent.getStringExtra("postimage");
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(url_postimage);

        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(DetailPost.this)
                            .load(task.getResult())
                            .override(1024, 980)
                            .into(postimage);

                } else {
                    Toast.makeText(DetailPost.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo = new Intent(DetailPost.this, PhotoViewActivity.class);
                photo.putExtra("postimage", url_postimage);
                startActivity(photo);
            }
        });


        Button buy = (Button) findViewById(R.id.buy_btn) ;
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buy_btn:
                        if (itsmine) {
                            Toast.makeText(DetailPost.this, "이미 구매한 제품입니다.", Toast.LENGTH_SHORT).show();
                        } else if(myself){
                            Toast.makeText(DetailPost.this, "당신의 판매글입니다.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(DetailPost.this, "제품 정보를 충분히 확인한 후 결정해주세요.", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetailPost.this);
                            builder.setTitle("구매");
                            builder.setMessage("구매하시겠습니까?\n이후 마이페이지에서 판매자의 계좌정보를 확인할 수 있습니다.");
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //파이어베이스 작업 : 올리기
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference saleRef = database.getReference("Posts").child(str_postid).child("forsale");
                                    saleRef.setValue(1);//판매중이거나 판매된 글로 변경
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("purchased");
                                    final String purchased = reference.push().getKey();

                                    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                                    String email = user.getEmail();

                                    int idx = email.indexOf("@");

                                    String mailid = email.substring(0, idx);


                                    //purchasded 저장 코드
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                /*DatabaseReference moviesRef = rootRef.child("users").child(mailid).child("purchased").child(purchased);
                                moviesRef.setValue(purchased);*/
                                    DatabaseReference moviesRef = rootRef.child("users").child(mailid).child("purchased").child(str_postid);
                                    moviesRef.setValue(str_postid);
                                    Toast.makeText(DetailPost.this, "구매가 완료되었습니다. 마이페이지의 구매자 메뉴를 확인해주세요.", Toast.LENGTH_LONG).show();

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
            }
        });

        jjim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailPost.this, "찜 기능은 추후 업데이트 됩니다.",Toast.LENGTH_SHORT).show();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*DatabaseReference myRef = database.getReference("chats").child(str_postid);
                myRef.setValue()*/

                Toast.makeText(DetailPost.this, "채팅 구현 *_*",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailPost.this, ChatActivity.class);
                intent.putExtra("chatid", str_postid);
                startActivity(intent);
            }
        });

    }//onCreate
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.delete_menu, popup.getMenu());
        popup.show();
    }
public void nomorebuy(){
    //TODO : 레이아웃 인플레이팅
    //레이아웃을 위에 겹쳐서 올리는 부분
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //레이아웃 객체생성
    LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.activity_not_for_sale, null);
    //레이아웃 배경 투명도 주기
    ll.setBackgroundColor(Color.parseColor("#99000000"));
    //레이아웃 위에 겹치기
    LinearLayout.LayoutParams paramll = new LinearLayout.LayoutParams
            (LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    addContentView(ll, paramll);
    ll.setClickable(true);
    //위에겹친 레이아웃에 온클릭 이벤트주기
}

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater= getMenuInflater();
       inflater.inflate(R.menu.delete_menu, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.modify:
                Toast.makeText(this, "menu1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "menu2", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
}