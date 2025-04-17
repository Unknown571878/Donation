package com.example.donation.Talent_Chat_;

import android.content.Context;

import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.donation.R;
import com.example.donation.profile_pg;
import com.example.donation.writing.WritingViewActivity;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.net.URI;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomViewHolder>{
    private ArrayList<ChatData> arrayList;
    private Context context;
    private String chat_Nickname;
    StorageReference storageReference;
    public ChatAdapter(ArrayList<ChatData> arrayList, Context context, String chat_Nickname){
        this.arrayList = arrayList;
        this.context = context;
        this.chat_Nickname = chat_Nickname;

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.talent_chat, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }
    private void setAlignment(CustomViewHolder holder, int gravity) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.chat_msg.getLayoutParams();
        params.gravity = gravity;
        holder.chat_msg.setLayoutParams(params);
    }
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.CustomViewHolder holder, int position) {
        ChatData chat = arrayList.get(position);
        holder.chat_nickname.setText(chat.getNickname());

        // 메시지가 비어 있지 않은지 확인
        if (chat.getMsg().length() == 0) {
            holder.chat_msg.setVisibility(View.GONE);
        } else {
            holder.chat_msg.setVisibility(View.VISIBLE);
            holder.chat_msg.setText(chat.getMsg());
        }

        holder.chat_time.setText(chat.getTime());

        // 이미지 URL이 비어 있지 않은지 확인
        if (chat.getImg().length() != 0) {
            storageReference = FirebaseStorage.getInstance().getReference("chat");
            String childName = chat.getUserId() + "/" + chat.getKey();

            storageReference.child(childName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Glide를 사용하여 이미지를 ImageView에 로드
                    Glide.with(context).load(uri).override(500,500).into(holder.chat_img);
                }
            });
        } else {
            // 이미지가 없으면 ImageView를 지우도록 설정
            holder.chat_img.setImageDrawable(null);
        }

        if (chat.getNickname().equals(chat_Nickname)) {
            holder.chat_msg.setBackgroundResource(R.drawable.mychat);
            setAlignment(holder, Gravity.END);
        } else {
            holder.chat_msg.setBackgroundResource(R.drawable.youchat);
            setAlignment(holder, Gravity.START);
        }
    }

    @Override
    public int getItemCount() {return (arrayList != null ? arrayList.size() : 0);}

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView chat_nickname;
        TextView chat_msg;
        TextView chat_time;
        ImageView chat_img;
        TextView chat_userid;
        TextView chat_id;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.chat_msg = itemView.findViewById(R.id.chat_msg);
            this.chat_nickname = itemView.findViewById(R.id.chat_nickname);
            this.chat_time = itemView.findViewById(R.id.chat_time);
            this.chat_img = itemView.findViewById(R.id.chat_img);   // 이미지
            this.chat_userid = itemView.findViewById(R.id.chat_usesrid);
            this.chat_id = itemView.findViewById(R.id.chat_id);
            this.chat_msg.setLayoutParams(params);
            this.chat_nickname.setLayoutParams(params);
            this.chat_time.setLayoutParams(params);
            this.chat_userid.setLayoutParams(params);
            this.chat_img.setLayoutParams(params);
            this.chat_id.setLayoutParams(params);
        }
    }
    public void addChat(ChatData chat) {
        arrayList.add(chat);
        notifyItemInserted(arrayList.size() - 1);

    }
}
