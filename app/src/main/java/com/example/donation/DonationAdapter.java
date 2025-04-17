package com.example.donation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donation.listofwriting.ListOfWritingItem;
import com.example.donation.point.PointListAdapter;
import com.example.donation.point.PointListItem;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.CustomViewHolder>{
    private ArrayList<DonationListItem> arrayList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser User;              // 파이어 베이스 유저
    private DatabaseReference databaseReference;
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    public DonationAdapter(ArrayList<DonationListItem> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.main_title.setText(arrayList.get(position).getTitle());
        holder.main_point.setText(arrayList.get(position).getPoint());
        holder.main_nickname.setText(arrayList.get(position).getNickname());
        holder.main_time.setText(arrayList.get(position).getTime());
    }

    @Override
    public int getItemCount() {return (arrayList != null ? arrayList.size() : 0);}

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView main_title, main_time, main_point, main_nickname;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this. main_title = itemView.findViewById(R.id.main_donation_title);
            this.main_point = itemView.findViewById(R.id.main_donation_point);
            this.main_time = itemView.findViewById(R.id.main_donation_time);
            this.main_nickname = itemView.findViewById(R.id.main_donation_nickname);
        }
    }
}
