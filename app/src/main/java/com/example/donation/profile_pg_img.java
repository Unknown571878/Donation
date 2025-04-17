package com.example.donation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.donation.etc.AppCompat;
import com.example.donation.login.UserAccout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class profile_pg_img extends AppCompat {
    private Uri uri;
    private ImageView img, tb_img;
    private Button btn1, btn2;
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseDatabase database;      // 파이어 베이스 데이터베이스
    private FirebaseStorage mFirebaseStorage; // 파이어 베이스 스토리지
    private FirebaseUser User;              // 파이어 베이스 유저
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private StorageReference storageReference;   // 스토리지 참조 만들기


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_view);

        img = (ImageView) findViewById(R.id.img_view_img);  // 가운데 이미지
        tb_img = (ImageView) findViewById(R.id.img_view_tb);// 뒤로가기
        btn1 = (Button) findViewById(R.id.img_view_btn1);   // 사진 선택
        btn2 = (Button) findViewById(R.id.img_view_btn2);   // 사진 올리기

        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("donation").child("UserAccount").child(User.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile");
        storageReference.child(User.getUid());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if(account.getUserImg().length() != 0){
                    Glide.with(profile_pg_img.this).load(account.getUserImg()).into(img);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 사진 선택
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select();
            }
        });
        // 사진 업로드
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
        // 이전 화면
        tb_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // 선택
    private void select() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    // 업로드
    private void upload() {
        storageReference = FirebaseStorage.getInstance().getReference("profile");
        storageReference.child(User.getUid()).child("image").putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    String name = uri.toString();
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("userImg",name);
                    mDatabaseRef.updateChildren(taskMap);
                    Toast.makeText(profile_pg_img.this, "업로드에 성공했습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(profile_pg_img.this, "업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            img.setImageURI(uri);
            grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
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
