package com.example.soonbox_findpw.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soonbox_findpw.DetailPost;
import com.example.soonbox_findpw.R;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewholder> {

    private ArrayList<Posts> arrayList;
    private Context context; //액티비티에서 콘텍스트 가져옴


    public CustomAdapter(ArrayList<Posts> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public class CustomViewholder extends RecyclerView.ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_product;
        TextView tv_price;
        TextView tv_username;

        public CustomViewholder(@NonNull View itemView) {
            super(itemView);
            this.iv_thumbnail = itemView.findViewById(R.id.iv_thumbnail);
            this.tv_product = itemView.findViewById(R.id.tv_product);
            this.tv_price = itemView.findViewById(R.id.tv_price);
            this.tv_username = itemView.findViewById(R.id.tv_username);
        }
    }

    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_post, parent,false);
        CustomViewholder holder = new CustomViewholder(view);
        return holder;
    }
    //각 아이템 매칭
    @Override
    public void onBindViewHolder(CustomViewholder holder, final int position) { //항목 구성

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getPostimage())
                .into(holder.iv_thumbnail);

        holder.tv_product.setText(arrayList.get(position).getProduct()); //getid()
        holder.tv_price.setText(String.valueOf(arrayList.get(position).getPrice()));
        holder.tv_username.setText(arrayList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Context context = v.getContext();

                Intent intent = new Intent(v.getContext(), DetailPost.class);

                intent.putExtra("product",arrayList.get(position).getProduct());
                intent.putExtra("account",arrayList.get(position).getAccount());
                intent.putExtra("etc",arrayList.get(position).getEtc());
                intent.putExtra("name",arrayList.get(position).getName());
                intent.putExtra("postid",arrayList.get(position).getPostid());
                intent.putExtra("postimage",arrayList.get(position).getPostimage());
                intent.putExtra("price",arrayList.get(position).getPrice());
                intent.putExtra("username",arrayList.get(position).getUsername());
                intent.putExtra("forsale",arrayList.get(position).getForsale());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }


}
