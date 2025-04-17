package com.example.donation.option;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.example.donation.PersonalSecurityActivity;
import com.example.donation.customer_operational.CustomerOperationalActivity;
import com.example.donation.etc.AccountChange;
import com.example.donation.etc.AppCompat;
import com.example.donation.R;
import com.example.donation.language.LanguageManager;
import com.example.donation.notice.NoticeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OptionActivity extends AppCompat {
    Uri uri;
    int bcount = 4;
    int lcount = 1;
    Button btn[] = new Button[bcount];
    LinearLayout llayout[] = new LinearLayout[lcount];
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseDatabase database;      // 파이어 베이스 데이터베이스
    private FirebaseUser User;              // 파이어 베이스 유저
    private View dialogView;
    private EditText edt1, edt2;
    private ImageView option_tb_btn;  // 프로필 이미지, 뒤로가기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_view);

        LanguageManager lang = new LanguageManager(this);

        Integer btnID[] = {R.id.option_listview_button1, R.id.option_listview_button2,R.id.option_listview_button4,R.id.option_listview_button8};
        Integer llayoutID[] = {R.id.option_ll1};
        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        View line = (View) findViewById(R.id.option_line);

        for (int i = 0; i < bcount; i++) {
            btn[i] = (Button) findViewById(btnID[i]);
        }
        for (int i = 0; i < lcount; i++) {
            llayout[i] = (LinearLayout) findViewById(llayoutID[i]);
        }

        // 뒤로가기 버튼
        option_tb_btn = (ImageView) findViewById(R.id.option_view_tb_btn);
        option_tb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 계정
        btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 아이디 & 비번 재입력
                dialogView = (View) View.inflate(OptionActivity.this, R.layout.account_auth, null);
                android.app.AlertDialog.Builder dlg1 = new android.app.AlertDialog.Builder(OptionActivity.this);
                dlg1.setTitle("재인증");
                dlg1.setView(dialogView);
                dlg1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edt1 = (EditText) dialogView.findViewById(R.id.account_auth_edt1);
                        edt2 = (EditText) dialogView.findViewById(R.id.account_auth_edt2);
                        String a = edt1.getText().toString();
                        String b = edt2.getText().toString();
                        if (edt1.length() == 0 && edt2.length() == 0) {
                            Toast.makeText(OptionActivity.this, "아이디 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                        } else if (edt2.length() == 0) {
                            Toast.makeText(OptionActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                        } else if (edt1.length() == 0) {
                            Toast.makeText(OptionActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            mFirebaseAuth.signInWithEmailAndPassword(a, b).addOnCompleteListener(OptionActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), AccountChange.class);
                                        startActivity(intent);
                                        finish(); // 현재 액티비티 삭제
                                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(OptionActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
                dlg1.show();
            }
        });
        // 개인 / 보안
        btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PersonalSecurityActivity.class);
                startActivity(intent);
            }
        });
        // 공지 사항
        btn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                startActivity(intent);
            }
        });
        // 고객 센터 / 운영 정책
        btn[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerOperationalActivity.class);
                startActivity(intent);
            }
        });
    }
}
