package com.example.donation.writing;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
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

public class writing_pg extends AppCompat {
    private Uri uri;
    private ImageView cancel, imageup;
    private Button filebtn, btn1;
    private TextView username, userimg, region;
    private EditText title, detail, money;
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private DatabaseReference mDatabaseRef1; // 실시간 데이터 베이스 유저 정보 가져오기위해
    private FirebaseDatabase mDatabase;
    private FirebaseUser User;  // 파이어베이스 유저
    private StorageReference storageReference;
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
        setContentView(R.layout.writing);

        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("donation");
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("donation").child("UserAccount").child(User.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile");


        WritingInfo writing = new WritingInfo();
        UserAccout userAccout = new UserAccout();

        cancel = findViewById(R.id.writing_tool_btn);   // 글쓰기 취소
        if (cancel != null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder cancelask =
                            new AlertDialog.Builder(writing_pg.this);
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

        imageup = findViewById(R.id.imageadd);
        filebtn = findViewById(R.id.filebtn);
        filebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


        title = (EditText) findViewById(R.id.edt_title);
        detail = (EditText) findViewById(R.id.edt_detail);
        money = (EditText) findViewById(R.id.edt_money);
        username = (TextView) findViewById(R.id.writing_username);
        region = (TextView) findViewById(R.id.writing_region);
        userimg = (TextView) findViewById(R.id.writing_userimg);
        setSpinner();

        mDatabaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if (account != null){
                    username.setText(account.getUserNickName());
                    userimg.setText(account.getUserImg());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btn1 = (Button) findViewById(R.id.writing_ok_btn);  // 글쓰기 완료 버튼
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 게시물의 제목과 세부 정보를 가져옵니다.
                String strTitle = title.getText().toString();
                String strDetail = detail.getText().toString();
                String strMoney = money.getText().toString();
                String strDateAndTime = getTime();
                String strUserName = username.getText().toString();
                String strUserImg = userimg.getText().toString();
                String strRegion = region.getText().toString();

                int RMoney = 0;
                int clickcount = 0;

                String result = "숫자입니다.";
                for(int i=0;i<strMoney.length();i++){
                    char c = strMoney.charAt(i);
                    if(c<48 || c> 57){//숫자가 아닌 경우
                        result = "문자가 포함됨";
                        break;
                    }
                }
                if (result.equals("문자가 포함됨")){
                    Toast.makeText(writing_pg.this, "금액은 숫자만 입력하세요.", Toast.LENGTH_SHORT).show();
                }else{
                    // 제목과 세부 정보가 비어 있지 않은지 확인합니다.
                    if (title.length() != 0 && detail.length() != 0) {
                        // 현재 사용자를 가져옵니다.
                        writing.setId(User.getUid());
                        writing.setTitle(strTitle);
                        writing.setMoney(strMoney);
                        writing.setDetail(strDetail);
                        writing.setDateAndTime(strDateAndTime);
                        writing.setUserEmailID(User.getEmail());
                        writing.setUserNickName(strUserName);
                        writing.setUserImg(strUserImg);
                        String key = mDatabaseRef.child("post").push().getKey();
                        writing.setKey(key);
                        userAccout.setPostNum(key);
                        writing.setReceive_Money(RMoney);
                        writing.setRegion(strRegion);

                        if(uri != null) {
                            writing.setImg(uri.toString());
                            storageReference.child(User.getUid()).child(key).child("postimage").putFile(uri);
                        }
                        mDatabaseRef.child("post").child(key).setValue(writing);
                        mDatabase = FirebaseDatabase.getInstance();
                        mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                .child("userpost").child(key).setValue(key);
                        Toast.makeText(writing_pg.this, "글쓰기를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 제목과 세부 정보가 비어 있으면 사용자에게 Toast 메시지를 표시합니다.
                        Toast.makeText(writing_pg.this, "빈칸없이 입력해주세요ㅁㄴ.", Toast.LENGTH_SHORT).show();
                    }
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

        spinner = (Spinner) findViewById(R.id.SpinnerWt);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
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