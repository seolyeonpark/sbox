package com.example.soonbox_findpw;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PhotoViewActivity extends AppCompatActivity {
    PhotoView photoview;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        final Intent intent = getIntent();

        close = findViewById(R.id.btn_close);
        photoview = (PhotoView) findViewById(R.id.photo_view);

        String url_postimage = intent.getStringExtra("postimage");
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(url_postimage);

        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(PhotoViewActivity.this)
                            .load(task.getResult())
                            .override(1024, 980)
                            .into(photoview);
                } else {
                    Toast.makeText(PhotoViewActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}