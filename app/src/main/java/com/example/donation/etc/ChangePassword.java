package com.example.donation.etc;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.donation.R;
import com.example.donation.login.UserAccout;
import com.example.donation.option.OptionActivity;
import com.example.donation.profile_pg;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompat{
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseUser User;              // 파이어 베이스 유저
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private EditText edt1, edt2, edt3;
    private Button btn1, btn2;
    private String pw1, pw2, pw3;
    private ImageView cpw_tb_btn;           // 뒤로가기

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 인스턴스
        User = mFirebaseAuth.getCurrentUser();      // 파이어베이스 유저 확인
        mDatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터 베이스 인스턴스
        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid()); // Path 지정

        edt1 = (EditText) findViewById(R.id.change_pw_edt1);
        edt2 = (EditText) findViewById(R.id.change_pw_edt2);
        edt3 = (EditText) findViewById(R.id.change_pw_edt3);
        btn1 = (Button) findViewById(R.id.change_pw_btn1);
        btn2 = (Button) findViewById(R.id.change_pw_btn2);
        
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if(account.getUserNickName().length() == 0){
                    pw1 = account.getUserPW();
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
                if(edt1.getText().toString().length() == 0){
                    Toast.makeText(ChangePassword.this, "현재 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }else if (edt2.getText().toString().length() == 0){
                    Toast.makeText(ChangePassword.this, "바꿀 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (edt3.getText().toString().length() ==0) {
                    Toast.makeText(ChangePassword.this, "비밀번호를 재확인 하세요.", Toast.LENGTH_SHORT).show();
                } else if (pw1.equals(edt1.getText().toString()) && edt2.getText().toString().equals(edt3.getText().toString())) {
                    User.updatePassword(edt2.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                    }
                                }
                            });
                    String pwd = edt2.getText().toString();
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("userPW",pwd);
                    mDatabaseRef.updateChildren(taskMap);
                    Toast.makeText(ChangePassword.this, "비밀번호가 변경 됬습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(ChangePassword.this, "비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 취소
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangePassword.this, "비밀번호 변경을 취소 했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // 이전 화면 가기
        cpw_tb_btn = (ImageView) findViewById(R.id.change_pw_tb_btn);
        cpw_tb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
