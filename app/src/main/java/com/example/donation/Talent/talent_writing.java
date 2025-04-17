package com.example.donation.Talent;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.login.UserAccout;
import com.example.donation.writing.writing_pg;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class talent_writing extends AppCompat {
    private Uri uri;
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private DatabaseReference mDatabaseRef1; // 실시간 데이터 베이스 유저 정보 가져오기위해
    private FirebaseDatabase mDatabase;
    private StorageReference storageReference;
    private ImageView wcancle, imgfile;
    private EditText title, detail, activity;
    private TextView username,userimg,userloc;
    private Button upactivity,imgfile_btn;
    private Spinner spinner;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talent_writing);
        // 뒤로가기
        wcancle = findViewById(R.id.talent_writing_tool_btn);
        if (wcancle != null) {
            wcancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder cancelask =
                            new AlertDialog.Builder(talent_writing.this);
                    cancelask.setTitle("글쓰기 취소");
                    cancelask.setMessage("글쓰기를 정말 취소하시겠습니까?");
                    cancelask.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "글쓰기를 취소하였습니다.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    cancelask.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    cancelask.show();
                }
            });
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mFirebaseAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("donation");
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("donation").child("UserAccount").child(User.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile");
        UserAccout userAccout = new UserAccout();
        Talent_Writing_Info talentWritingInfo = new Talent_Writing_Info();

        title = findViewById(R.id.talent_edt_title);
        detail = findViewById(R.id.talent_edt_detail);
        activity = findViewById(R.id.talent_edt_activity);
        username = findViewById(R.id.talent_writing_username);
        userimg = (TextView) findViewById(R.id.talent_writing_userimg);
        userloc = (TextView) findViewById(R.id.talent_writing_userloc); // 유저 지역
        upactivity = findViewById(R.id.uploadactivity);
        setSpinner();


        mDatabaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if (account != null) {
                    username.setText(account.getUserNickName());
                    userimg.setText(account.getUserImg());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        upactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTitle = title.getText().toString();
                String strDetail = detail.getText().toString();
                String strActivity = activity.getText().toString();
                String strDateAndTime = getTime();
                String strUsername = username.getText().toString();
                String strUserImg = userimg.getText().toString();
                String strRegion = userloc.getText().toString();
                int ClickCount = 0;

                if (title.length() != 0 && detail.length() != 0 && activity.length() != 0) {
                    talentWritingInfo.setId(User.getUid());
                    talentWritingInfo.setUserImg(strUserImg);
                    talentWritingInfo.setTitle(strTitle);
                    talentWritingInfo.setDetail(strDetail);
                    talentWritingInfo.setActivity(strActivity);
                    talentWritingInfo.setDateAndTime(strDateAndTime);
                    talentWritingInfo.setUserNickName(strUsername);
                    talentWritingInfo.setUserEmailID(User.getEmail());
                    talentWritingInfo.setRegion(strRegion);
                    String key = mDatabaseRef.child("Activity Post").push().getKey();
                    talentWritingInfo.setKey(key);
                    userAccout.setActivityPostNum(key);
                    if(uri != null) {
                        talentWritingInfo.setImg(uri.toString());
                        storageReference.child(User.getUid()).child(key).child("postimage").putFile(uri);
                    }

                    mDatabaseRef.child("Activity Post").child(key).setValue(talentWritingInfo);
                    mDatabase = FirebaseDatabase.getInstance();
                    mDatabase.getReference("donation").child("UserAccount").child(User.getUid()).child("user activity post").child(key).setValue(key);
                    mDatabase = FirebaseDatabase.getInstance();
                    mDatabase.getReference("donation").child("UserAccount").child(User.getUid()).child("Join Activity").child(key).setValue(key);
                    Toast.makeText(talent_writing.this, "글쓰기를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 제목과 세부 정보가 비어 있으면 사용자에게 Toast 메시지를 표시합니다.
                    Toast.makeText(talent_writing.this, "빈칸 없이 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 이미지 등록
        imgfile = (ImageView) findViewById(R.id.talent_imageadd);
        imgfile_btn = (Button) findViewById(R.id.talent_filebtn);
        imgfile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            imgfile.setImageURI(uri);
            grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }
    public void setSpinner() {
        String[] item = {"서울", "경기", "강원",
                "인천", "대구", "광주", "부산",
                "울산", "대전", "충북","충남",
                "전북", "전남", "경북", "경남", "제주"};

        spinner = (Spinner) findViewById(R.id.Tal_WtSpr);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userloc.setText(item[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
