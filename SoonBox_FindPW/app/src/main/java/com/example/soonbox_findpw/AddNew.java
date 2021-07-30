package com.example.soonbox_findpw;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class AddNew extends AppCompatActivity {
//등록 변수
    int run = 1;
    //view들을 업로드 함수에서 사용할 변수
    Uri imageUri;
    String myUrl= "";
    Button done;
    StorageTask uploadTask;
    StorageReference storageReference;
    //받아올 데이터 변수
    ImageView new_image;
    EditText product;
    EditText price;
    EditText name;
    EditText account;
    EditText etc;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        done = findViewById(R.id.new_done);
        new_image = findViewById(R.id.new_image);
        product = findViewById(R.id.new_product);
        price = findViewById(R.id.new_price);
        name = findViewById(R.id.new_name);
        account = findViewById(R.id.new_account);
        etc = findViewById(R.id.new_etc);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //FirebaseUser user = firebaseAuth.getCurrentUser();
        //myemail= user.getEmail();




        new_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                storageReference = FirebaseStorage.getInstance().getReference("Posts");// 6:33

                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .start(AddNew.this);//크롭 라이브러리 추가
            }
        });

    }//OnCreate



    //크롭한 이미지 받아서 이미지 새로 고쳐줌
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                 imageUri = result.getUri();

                new_image.setImageURI(imageUri);
            }
        }else {
            Toast.makeText(this, "crop한 이미지 저장 중 오류가 발생했습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("등록중");
        progressDialog.show();//로딩

        if (imageUri != null){
            final StorageReference filereference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadTask = filereference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");//저장 경로
                        //DatabaseReference userreference= FirebaseDatabase.getInstance().getReference("users");

                        final String postid = reference.push().getKey();
                        //final String userid= userreference.


                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postimage", myUrl);
                        hashMap.put("product", product.getText().toString());
                        hashMap.put("price", price.getText().toString());
                        hashMap.put("name", name.getText().toString());
                        hashMap.put("account", account.getText().toString());
                        hashMap.put("etc", etc.getText().toString());
                        hashMap.put("forsale", 0);
                        //hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(postid).setValue(hashMap);

                        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                        String email= user.getEmail();

                        int idx = email.indexOf("@");

                        String mailid = email.substring(0, idx);


                        //uploaded 저장 코드
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        //DatabaseReference moviesRef = rootRef.child("users").child(stuentnumber.getText().toString()).child("uploaded").child(postid);
                        DatabaseReference moviesRef = rootRef.child("users").child(mailid).child("uploaded").child(postid);
                        moviesRef.setValue(postid);


                        progressDialog.dismiss();
                        startActivity(new Intent(AddNew.this, MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(AddNew.this, "실패!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNew.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "이미지가 선택되지 않았어요!", Toast.LENGTH_SHORT).show();
            run = 0;
        }
    }



    // 배경 누르면 키보드 내리기
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
    // 출처: https://hooops.tistory.com/entry/안드로이드-스튜디오-키보드-내리기-배경터치이벤트 [오묘한색]

    @Override//시스템 백키 눌렸을 때
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("작성중인 내용을 저장하지 않고 나가시겠습니까?");
        builder.setPositiveButton("그만 쓸래요", dialogListner);
        builder.setNegativeButton("아니요! 마저 쓸래요!", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    DialogInterface.OnClickListener dialogListner = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        } //TODO : 아래 메소드랑 중복되는것 같>>
    };

    public void btn_back(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("작성중인 내용을 저장하지 않고 나가시겠습니까?");
        builder.setPositiveButton("그만 쓸래요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();//등록 창 닫힘
            }
        });
        builder.setNegativeButton("아니요! 마저 쓸래요!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    /*private void writeNewUser(String uploaded) {

        UploadLink uploadLink = new UploadLink(uploaded);
        mDatabase.child("users").push().setValue(uploadLink)
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

    public void btn_done (View v){ // 등록 완료 버튼
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("글 올리기(완료)");
        builder.setMessage("정보를 제대로 입력하셨나요?");

        builder.setPositiveButton("네!", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadImage();

                //TODO : 서버에 등록

                //TODO : 글 등록하고 핸들러 처리해서 글이 등록되기 전에 홈으로 가는 것을 막기. 안막으면 등록 :run 변수 1= 진행 0 = 오류.

            }
        });
        builder.setNegativeButton("악! 아니요!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"입력한 정보를 다시 확인해주세요", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }//등록 완료 버튼

public void dismiss(){
    Intent intent = new Intent(AddNew.this, MainActivity.class);
    startActivity(intent);
    finish();//등록 창 닫힘
}


}