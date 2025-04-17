package com.example.donation;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.example.donation.etc.AppCompat;
import com.example.donation.etc.ChangeEmail;
import com.example.donation.etc.ChangeHandphone;
import com.example.donation.etc.ChangeName;
import com.example.donation.etc.ChangeNickName;
import com.example.donation.etc.ChangePassword;
import com.example.donation.login.UserAccout;
import com.example.donation.writing.WritingViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class profile_pg extends AppCompat {
    ImageView profileEdit;
    Button pf_name_btn,pf_phone_btn, pf_nickname_btn;
    private TextView user_name,user_phone,user_nickname1,user_name2, user_nickname;
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseDatabase database;      // 파이어 베이스 데이터베이스
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private FirebaseUser User;              // 파이어 베이스 유저
    private ImageView profile_tb_btn;   // 뒤로가기
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        profileEdit = findViewById(R.id.profile_ig);
        pf_name_btn = (Button) findViewById(R.id.profile_name_btn);
        pf_phone_btn = (Button) findViewById(R.id.profile_phone_btn);
        pf_nickname_btn = (Button) findViewById(R.id.profile_nickname_btn);
        user_name = (TextView) findViewById(R.id.profile_user_name);
        user_name2 = (TextView) findViewById(R.id.profile_user_name2);
        user_phone = (TextView) findViewById(R.id.profile_user_phone);
        user_nickname1 = (TextView) findViewById(R.id.profile_nickname);
        user_nickname = (TextView) findViewById(R.id.profile_user_nickname);


        // 파이어 베이스 연동, 구현 부분
        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("donation").child("UserAccount").child(User.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile");
        // 내용 넣기
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if( account != null){
                    if(account.getUserNickName().length()==0){
                        user_name.setText(account.getUserName());
                    }else {
                        user_name.setText(account.getUserName());
                    }
                    user_name2.setText(account.getUserName());
                    user_nickname1.setText(account.getUserNickName());
                    if(account.getUserNickName().length()==0){
                        user_nickname.setText(account.getUserName());
                    }else {
                        user_nickname.setText(account.getUserNickName());
                    }
                    String phone = account.getUserPhone();
                    String first = phone.substring(0,3);
                    String mid = phone.substring(3,7);
                    String last = phone.substring(7,11);
                    String total = first+"-"+mid+"-"+last;
                    user_phone.setText(total);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        storageReference.child(User.getUid()).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(profile_pg.this).load(uri).into(profileEdit);
            }
        });

        // 프로필 이미지 변경
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), profile_pg_img.class);
                startActivity(intent);
            }
        });

        // 이름 변경 버튼
        pf_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangeName.class);
                startActivity(intent);
            }
        });

        // 핸드폰 번호 변경 버튼
        pf_phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangeHandphone.class);
                startActivity(intent);
            }
        });

        // 닉네임 변경 버튼
        pf_nickname_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangeNickName.class);
                startActivity(intent);
            }
        });
        // 프로필 툴바 이전 버튼
        profile_tb_btn = (ImageView) findViewById(R.id.profile_tool_tb_btn);
        profile_tb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티가 파괴되지 않았는지 확인
        if (!isFinishing()) {
            // Glide 작업 중지
            Glide.with(this).onDestroy();
        }
    }
}
