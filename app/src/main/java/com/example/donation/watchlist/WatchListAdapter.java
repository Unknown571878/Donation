package com.example.donation.watchlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donation.R;
import com.example.donation.listofwriting.ListOfWritingItem;
import com.example.donation.writing.WritingViewActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

interface OnItemClickListener {
    void onItemClick(View view, int position);
}
    public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.CustomViewHolder> {
    private ArrayList<WatchListItem> arrayList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser User;              // 파이어 베이스 유저
    private DatabaseReference databaseReference;
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    public WatchListAdapter(ArrayList<WatchListItem> arrayList, Context context){
            this.arrayList = arrayList;
            this.context = context;
            }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
            }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_writing_item, parent, false);
            CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
            }

    @Override
    public void onBindViewHolder(@NonNull WatchListAdapter.CustomViewHolder holder, int position) {
            holder.tv_title.setText(arrayList.get(position).getTitle());
            holder.tv_money.setText(String.valueOf(arrayList.get(position).getMoney()));
            holder.tv_dateandtime.setText(arrayList.get(position).getDateAndTime());
            holder.tv_userName.setText(arrayList.get(position).getUserName());
            holder.tv_viewcount.setText(String.valueOf(arrayList.get(position).getClickCount()));
            holder.tv_img.setText(arrayList.get(position).getImg());
            holder.tv_listid.setText(arrayList.get(position).getKey());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    firebaseFirestore = FirebaseFirestore.getInstance();
                    String key = arrayList.get(holder.getAdapterPosition()).getKey();
                    // 뷰 업데이트

                    Intent intent = new Intent(view.getContext(), WatchListView.class);
                    intent.putExtra("title", arrayList.get(holder.getAdapterPosition()).getTitle());
                    intent.putExtra("money", String.valueOf(arrayList.get(holder.getAdapterPosition()).getMoney()));
                    intent.putExtra("dateandtime", arrayList.get(holder.getAdapterPosition()).getDateAndTime());
                    intent.putExtra("detail", arrayList.get(holder.getAdapterPosition()).getDetail());
                    intent.putExtra("username",arrayList.get(holder.getAdapterPosition()).getUserName());
                    intent.putExtra("img", arrayList.get(holder.getAdapterPosition()).getImg());
                    intent.putExtra("listid", arrayList.get(holder.getAdapterPosition()).getKey());

                    view.getContext().startActivity(intent);
                    }
                });
            }

    @Override
    public int getItemCount() {
            return (arrayList != null ? arrayList.size() : 0);
            }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;  // 제목
        TextView tv_money;  // 기부금액
        TextView tv_dateandtime;    // 시간
        TextView tv_userName;   // 유저 이름
        TextView tv_viewcount;  // 조회수
        TextView tv_img;        // 이미지
        TextView tv_listid;     // 리스트 키

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.iv_title);
            this.tv_money = itemView.findViewById(R.id.iv_money);
            this.tv_dateandtime = itemView.findViewById(R.id.iv_dateandtime);
            this.tv_userName = itemView.findViewById(R.id.iv_userName);
            this.tv_img = itemView.findViewById(R.id.iv_img);
            this.tv_listid = itemView.findViewById(R.id.iv_listid);
        }
    }
}
