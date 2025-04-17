package com.example.donation.watchlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.login.UserAccout;
import com.example.donation.price_determin.Price_determine;
import com.example.donation.writing.WritingViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class WatchListView extends AppCompat {
    private FirebaseDatabase mDatabase;          // 파이어 베이스 데이터베이스
    private FirebaseAuth mFirebaseAuth;         // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터 베이스
    private FirebaseUser User;                  // 파이어 베이스 유저
    private TextView viewcount, viewmoney, viewtime,viewtitle,viewdetail,viewusernickname;
    private Button GibuButton;
    private ImageView mainImg, Fimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_view_like);

        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("donation");
        mDatabase = FirebaseDatabase.getInstance();

        viewmoney = (TextView) findViewById(R.id.writing_view_like_money);       // 금액
        viewtime = (TextView) findViewById(R.id.writing_view_like_time);         // 시간
        viewtitle = (TextView) findViewById(R.id.writing_view_like_title);       // 제목
        viewdetail = (TextView) findViewById(R.id.writing_view_like_detail);     // 내용
        viewusernickname = (TextView) findViewById(R.id.writing_view_like_nickname);    // 게시자 닉네임
        Fimage = (ImageView) findViewById(R.id.writing_tool_btn); // 뒤로가기 버튼
        mainImg = (ImageView) findViewById(R.id.writing_view_like_img);// 게시글 이미지

        GibuButton = findViewById(R.id.writing_view_like_GibuButton);

        // 뒤로가기
        Fimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String Title = getIntent().getStringExtra("title");
        String Money = getIntent().getStringExtra("money");
        String Dateandtime = getIntent().getStringExtra("dateandtime");
        String Detail = getIntent().getStringExtra("detail");
        String userNickName = getIntent().getStringExtra("username");

        String img = getIntent().getStringExtra("img");
        String listid = getIntent().getStringExtra("listid");

        viewtitle.setText(Title);
        viewmoney.setText(Money);
        viewtime.setText(Dateandtime);
        viewdetail.setText(Detail);
        viewusernickname.setText(userNickName);

        Glide.with(WatchListView.this).load(img).into(mainImg);

        // 기부 버튼
        GibuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Price_determine.class);
                startActivity(intent);
            }
        });
    }
}
