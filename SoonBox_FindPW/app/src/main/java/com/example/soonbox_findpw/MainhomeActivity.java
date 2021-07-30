package com.example.soonbox_findpw;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainhomeActivity extends AppCompatActivity {

    EditText et_user_name,et_user_number;

    private DatabaseReference mDatabase;

    //define view objects
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignup;
    Button login;
    TextView textviewSingin;
    TextView textviewMessage;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;

    MainActivity mainActivity= new MainActivity();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainhome);


        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        et_user_name = findViewById(R.id.et_user_name);
        et_user_number=findViewById(R.id.et_user_number);
        buttonSignup= findViewById(R.id.buttonSignup);
        textviewSingin=(TextView)findViewById(R.id.textViewSignin);


        //firebase 정의
        mDatabase = FirebaseDatabase.getInstance().getReference();

        readUser();


        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUserName = et_user_name.getText().toString();
                String getUserNumber=et_user_number.getText().toString();
                String getEmail=editTextEmail.getText().toString();
                String getPw=editTextPassword.getText().toString();
                String getUploaded="";
                String getPurchased = "";

                int idx = getEmail.indexOf("@");

                String mailid = getEmail.substring(0, idx);

                //hashmap 만들기
                HashMap result = new HashMap<>();
                result.put("userName", getUserName);
                result.put("studentnumber", getUserNumber);
                result.put("editTextEmail", getEmail);
                result.put("editTextPassword", getPw);
                result.put("uploaded", getUploaded);
                result.put("purchased", getPurchased);

                //writeNewUser(getUserName,getUserNumber,getEmail,getPw, getUploaded);

                User user = new User(getUserName, getEmail,getUserNumber, getPw,getUploaded, getPurchased);


                mDatabase.child("users").child(mailid).setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Write was successful!
                                Toast.makeText(MainhomeActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Write failed
                                Toast.makeText(MainhomeActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                if(v == buttonSignup) {
                    //TODO
                    registerUser();
                }

                if(v == textviewSingin) {
                    //TODO
                    startActivity(new Intent(MainhomeActivity.this, LoginActivity.class)); //추가해 줄 로그인 액티비티
                }

            }
        });

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        //initializing views

        textviewSingin= (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        progressDialog = new ProgressDialog(this);

        //button click event
        //textviewSingin.setOnClickListener(this);
    }


    /*public void writeNewUser(String userName, String studentnumber, String editTextEmail, String editTextPassword, String uploaded) {

        User user = new User(userName, editTextEmail,studentnumber, editTextPassword,uploaded);


        mDatabase.child("users").child(getEmail).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(MainhomeActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(MainhomeActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    private void readUser(){
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(User.class) != null){
                    User post = dataSnapshot.getValue(User.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                } else {
                    Toast.makeText(MainhomeActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    //String email = editTextEmail.getText().toString().trim();
    //String password = editTextPassword.getText().toString().trim();

    //Firebse creating a new user
    private void registerUser(){
        //사용자가 입력하는 email, password를 가져온다.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        //email과 password가 비었는지 아닌지를 체크 한다.
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }

        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            //에러발생시
                            textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                            Toast.makeText(MainhomeActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

}
