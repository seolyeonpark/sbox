package com.example.soonbox_findpw;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class Frag5 extends Fragment implements View.OnClickListener{

    private static final String TAG = "Frag5";

    //firebase auth object
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    //view objects
    private TextView textViewUserEmail; private TextView textivewDelete; private TextView user_name;
    private TextView user_number;
    private Button buttonLogout;
    private ImageView profile;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private  StorageReference storageReference;
    private String  imagelink;



    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email= user.getEmail();

    int idx = email.indexOf("@");

    String mailid = email.substring(0, idx);


    Button btn_q;
    Button btn_seller;
    Button btn_order;


    @Override
    public void onCreate (@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance(); //파이어베이스 DB연동
        databaseReference = database.getReference("users");//DB테이블 연결
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag5, container, false);

        textViewUserEmail = (TextView) view.findViewById(R.id.textviewUserEmail);
        buttonLogout = (Button) view.findViewById(R.id.buttonLogout);
        textivewDelete = (TextView) view.findViewById(R.id.textviewDelete);
        user_name=(TextView) view.findViewById(R.id.user_name);
        user_number=(TextView)view.findViewById(R.id.user_number);
        profile = (ImageView)view.findViewById(R.id.profile);

        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);

       /* databaseReference = database.getReference("users").child(mailid).child("profileimage").child("profileimage");
        ValueEventListener profileListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
               imagelink = user.getProfileimage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(profileListener);*/



       // StorageReference ref =gs://soonbox-c3b0b.appspot.com/users/tjswn0125/profileimage/1606202746211.null
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://soonbox-c3b0b.appspot.com/users").child(mailid).child("profileimage/image.null");
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(Frag5.this)
                            .load(task.getResult())
                            .override(1024, 980)
                            .into(profile);

                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileSettingActivity.class);
                intent.putExtra("mailid",mailid);
                startActivity(intent);
            }
        });

        textViewUserEmail.setText(user.getEmail());

        btn_q = (Button)view.findViewById(R.id.btn_q);

        btn_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), FAQActivity.class);
                startActivity(intent);
            }


        });

        btn_order = (Button)view.findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), Purc_application.class);
                //intent.putExtra("getPWuser", mailid); //파베 경로를 위한 유저 아이디
                startActivity(intent);
            }


        });

        btn_seller = (Button)view.findViewById(R.id.btn_seller);
        btn_seller.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), Purc_order.class);
                //intent.putExtra("getPWuser", mailid); //파베 경로를 위한 유저 아이디
                startActivity(intent);
            }

        });



        return view;}//onCreateView

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            Toast.makeText(getActivity(), "앱에서 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        //회원탈퇴를 클릭하면 회원정보를 삭제한다. 삭제전에 컨펌창을 하나 띄워야 겠다.
        if(view == textivewDelete) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
            alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getActivity(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                            getActivity().finish();
                                            startActivity(new Intent(getContext(), LoginActivity.class));
                                        }
                                    });
                        }
                    }
            );
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(), "취소", Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
        }
    }}