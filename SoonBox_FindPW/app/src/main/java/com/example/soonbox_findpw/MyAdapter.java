package com.example.soonbox_findpw;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;
    List<Chat> mChat;
    String stEmail;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;

    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    String email= user.getEmail();

    int idx = email.indexOf("@");

    String mailid = email.substring(0, idx);

    String id;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.mTextView);
        }
    }


    public MyAdapter(List<Chat> mChat, String email) {
        this.mChat = mChat;
        this.stEmail=email;
    }

    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getEmail().equals(stEmail)){
            id = mailid;
            return 1;
        }else{
            idx = mChat.get(position).getEmail().indexOf("@");
            id = mChat.get(position).getEmail().substring(0, idx);
            return 2;
        }
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v;




        if (viewType == 1) {
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_text_view, parent, false);
            final CircleImageView iv = v.findViewById(R.id.iv);
            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://soonbox-c3b0b.appspot.com/users").child(id).child("profileimage/image.null");
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(v).load(task.getResult()).into(iv);
                }
            });
        } else {
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
            final CircleImageView iv = v.findViewById(R.id.iv);
            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://soonbox-c3b0b.appspot.com/users").child(id).child("profileimage/image.null");
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(v).load(task.getResult()).into(iv);
                }
            });
        }




        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //View view;
        holder.mTextView.setText(mChat.get(position).getText());
        //CircleImageView iv= view.findViewById(R.id.iv);
        //Glide.with(view).load(item.getPofileUrl()).into(iv);


    }


    @Override
    public int getItemCount() {
        return mChat.size();
    }
}