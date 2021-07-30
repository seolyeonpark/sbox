package com.example.soonbox_findpw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class ProfileSettingActivity extends AppCompatActivity {
    int run=1;
    Uri imageUri;
    String myUrl= "";
    StorageTask uploadTask;
    ImageView setimage;
    Button change;
    //String mailid;
    StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    String email= user.getEmail();
    int idx = email.indexOf("@");

    String mailid = email.substring(0, idx);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        //Intent intent = getIntent();
        //mailid = intent.getStringExtra("mailid");



        setimage=findViewById(R.id.setimage);
        setimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                storageReference = FirebaseStorage.getInstance().getReference("users/"+mailid+"/profileimage");// 6:33

                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .start(ProfileSettingActivity.this);//크롭 라이브러리 추가
            }
        });




        change = (Button)findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                uploadImage();

            }
        });

    }//create

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();

                setimage.setImageURI(imageUri);
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
            final StorageReference filereference = storageReference.child("image." + getFileExtension(imageUri));

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

                        //실시간 DB에도 연동
                        myUrl = downloadUri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(mailid).child("profileimage");//저장 경로
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("profileimage", myUrl);
                        reference.setValue(hashMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(ProfileSettingActivity.this, MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(ProfileSettingActivity.this, "실패!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileSettingActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "이미지가 선택되지 않았어요!", Toast.LENGTH_SHORT).show();
            run=0;
        }
    }
}