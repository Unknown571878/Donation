package com.example.donation.etc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class ChangeHandphone extends AppCompat{
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseUser User;              // 파이어 베이스 유저
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private EditText edt1,edt2, edt3;
    private Button btn1, btn2;
    String phone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_handphone);

        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 인스턴스
        User = mFirebaseAuth.getCurrentUser();      // 파이어베이스 유저 확인
        mDatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터 베이스 인스턴스
        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid()); // Path 지정

        edt1 = (EditText) findViewById(R.id.change_hp_edt1);    // 현재 휴대폰 번호
        edt2 = (EditText) findViewById(R.id.change_hp_edt2);    // 새 휴대폰 번호
        edt3 = (EditText) findViewById(R.id.change_hp_edt3);    // 새 휴대폰 번호 확인
        btn1 = (Button) findViewById(R.id.change_hp_btn1);      // 변경
        btn2 = (Button) findViewById(R.id.change_hp_btn2);      // 취소

        // 현재 휴대폰 번호
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if(account != null){
                    phone = account.getUserPhone();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 변경
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt1.getText().toString().length() == 0 || !edt1.getText().toString().equals(phone)) {
                    Toast.makeText(ChangeHandphone.this, "현재 전화번호를 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (edt2.getText().toString().length() != 11) {
                    Toast.makeText(ChangeHandphone.this, "바꿀 전화번호를 11자리 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (!edt3.getText().toString().equals(edt2.getText().toString())) {
                    Toast.makeText(ChangeHandphone.this, "전화번호를 재확인 하세요.", Toast.LENGTH_SHORT).show();
                } else if (phone.equals(edt1.getText().toString()) && edt2.getText().toString().equals(edt3.getText().toString())) {
                    String ch_phone = edt2.getText().toString();
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("userPhone",ch_phone);
                    mDatabaseRef.updateChildren(taskMap);
                    Toast.makeText(ChangeHandphone.this, "전화번호가 변경 됬습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(ChangeHandphone.this, "전화번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 취소
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangeHandphone.this, "비밀번호 변경을 취소 했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // 뒤로가기
        ImageView chp_tb_btn = (ImageView) findViewById(R.id.change_handphone_tb_btn);
        chp_tb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
