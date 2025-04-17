package com.example.donation.etc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.donation.R;
import com.example.donation.login.UserAccout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeEmail extends AppCompat{
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseUser User;              // 파이어 베이스 유저
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private ImageView cem_tb_btn;           // 뒤로가기
    private TextView em_tv1;
    private EditText edt1, edt2;
    private Button btn1, btn2, btn3;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email);

        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 인스턴스
        User = mFirebaseAuth.getCurrentUser();      // 파이어베이스 유저 확인
        mDatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터 베이스 인스턴스
        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid()); // Path 지정

        em_tv1 = (TextView) findViewById(R.id.change_em_tv1);   // 현재 이메일 주소
        edt1 = (EditText) findViewById(R.id.change_em_edt1);    // 변경할 이메일 주소
        edt2 = (EditText) findViewById(R.id.change_em_edt2);    // 인증 번호
        btn1 = (Button) findViewById(R.id.change_em_btn1);      // 인증 번호 다시 받기
        btn2 = (Button) findViewById(R.id.change_em_btn2);      // 인증 번호 받기
        btn3 = (Button) findViewById(R.id.change_em_btn3);      // 인증 확인

        edt2.setVisibility(View.GONE);  // 레이아웃 숨김 (영역까지 없앰)
        btn1.setVisibility(View.GONE);  // 레이아웃 숨김 (영역까지 없앰)
        btn3.setVisibility(View.GONE);  // 레이아웃 숨김 (영역까지 없앰)

        // 현재 이메일 주소 할당
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if(account != null){
                    em_tv1.setText(account.getUserEmail());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 인증 번호 받기 클릭시
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt2.setVisibility(View.VISIBLE); // 레이아웃 표시
                btn1.setVisibility(View.VISIBLE); // 레이아웃 표시
                btn2.setVisibility(View.GONE);      // 레이아웃 숨김 ( 영역까지 없앰)
                btn3.setVisibility(View.VISIBLE); // 레이아웃 표시
            }
        });

        // 뒤로가기
        cem_tb_btn = (ImageView) findViewById(R.id.change_em_tb_btn);
        cem_tb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
