package com.example.donation.price_determin;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.donation.DonationListItem;
import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.login.UserAccout;
import com.example.donation.point.PointListItem;
import com.example.donation.writing.WritingInfo;
import com.example.donation.writing.writing_pg;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Price_determine extends AppCompat {
    private Button btn1000, btn5000, btn10000, btn50000, give_money;
    private EditText price;
    private TextView point, maxmoney, money, savemoney, totalmoney;
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseUser User;              // 파이어 베이스 유저
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private FirebaseMessaging firebaseMessaging;
    long mNow;
    int total,goal;
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
        setContentView(R.layout.price_determin);

        give_money = findViewById(R.id.give_money);
        btn1000 = findViewById(R.id.amountButton1);
        btn5000 = findViewById(R.id.amountButton2);
        btn10000 = findViewById(R.id.amountButton3);
        btn50000 = findViewById(R.id.amountButton4);
        price = findViewById(R.id.customAmountEditText);
        point = findViewById(R.id.userpoint);
        maxmoney = findViewById(R.id.p_money);
        money = findViewById(R.id.getmoney);
        savemoney = findViewById(R.id.getsavemoney);
        totalmoney = findViewById(R.id.totaldonation);
        ImageView Price_home = (ImageView) findViewById(R.id.price_tb_btn);
        if (Price_home != null) {
            Price_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 인스턴스
        User = mFirebaseAuth.getCurrentUser();      // 파이어베이스 유저 확인
        mDatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터 베이스 인스턴스
        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid()); // Path 지정
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if(account != null){
                    point.setText(String.valueOf(account.getUserPoint()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        String ThisPost = getIntent().getStringExtra("thispost"); // 게시글 아이디
        String strTitle = getIntent().getStringExtra("thistitle"); // 게시글 제목
        String PostEditerID = getIntent().getStringExtra("postuserID"); // 게시글 작성자 아이디
        String strPostUserNickname = getIntent().getStringExtra("PostUserNickname");
        String strTime = getTime(); //시간
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("donation").child("post").child(ThisPost);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WritingInfo writingInfo = snapshot.getValue(WritingInfo.class);
                if(writingInfo != null){
                    money.setText(writingInfo.getMoney()); // 기부 총액
                    savemoney.setText(String.valueOf(writingInfo.getReceive_Money())); // 지금까지 받은
                    maxmoney.setText(String.valueOf(Integer.parseInt(money.getText().toString())
                            - Integer.parseInt(savemoney.getText().toString())));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        btn1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_price = price.getText().toString();
                if (input_price.isEmpty()){
                    if ( 1000 > Integer.parseInt(point.getText().toString()) ){
                        price.setText(point.getText().toString());
                        Toast.makeText(getApplicationContext(), "보유중인 최대 포인트를 넘었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        price.setText("1000");
                    }
                } else {
                    if ( 1000 + Integer.parseInt(price.getText().toString()) > Integer.parseInt(point.getText().toString()) ){
                        price.setText(point.getText().toString());
                        Toast.makeText(getApplicationContext(), "보유중인 최대 포인트를 넘었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        price.setText(String.valueOf(1000 + Integer.parseInt(price.getText().toString())));
                    }
                }
            }
        });
        btn5000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_price = price.getText().toString();
                if (input_price.isEmpty()){
                    if ( 5000 > Integer.parseInt(point.getText().toString()) ){
                        price.setText(point.getText().toString());
                        Toast.makeText(getApplicationContext(), "보유중인 최대 포인트를 넘었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        price.setText("5000");
                    }
                } else {
                    if ( 5000 + Integer.parseInt(price.getText().toString()) > Integer.parseInt(point.getText().toString()) ){
                        price.setText(point.getText().toString());
                        Toast.makeText(getApplicationContext(), "보유중인 최대 포인트를 넘었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        price.setText(String.valueOf(5000 + Integer.parseInt(price.getText().toString())));
                    }
                }
            }
        });
        btn10000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_price = price.getText().toString();
                if (input_price.isEmpty()){
                    if ( 10000 > Integer.parseInt(point.getText().toString()) ){
                        price.setText(point.getText().toString());
                        Toast.makeText(getApplicationContext(), "보유중인 최대 포인트를 넘었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        price.setText("10000");
                    }
                } else {
                    if ( 10000 + Integer.parseInt(price.getText().toString()) > Integer.parseInt(point.getText().toString()) ){
                        price.setText(point.getText().toString());
                        Toast.makeText(getApplicationContext(), "보유중인 최대 포인트를 넘었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        price.setText(String.valueOf(10000 + Integer.parseInt(price.getText().toString())));
                    }
                }
            }
        });
        btn50000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_price = price.getText().toString();
                if (input_price.isEmpty()){
                    if ( 50000 > Integer.parseInt(point.getText().toString()) ){
                        price.setText(point.getText().toString());
                        Toast.makeText(getApplicationContext(), "보유중인 최대 포인트를 넘었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        price.setText("50000");
                    }
                } else {
                    if ( 50000 + Integer.parseInt(price.getText().toString()) > Integer.parseInt(point.getText().toString()) ){
                        price.setText(point.getText().toString());
                        Toast.makeText(getApplicationContext(), "보유중인 최대 포인트를 넘었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        price.setText(String.valueOf(50000 + Integer.parseInt(price.getText().toString())));
                    }
                }
            }
        });


        give_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(price.length() != 0){
                    if (Integer.parseInt(price.getText().toString()) > Integer.parseInt(point.getText().toString())) {
                        price.setText(point.getText().toString());
                        Toast.makeText(getApplicationContext(), "보유중인 최대 포인트를 넘었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        android.app.AlertDialog.Builder cancelask =
                                new AlertDialog.Builder(Price_determine.this);
                        cancelask.setTitle("기부");
                        cancelask.setMessage("포인트 " + price.getText().toString() + "을 사용하여 기부하시겠습니까?");
                        cancelask.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (Integer.parseInt(savemoney.getText().toString()) + Integer.parseInt(price.getText().toString())
                                        > Integer.parseInt(money.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "금액이 너무 큽니다", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                } else if(Integer.parseInt(savemoney.getText().toString()) + Integer.parseInt(price.getText().toString())
                                        <Integer.parseInt(money.getText().toString())){
                                    PointListItem pointListItem = new PointListItem();
                                    pointListItem.setPoint(String.valueOf(-Integer.parseInt(price.getText().toString())));
                                    pointListItem.setTitle(strTitle);
                                    pointListItem.setTime(strTime);
                                    pointListItem.setPostId(ThisPost);
                                    mDatabase = FirebaseDatabase.getInstance();
                                    mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                            .child("Donation List").push().setValue(pointListItem);
    
                                    int change = Integer.parseInt(point.getText().toString()) - Integer.parseInt(price.getText().toString()); // 사용하고 남은 금액
                                    mDatabase = FirebaseDatabase.getInstance();
                                    mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                            .child("userPoint").setValue(change);
    
                                    mDatabase = FirebaseDatabase.getInstance();
                                    mDatabase.getReference("donation").child("post").child(ThisPost)
                                            .child("receive_Money")
                                            .setValue(Integer.parseInt(savemoney.getText().toString()) + Integer.parseInt(price.getText().toString())); // 기부한 포인트 전송
                                    Toast.makeText(getApplicationContext(), price.getText().toString() + "포인트 기부하였습니다", Toast.LENGTH_SHORT).show();
                                    DonationListItem donationListItem = new DonationListItem();
                                    donationListItem.setTitle(strTitle);
                                    donationListItem.setTime(strTime);
                                    donationListItem.setPostId(ThisPost);
                                    donationListItem.setNickname(strPostUserNickname);
                                    donationListItem.setPoint(String.valueOf(Integer.parseInt(price.getText().toString())));

                                    mDatabase = FirebaseDatabase.getInstance();
                                    mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                            .child("User Donation").push().setValue(donationListItem)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        finish();
                                                    }
                                                }
                                            });
                                    finish();
                                }else if(Integer.parseInt(savemoney.getText().toString()) + Integer.parseInt(price.getText().toString())
                                        ==Integer.parseInt(money.getText().toString())){
                                    PointListItem pointListItem = new PointListItem();
                                    pointListItem.setPoint(String.valueOf(-Integer.parseInt(price.getText().toString())));
                                    pointListItem.setTitle(strTitle);
                                    pointListItem.setTime(strTime);
                                    pointListItem.setPostId(ThisPost);
                                    mDatabase = FirebaseDatabase.getInstance();
                                    mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                            .child("Donation List").push().setValue(pointListItem);
    
                                    int change = Integer.parseInt(point.getText().toString()) - Integer.parseInt(price.getText().toString()); // 사용하고 남은 금액
                                    mDatabase = FirebaseDatabase.getInstance();
                                    mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                            .child("userPoint").setValue(change);
    
                                    mDatabase = FirebaseDatabase.getInstance();
                                    mDatabase.getReference("donation").child("post").child(ThisPost)
                                            .child("receive_Money")
                                            .setValue(Integer.parseInt(savemoney.getText().toString()) + Integer.parseInt(price.getText().toString())); // 기부한 포인트 전송
                                    Toast.makeText(getApplicationContext(), price.getText().toString() + "포인트 기부하였습니다", Toast.LENGTH_SHORT).show();
    
                                    mDatabase = FirebaseDatabase.getInstance();
                                    mDatabase.getReference("donation").child("post").child(ThisPost)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    WritingInfo writingInfo = snapshot.getValue(WritingInfo.class);
                                                    if(writingInfo != null){
                                                        total = writingInfo.getReceive_Money();
                                                        goal = Integer.parseInt(writingInfo.getMoney());
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {}});
                                    if (total == goal) {
                                        String strPoint = money.getText().toString();
                                        String str = "기부 결과";
                                        PointListItem getpointListItem = new PointListItem();
                                        getpointListItem.setPoint("+" + strPoint);
                                        getpointListItem.setTitle(str);
                                        getpointListItem.setTime(strTime);
                                        getpointListItem.setPostId(ThisPost);
    
                                        mDatabase = FirebaseDatabase.getInstance();
                                        mDatabase.getReference("donation").child("UserAccount").child(PostEditerID)
                                                .child("Donation List").push().setValue(getpointListItem);
    
                                        mDatabase = FirebaseDatabase.getInstance();
                                        mDatabase.getReference("donation").child("UserAccount").child(PostEditerID)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        UserAccout userAccout = snapshot.getValue(UserAccout.class);
                                                        if (userAccout != null) {
                                                            int userpoint = userAccout.getUserPoint();
                                                            int usertotalpoint = userpoint + total;
    
                                                            mDatabase = FirebaseDatabase.getInstance();
                                                            mDatabase.getReference("donation").child("UserAccount").child(PostEditerID)
                                                                    .child("userPoint").setValue(usertotalpoint);
    
                                                            mDatabase = FirebaseDatabase.getInstance();
                                                            mDatabase.getReference("donation").child("post").child(ThisPost)
                                                                    .child("receive_Money").setValue(-1);
    
                                                            DonationListItem donationListItem = new DonationListItem();
                                                            donationListItem.setTitle(strTitle);
                                                            donationListItem.setTime(strTime);
                                                            donationListItem.setPostId(ThisPost);
                                                            donationListItem.setNickname(strPostUserNickname);
                                                            donationListItem.setPoint(String.valueOf(Integer.parseInt(price.getText().toString())));
    
                                                            mDatabase = FirebaseDatabase.getInstance();
                                                            mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                                                    .child("User Donation").push().setValue(donationListItem)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                finish();
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                    }
                                }
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
                }else {
                    Toast.makeText(Price_determine.this, "금액을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
