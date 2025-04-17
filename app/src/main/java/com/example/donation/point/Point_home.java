package com.example.donation.point;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.listofwriting.ListOfWritingAdapter;
import com.example.donation.listofwriting.ListOfWritingItem;
import com.example.donation.login.UserAccout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Point_home extends AppCompat {
    ImageView tb_back;  // 툴바 튀로가기
    private FirebaseAuth mFirebaseAuth;         // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터 베이스
    private FirebaseUser User;              // 파이어 베이스 유저
    private FirebaseDatabase database;
    private RecyclerView pointList;
    private RecyclerView.Adapter adapter;
    private  RecyclerView.LayoutManager layoutManager;
    private ArrayList<PointListItem> arrayList;
    private TextView Mypoint;
    SwipeRefreshLayout point_swiperefreshlayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_home);

        // 툴바 튀로가기 버튼
        Mypoint = findViewById(R.id.mypoint);
        tb_back = (ImageView) findViewById(R.id.point_home_tb_btn);
        tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pointList = findViewById(R.id.point_list);
        pointList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        pointList.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 인스턴스
        User = mFirebaseAuth.getCurrentUser();      // 파이어베이스 유저 확인

        mDatabaseRef = database.getReference("donation").child("UserAccount")
                .child(User.getUid()).child("Donation List");
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PointListItem item = snapshot.getValue(PointListItem.class);
                    arrayList.add(0, item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Point_home", String.valueOf(error.toException()));
            }
        });
        adapter = new PointListAdapter(arrayList, this);
        pointList.setAdapter(adapter);

        mDatabaseRef = database.getReference("donation").child("UserAccount")
                .child(User.getUid());
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if(account != null){
                    Mypoint.setText(String.valueOf(account.getUserPoint()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        point_swiperefreshlayout = findViewById(R.id.point_swiperefreshlayout);
        point_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                overridePendingTransition(0,0);
                finish();
                startActivity(intent);
                overridePendingTransition(0,0);
                Toast.makeText(getApplicationContext(),"새로고침 하였습니다.", Toast.LENGTH_SHORT).show();
                point_swiperefreshlayout.setRefreshing(false);
            }
        });
    }
}
