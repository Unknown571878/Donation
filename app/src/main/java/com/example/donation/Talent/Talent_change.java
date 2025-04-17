package com.example.donation.Talent;

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
import com.example.donation.writing.WritingInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Talent_change extends AppCompat {
    private Uri uri;
    private ImageView cancel, imageup;
    private Button filebtn, changeup;
    private EditText title, detail, activity;
    private TextView userimg, region;
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private DatabaseReference mDatabaseRef1; // 실시간 데이터 베이스 유저 정보 가져오기위해
    private FirebaseDatabase mDatabase;
    private FirebaseUser User;  // 파이어베이스 유저
    private StorageReference storageReference;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talent_change);

        title = findViewById(R.id.change_talent_edt_title);
        detail = findViewById(R.id.change_talent_edt_detail);
        activity = findViewById(R.id.change_talent_edt_activity);
        cancel = findViewById(R.id.change_talent_writing_view_tb);
        changeup = findViewById(R.id.tchange_uploadactivity);
        imageup = findViewById(R.id.change_talent_imageadd);
        filebtn = findViewById(R.id.change_talent_filebtn);
        userimg = (TextView) findViewById(R.id.change_talent_writing_userimg);    // 보이지 않는 유저 이미지
        region = (TextView) findViewById(R.id.change_talent_writing_region);  // 보이지 않는 텍스트
        TextView userNickname = (TextView) findViewById(R.id.change_talent_userNickName);

        String Title = getIntent().getStringExtra("title");
        String Detail = getIntent().getStringExtra("detail");
        String UserName = getIntent().getStringExtra("username");
        String DateAndTime = getIntent().getStringExtra("dateandtime");
        String PostKey = getIntent().getStringExtra("postkey");
        String Img = getIntent().getStringExtra("img");
        String UserImg = getIntent().getStringExtra("userimg");
        String userID = getIntent().getStringExtra("userID");
        String activityactivity = getIntent().getStringExtra("activity");

        activity.setText(activityactivity);
        title.setText(Title);
        detail.setText(Detail);

        userimg.setText(UserImg);
        setSpinner();
        //Glide.with(Writing_change.this).load(Img).into(imageup);

        if (cancel != null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder cancelask =
                            new AlertDialog.Builder(com.example.donation.Talent.Talent_change.this);
                    cancelask.setTitle("게시글 수정 취소");
                    cancelask.setMessage("수정을 정말 취소하시겠습니까?");
                    cancelask.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "수정을 취소하였습니다.",
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
        filebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mFirebaseAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("donation");
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("donation").child("UserAccount").child(User.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile");
        UserAccout userAccout = new UserAccout();
        Talent_Writing_Info talentWritingInfo = new Talent_Writing_Info();

        mDatabaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if (account != null) {
                    userNickname.setText(account.getUserNickName());
                    userimg.setText(account.getUserImg());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        changeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTitle = title.getText().toString();
                String strDetail = detail.getText().toString();
                String strActivity = activity.getText().toString();
                String strUserNickname = userNickname.getText().toString();
                String strUserImg = userimg.getText().toString();
                int ClickCount = 0;

                if (title.length() != 0 && detail.length() != 0 && activity.length() != 0) {
                    talentWritingInfo.setId(User.getUid());
                    talentWritingInfo.setUserImg(strUserImg);
                    talentWritingInfo.setTitle(strTitle);
                    talentWritingInfo.setDetail(strDetail);
                    talentWritingInfo.setActivity(strActivity);
                    talentWritingInfo.setDateAndTime(DateAndTime);
                    talentWritingInfo.setUserName(strUserNickname);
                    talentWritingInfo.setUserEmailID(userID);
                    talentWritingInfo.setKey(PostKey);
                    userAccout.setActivityPostNum(PostKey);
                    if (uri != null) {
                        talentWritingInfo.setImg(uri.toString());
                        storageReference.child(User.getUid()).child(PostKey).child("postimage").putFile(uri);
                    }

                    mDatabaseRef.child("Activity Post").child(PostKey).setValue(talentWritingInfo);
                    mDatabase = FirebaseDatabase.getInstance();
                    mDatabase.getReference("donation").child("UserAccount").child(User.getUid()).child("user activity post").child(PostKey).setValue(PostKey);
                    Toast.makeText(Talent_change.this, "글수정를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 제목과 세부 정보가 비어 있으면 사용자에게 Toast 메시지를 표시합니다.
                    Toast.makeText(Talent_change.this, "빈칸없이 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            imageup.setImageURI(uri);
            grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }
    public void setSpinner() {
        String[] item = {"서울", "경기", "강원",
                "인천", "대구", "광주", "부산",
                "울산", "대전", "충북","충남",
                "전북", "전남", "경북", "경남", "제주"};

        spinner = findViewById(R.id.change_Tal_CorSpi);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                region.setText(item[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
