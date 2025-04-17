package com.example.donation.Talent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.donation.R;
import com.example.donation.Talent_Chat_.Talent_Chat;
import com.example.donation.etc.AppCompat;
import com.example.donation.login.UserAccout;
import com.example.donation.price_determin.Price_determine;
import com.example.donation.writing.WritingViewActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class talent_detail extends AppCompat {
    private TextView t_title, t_detail, t_nickname, t_dateandtime, t_activity, applyuser, t_loc;
    private ImageView mw;
    private ImageView userImg, mainImg;
    private Button apply_btn, chat_btn;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseUser User;
    private FirebaseAuth mFirebaseAuth;
    private StorageReference storageReference;

    SwipeRefreshLayout talent_swiperefreshlayout;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talent_writing_view);

        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile");

        t_title = findViewById(R.id.talent_Viewtitle);
        t_detail = findViewById(R.id.talent_Viewdetail);
        t_nickname = findViewById(R.id.talent_writing_view_nickname);
        t_dateandtime = findViewById(R.id.talent_Viewdateandtime);
        t_activity = findViewById(R.id.talent_Viewactivity);
        t_loc = (TextView) findViewById(R.id.talent_writing_view_loc);  // 지역명
        mw = findViewById(R.id.talent_writing_view_tb);
        userImg = (ImageView) findViewById(R.id.talent_writing_view_userimg);
        mainImg = (ImageView) findViewById(R.id.talent_writing_view_mainimg);
        apply_btn = findViewById(R.id.talent_GibuButton);
        chat_btn = findViewById(R.id.talent_chatButton);
        applyuser = findViewById(R.id.applyuser);
        talent_swiperefreshlayout = findViewById(R.id.talent_swiperefreshlayout);

        talent_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                overridePendingTransition(0,0);
                finish();
                startActivity(intent);
                overridePendingTransition(0,0);
                Toast.makeText(getApplicationContext(),"새로고침 하였습니다.", Toast.LENGTH_SHORT).show();
                talent_swiperefreshlayout.setRefreshing(false);
            }
        });


        mw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}});

        String Title = getIntent().getStringExtra("title");
        String Activity = getIntent().getStringExtra("activity");
        String Dateandtime = getIntent().getStringExtra("dateandtime");
        String Detail = getIntent().getStringExtra("detail");
        String userNickName = getIntent().getStringExtra("username");
        String userimg = getIntent().getStringExtra("userimg");
        String mainimg = getIntent().getStringExtra("mainimg");
        String UserID = getIntent().getStringExtra("userID");
        String PostKey = getIntent().getStringExtra("thispost");
        String Region = getIntent().getStringExtra("region");
        storageReference.child(UserID).child(PostKey).child("postimage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(talent_detail.this).load(uri).into(mainImg);
            }
        });
        storageReference.child(UserID).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(talent_detail.this).load(uri).into(userImg);
            }
        });



        t_title.setText(Title);
        t_detail.setText(Detail);
        t_dateandtime.setText(Dateandtime);
        t_nickname.setText(userNickName);
        t_activity.setText(Activity);
        t_loc.setText(Region);

        String loginUser = User.getUid();

        if (loginUser.equals(UserID)){
            apply_btn.setVisibility(View.GONE);
            chat_btn.setVisibility(View.VISIBLE);
        } else {
            database = FirebaseDatabase.getInstance();
            database.getReference("donation").child("Activity Post").child(PostKey)
                    .child("Apply User").get()
                            .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        apply_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(getApplicationContext(), "이미 마감된 기부 활동 입니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        apply_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                android.app.AlertDialog.Builder apply_talent =
                                                        new AlertDialog.Builder(talent_detail.this);
                                                apply_talent.setTitle("활동");
                                                apply_talent.setMessage("이 활동에 참여하시겠습니까?");
                                                apply_talent.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        database = FirebaseDatabase.getInstance();
                                                        database.getReference("donation").child("Activity Post").child(PostKey)
                                                                .child("Apply User").setValue(User.getUid());
                                                        database = FirebaseDatabase.getInstance();
                                                        database.getReference("donation").child("UserAccount")
                                                                .child(User.getUid()).child("Join Activity").child(PostKey).setValue(PostKey);
                                                        Intent intent = getIntent();
                                                        overridePendingTransition(0,0);
                                                        finish();
                                                        startActivity(intent);
                                                        overridePendingTransition(0,0);

                                                        Toast.makeText(getApplicationContext(), "등록되었습니다", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                apply_talent.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {dialogInterface.cancel();}
                                                });
                                                apply_talent.show();
                                            }
                                        });
                                    }
                                }
                            });

            database = FirebaseDatabase.getInstance();
            DatabaseReference check = database.getReference("donation").child("Activity Post").child(PostKey);
            check.child("Apply User").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        apply_btn.setVisibility(View.GONE);
                        database = FirebaseDatabase.getInstance();
                        database.getReference("donation").child("Activity Post").child(PostKey).child("Apply User")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (User.getUid().equals(snapshot.getValue(String.class))){
                                            chat_btn.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}
                                });
                    } else {
                        apply_btn.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Talent_Chat.class);
                intent.putExtra("hostid", UserID);
                intent.putExtra("posttitle", Title);

                view.getContext().startActivity(intent);
            }
        });
    }

}