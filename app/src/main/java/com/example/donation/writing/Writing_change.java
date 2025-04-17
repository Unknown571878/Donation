package com.example.donation.writing;

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

import com.bumptech.glide.Glide;
import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.login.UserAccout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Writing_change extends AppCompat {
    private Uri uri;
    private ImageView cancel, imageup;
    private Button filebtn, changeup;
    private EditText title, detail, money;
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
        setContentView(R.layout.post_change);

        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("donation");
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("donation").child("UserAccount").child(User.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile");

        title = findViewById(R.id.change_title);
        detail = findViewById(R.id.change_detail);
        money = findViewById(R.id.change_money);
        cancel = findViewById(R.id.change_writing_tool_btn);
        changeup = findViewById(R.id.change_writing_ok_btn);
        imageup = findViewById(R.id.change_imageadd);
        filebtn = findViewById(R.id.change_filebtn);
        userimg = (TextView) findViewById(R.id.post_change_userimg);    // 보이지 않는 유저 이미지
        region = (TextView) findViewById(R.id.post_change_region);  // 보이지 않는 텍스트

        WritingInfo writing = new WritingInfo();
        UserAccout userAccout = new UserAccout();

        String Title = getIntent().getStringExtra("title");
        String Detail = getIntent().getStringExtra("detail");
        String Money = getIntent().getStringExtra("money");
        String UserName = getIntent().getStringExtra("username");
        String DateAndTime = getIntent().getStringExtra("dateandtime");
        int ReceiveMoney = Integer.parseInt(getIntent().getStringExtra("DonationMoney"));
        String PostKey = getIntent().getStringExtra("postkey");
        String Img = getIntent().getStringExtra("img");
        String UserImg = getIntent().getStringExtra("userimg");


        title.setText(Title);
        detail.setText(Detail);
        money.setText(Money);
        userimg.setText(UserImg);
        setSpinner();
        //Glide.with(Writing_change.this).load(Img).into(imageup);

        if (cancel != null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder cancelask =
                            new AlertDialog.Builder(Writing_change.this);
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
        changeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 게시물의 제목과 세부 정보를 가져옵니다.
                String strTitle = title.getText().toString();
                String strDetail = detail.getText().toString();
                String strMoney = money.getText().toString();
                String strUserimg = userimg.getText().toString();
                String strRegion = region.getText().toString();

                String result = "숫자입니다.";
                for(int i=0;i<strMoney.length();i++){
                    char c = strMoney.charAt(i);
                    if(c<48 || c> 57){//숫자가 아닌 경우
                        result = "문자가 포함됨";
                        break;
                    }
                }
                if (result.equals("문자가 포함됨")){
                    Toast.makeText(Writing_change.this, "금액은 숫자만 입력하세요.", Toast.LENGTH_SHORT).show();
                }else{
                    // 제목과 세부 정보가 비어 있지 않은지 확인합니다.
                    if (title.length() != 0 && detail.length() != 0) {
                        // 현재 사용자를 가져옵니다.
                        writing.setId(User.getUid());
                        writing.setUserImg(strUserimg); // 유저 이미지
                        writing.setTitle(strTitle);     // 제목
                        writing.setMoney(strMoney);     // 돈
                        writing.setDetail(strDetail);   // 내용
                        writing.setDateAndTime(DateAndTime);    //시간
                        writing.setUserEmailID(User.getEmail());    // 이메일
                        writing.setUserName(UserName);  // 이름
                        writing.setRegion(strRegion);   // 지역 설정
                        writing.setKey(PostKey);        // 키
                        userAccout.setPostNum(PostKey); // 키
                        writing.setReceive_Money(ReceiveMoney); // 받은 돈
                        if(uri != null) {
                            writing.setImg(uri.toString());
                            storageReference.child(User.getUid()).child(PostKey).child("postimage").putFile(uri);
                        }
                        mDatabaseRef.child("post").child(PostKey).setValue(writing);
                        mDatabase = FirebaseDatabase.getInstance();
                        mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                .child("userpost").child(PostKey).setValue(PostKey);
                        Toast.makeText(Writing_change.this, "게시글 수정을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 제목과 세부 정보가 비어 있으면 사용자에게 Toast 메시지를 표시합니다.
                        Toast.makeText(Writing_change.this, "dd.", Toast.LENGTH_SHORT).show();
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

        spinner = (Spinner) findViewById(R.id.post_change_spinner);

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
