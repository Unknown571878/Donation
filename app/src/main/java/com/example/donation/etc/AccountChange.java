package com.example.donation.etc;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.donation.MainActivity;
import com.example.donation.R;
import com.example.donation.listofwriting.ListOfWritingActivity;
import com.example.donation.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccountChange extends AppCompat {

    private Button btn1, btn2, btn3;
    private ImageView account_tb_btn;
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseUser User;              // 파이어 베이스 유저
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private DatabaseReference mDatabaseRef1; // 실시간 데이터 베이스
    private FirebaseStorage fStorage; // 파이어 베이스 스토리지
    private StorageReference storageRef; // 스토리지 연결
    String[] mytalentarr = new String[100];  // 내가 쓴 재능 기부 게시글 ID 배열
    String[] mywritingarr = new String[100];  // 내가 쓴 기부 게시글 ID 배열
    String[] talentarr = new String[100];  // 재능 기부 게시글 ID 배열
    String[] writingarr = new String[100];   // 기부 게시글 ID 배열

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_view);

        btn1 = (Button) findViewById(R.id.account_view_btn1);   // 이메일 변경 버튼
        btn2 = (Button) findViewById(R.id.account_view_btn2);   // 휴대폰 번호 변경 버튼
        btn3 = (Button) findViewById(R.id.account_view_btn3);   // 회원탈퇴 버튼
        account_tb_btn = (ImageView) findViewById(R.id.account_tb_btn); // 뒤로가기

        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        fStorage = FirebaseStorage.getInstance();
        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid());
        mDatabaseRef1 = mDatabase.getReference("donation");
        storageRef = fStorage.getReference("profile").child(User.getUid());

        // 뒤로가기
        account_tb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 내가 쓴 재능 기부 게시글 ID 배열 저장
        mDatabaseRef.child("user activity post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    String a = snapshot.getKey();
                    mytalentarr[i] = a;
                    Log.d("내가 쓴 재능 기부 게시글 ID 배열", mytalentarr[i]);
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // 내가 쓴 기부 게시글 ID 배열 저장
        mDatabaseRef.child("userpost").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                int z = 0;
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    String a = snapshot.getKey();
                    mywritingarr[z] = a;
                    Log.d("내가 쓴 기부 게시글 ID 배열", mywritingarr[z]);
                    z++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // 재능 기부 게시글 ID 배열 저장
        mDatabaseRef1.child("Activity Post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                int x = 0;
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    String a = snapshot.getKey();
                    talentarr[x] = a;
                    Log.d("재능 기부 게시글 ID 배열", talentarr[x]);
                    x++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // 기부 게시글 ID 배열 저장
        mDatabaseRef1.child("post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                int c = 0;
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    String a = snapshot.getKey();
                    writingarr[c] = a;
                    Log.d("기부 게시글 ID 배열", writingarr[c]);
                    c++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 이름 & 닉네임 변경
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangeName.class);
                startActivity(intent);
            }
        });

        // 핸드폰 번호 변경
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangeHandphone.class);
                startActivity(intent);
            }
        });

        // 탈퇴 버튼 처리
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg2 = new AlertDialog.Builder(AccountChange.this);
                dlg2.setTitle("회원 탈퇴");
                dlg2.setMessage("정말 회원 탈퇴를 하시겠습니까? \n한번 탈퇴하면 되돌릴 수 없습니다.");
                dlg2.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 자동 로그인 종료
                        SharedPreferences prefer = getSharedPreferences("temp", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefer.edit();
                        Boolean b = prefer.getBoolean("cb", false);
                        editor.putBoolean("cb", b);
                        editor.putBoolean("isInit", false);
                        editor.apply();
                        // 재능 기부 게시판 글 삭제
                        for (int q = 0; q < 100; q++) {
                            for (int w = 0; w < 100; w++) {
                                if (mytalentarr[q] != null) {
                                    if (mytalentarr[q].equals(talentarr[w])) {
                                        mDatabaseRef1.child("Activity Post").child(talentarr[w]).removeValue();
                                        talentarr[w] = "0";
                                        Log.d("yes", "yes");
                                    }
                                }
                            }
                        }
                        // 기부 게시판 글 삭제
                        for (int e = 0; e < 100; e++) {
                            if (mywritingarr[e] != null) {
                                for (int r = 0; r < 100; r++) {
                                    if (mywritingarr[e].equals(writingarr[r])) {
                                        mDatabaseRef1.child("post").child(writingarr[r]).removeValue();
                                        writingarr[r] = "0";
                                        Log.d("yes", "yes");
                                    }
                                }
                            }
                        }
                        mFirebaseAuth.signOut();
                        User.delete();  // 유저 정보 삭제
                        mDatabaseRef.removeValue(); // 데이터 베이스 삭제
                        storageRef.delete();    // 스토리지 삭제

                        finish();
                        Intent intent = new Intent(AccountChange.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "회원 탈퇴가 되셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg2.show();
            }
        });
    }
}