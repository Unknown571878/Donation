package com.example.donation.point;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donation.R;
import com.example.donation.listofwriting.ListOfWritingAdapter;
import com.example.donation.listofwriting.ListOfWritingItem;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PointListAdapter extends RecyclerView.Adapter<PointListAdapter.CustomViewHolder> {
    private ArrayList<PointListItem> arrayList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser User;              // 파이어 베이스 유저
    private DatabaseReference databaseReference;
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    public PointListAdapter(ArrayList<PointListItem> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.point_home_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PointListAdapter.CustomViewHolder holder, int position) {
        holder.tv_Btime.setText(arrayList.get(position).getTime());
        holder.tv_Bpoint.setText(arrayList.get(position).getPoint());
        holder.tv_Btitle.setText(arrayList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {return (arrayList != null ? arrayList.size() : 0);}

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Bpoint;
        TextView tv_Btitle;
        TextView tv_Btime;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_Bpoint = itemView.findViewById(R.id.donation_point);
            this.tv_Btitle = itemView.findViewById(R.id.donation_title);
            this.tv_Btime = itemView.findViewById(R.id.donation_time);
        }
    }
}
