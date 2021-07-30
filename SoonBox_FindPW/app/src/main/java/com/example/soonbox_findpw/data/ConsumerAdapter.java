package com.example.soonbox_findpw.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soonbox_findpw.DetailPost;
import com.example.soonbox_findpw.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConsumerAdapter extends RecyclerView.Adapter<ConsumerAdapter.CustomViewHolder>{
    FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
    DatabaseReference  databaseRef;
    private String banknumber;
    private String username;
    private ArrayList<Posts> arrayList;
    private Context context;
    private String postid;



    public ConsumerAdapter(ArrayList<Posts> arrayList, Context context, String postid) {  /*String mailid*/
        this.arrayList = arrayList;
        this.context = context;
        //this.randompw = randompw;
        this.postid = postid;



    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purc_application_two, parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;


       //파이어베이스 DB연동


    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getPostimage())
                .into(holder.iv_thumbnail);
        holder.tv_product.setText(arrayList.get(position).getProduct());
        holder.tv_price.setText(String.valueOf(arrayList.get(position).getPrice()));
        holder.bv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // 비번 확인 버튼 누르면 수행 할 명령
               //랜덤 비번 넣을 변수

                AlertDialog.Builder builder = new AlertDialog.Builder(context); //context == Purc_aplication
                banknumber = arrayList.get(position).getAccount();
                username=arrayList.get(position).getName();
                builder.setTitle("구매");
                builder.setMessage(username+" 계좌번호는 "+ banknumber +" 입니다.\n확인 완료시 예 버튼을 눌러주세요.");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show(); //팝업창 보여줌
                //이 부분은 확인용으로 자신의 pw를 가져오는 것이지만, 이후 Posts.class에 pw를 추가하고 DB - Posts - A12234 - 'postpw'를 추가하는 방식으로 해서 불러오면 될 것 같습니다.
            }
        });
        holder.itemView.setTag(position);
        postid = arrayList.get(position).getPostid();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailPost.class);
                intent.putExtra("product",arrayList.get(position).getProduct());
                intent.putExtra("account",arrayList.get(position).getAccount());
                intent.putExtra("etc",arrayList.get(position).getEtc());
                intent.putExtra("name",arrayList.get(position).getName());
                intent.putExtra("postid",arrayList.get(position).getPostid());
                intent.putExtra("postimage",arrayList.get(position).getPostimage());
                intent.putExtra("price",arrayList.get(position).getPrice());
                intent.putExtra("username",arrayList.get(position).getUsername());
                intent.putExtra("forsale",arrayList.get(position).getForsale());
                intent.putExtra("itsmine", true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_price;
        TextView tv_product;
        Button bv_button;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_thumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
            this.tv_price = (TextView) itemView.findViewById(R.id.apply_price);
            this.tv_product = (TextView) itemView.findViewById(R.id.apply_product);
            this.bv_button = (Button) itemView.findViewById(R.id.info_account);
        }
    }

}