package com.example.donation.etc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.donation.R;
import com.example.donation.login.UserAccout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangeNickName extends AppCompat{
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseUser User;              // 파이어 베이스 유저
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private EditText edt2;
    private Button btn1;
    private ImageView ch_na_back;           // 뒤로가기

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_nickname);

        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 인스턴스
        User = mFirebaseAuth.getCurrentUser();      // 파이어베이스 유저 확인
        mDatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터 베이스 인스턴스
        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid()); // Path 지정

        edt2 = (EditText) findViewById(R.id.change_name_edt2);  // 닉네임
        btn1 = (Button) findViewById(R.id.change_nickname_btn1);    // 변경 확인 버튼

        // 현재 이름, 닉네임 할당
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if (account != null) {
                    edt2.setText(account.getUserNickName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 닉네임 변경
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NickName = edt2.getText().toString();
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("userNickName", NickName);
                mDatabaseRef.updateChildren(taskMap);
                finish();
            }
        });

        // 뒤로 가기 버튼
        ch_na_back = (ImageView) findViewById(R.id.change_nickname_tb_btn);
        ch_na_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
