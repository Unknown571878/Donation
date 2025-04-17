package com.example.donation.login;

import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindIdPassword extends AppCompat {
    private Button find_pwd_ck_btn;
    private EditText pwd_edt;
    private ImageView tb_img;
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id_password);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.setLanguageCode("ko_KR");
        find_pwd_ck_btn = (Button) findViewById(R.id.find_pwd_ck_btn);
        pwd_edt = (EditText) findViewById(R.id.find_pwd_edt);
        tb_img = (ImageView) findViewById(R.id.find_id_password_tb_img);

        // 비밀번호 찾기 확인
        find_pwd_ck_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = pwd_edt.getText().toString();
                if(email.length() != 0){
                    mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "Email send");
                                Toast.makeText(FindIdPassword.this, "비밀번호 재설정 이메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "아이디가 틀렸습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tb_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
