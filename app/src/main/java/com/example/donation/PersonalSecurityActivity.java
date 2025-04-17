package com.example.donation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.donation.etc.AccountChange;
import com.example.donation.etc.AppCompat;
import com.example.donation.etc.ChangePassword;
import com.example.donation.option.OptionActivity;
import com.example.donation.question.QuestionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PersonalSecurityActivity extends AppCompat {
    Button btn1,btn2, btn3;
    private View dialogView;
    private EditText edt1, edt2;
    private ImageView btn_ps_option;    //뒤로가기
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_security);

        btn_ps_option = (ImageView) findViewById(R.id.personal_security_tb_btn);
        btn_ps_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn1 = (Button) findViewById(R.id.personal_security_btn1);
        btn3 = (Button) findViewById(R.id.personal_security_btn3);

        mFirebaseAuth = FirebaseAuth.getInstance();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), profile_pg.class);
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 아이디 & 비번 재입력
                dialogView = (View) View.inflate(PersonalSecurityActivity.this,R.layout.account_auth, null);
                android.app.AlertDialog.Builder dlg1 = new android.app.AlertDialog.Builder(PersonalSecurityActivity.this);
                dlg1.setTitle("재인증");
                dlg1.setView(dialogView);
                dlg1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edt1 = (EditText) dialogView.findViewById(R.id.account_auth_edt1);
                        edt2 = (EditText) dialogView.findViewById(R.id.account_auth_edt2);
                        String a = edt1.getText().toString();
                        String b = edt2.getText().toString();
                        if (edt1.length()==0 && edt2.length()==0) {
                            Toast.makeText(PersonalSecurityActivity.this, "아이디 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                        } else if (edt2.length()==0) {
                            Toast.makeText(PersonalSecurityActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                        } else if (edt1.length()==0) {
                            Toast.makeText(PersonalSecurityActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }else {
                            mFirebaseAuth.signInWithEmailAndPassword(a, b).addOnCompleteListener(PersonalSecurityActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                                        startActivity(intent);
                                        finish(); // 현재 액티비티 삭제
                                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PersonalSecurityActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
                dlg1.show();
            }
        });


    }
}
