package com.example.donation.point;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.listofwriting.ListOfWritingActivity;
import com.example.donation.login.UserAccout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonationScore extends AppCompat {
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private FirebaseUser User;              // 파이어 베이스 유저
    private TextView tv_name, tv_score;   // 유저 이름 , 유저 점수
    private ImageView tb_back;  // 툴바 뒤로가기 버튼
    private Button btn1;        // 기부하러 가기 버튼
    int score = 0;
    int total = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donationscore); // 기부 점수 현황

        tv_name = (TextView) findViewById(R.id.donationscore_tv1);  // 유저 이름
        tv_score = (TextView) findViewById(R.id.donationscore_tv2); // 유저 점수
        btn1 = (Button) findViewById(R.id.donationscore_btn1);      // 기부하러 가기 버튼
        tb_back = (ImageView) findViewById(R.id.donationscore_tb);  // 툴바 뒤로가기 버튼
        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        // 유저 이름 가져오기
        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if (account != null) {
                    if (account.getUserNickName().length() == 0) {
                        tv_name.setText(account.getUserName());
                    } else if (account.getUserNickName() == null) {
                        tv_name.setText(account.getUserName());
                    } else {
                        tv_name.setText(account.getUserNickName());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount")
                .child(User.getUid()).child("Donation List");
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PointListItem item = snapshot.getValue(PointListItem.class);
                    if(item.getPostId().equals(User.getUid())){
                    }else{
                        score = Integer.parseInt(item.getPoint());
                        total = total + score;
                    }
                }
                tv_score.setText(String.valueOf(Math.abs(total)));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Point_home", String.valueOf(error.toException()));
            }
        });


        // 기부하러 가기 버튼
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonationScore.this, ListOfWritingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 툴바 뒤로가기
        tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
