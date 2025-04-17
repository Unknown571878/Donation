package com.example.donation.writing;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.donation.listofwriting.ListOfWritingActivity;
import com.example.donation.login.UserAccout;
import com.example.donation.price_determin.Price_determine;
import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.profile_pg;
import com.example.donation.watchlist.HeartList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class WritingViewActivity extends AppCompat {

    private FirebaseDatabase mDatabase;          // 파이어 베이스 데이터베이스
    private FirebaseAuth mFirebaseAuth;         // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터 베이스
    private FirebaseUser User;                  // 파이어 베이스 유저
    private StorageReference storageReference;
    Button GibuButton, btn1;
    ImageView mainImg,Fimage,Edit_Post,userImg;
    TextView viewmoney, viewtime,viewtitle,viewdetail,viewusernickname, donationMoney,
    user_region, received_money, donation_finish;
    RelativeLayout hide;
    SwipeRefreshLayout writing_swiperefreshlayout;
    ProgressBar donationpercent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_view);

        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("donation");
        mDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile");

        viewmoney = (TextView) findViewById(R.id.Viewmoney);       // 금액
        viewtime = (TextView) findViewById(R.id.Viewdateandtime);  // 시간
        viewtitle = (TextView) findViewById(R.id.Viewtitle);       // 제목
        viewdetail = (TextView) findViewById(R.id.Viewdetail);     // 내용
        viewusernickname = (TextView) findViewById(R.id.writing_view_nickname);    // 게시자 닉네임
        Fimage = (ImageView) findViewById(R.id.writing_tool_btn); // 뒤로가기 버튼
        mainImg = (ImageView) findViewById(R.id.writing_view_img);// 게시글 이미지
        userImg = (ImageView) findViewById(R.id.writing_view_userimg);  // 사용자 이미지
        GibuButton = findViewById(R.id.GibuButton);
        btn1 = (Button) findViewById(R.id.writing_view_heart);     // 찜하기 버튼
        Edit_Post = findViewById(R.id.Editpostmenu);               // 게시글 수정 메뉴
        donationpercent = findViewById(R.id.money_progress);
        donationMoney = findViewById(R.id.money_percent);
        user_region = findViewById(R.id.writing_view_region);
        received_money = findViewById(R.id.received_money);
        donation_finish = findViewById(R.id.donation_finish_detail);
        hide = findViewById(R.id.writing_detail_progress);
        writing_swiperefreshlayout = findViewById(R.id.writing_swiperefreshlayout);

        writing_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                overridePendingTransition(0,0);
                finish();
                startActivity(intent);
                overridePendingTransition(0,0);
                Toast.makeText(getApplicationContext(),"새로고침 하였습니다.", Toast.LENGTH_SHORT).show();
                writing_swiperefreshlayout.setRefreshing(false);
            }
        });


        // 뒤로가기
        Fimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });

        String Title = getIntent().getStringExtra("title");
        String Money = getIntent().getStringExtra("money");
        String Dateandtime = getIntent().getStringExtra("dateandtime");
        String Detail = getIntent().getStringExtra("detail");
        String userNickName = getIntent().getStringExtra("username");
        String img = getIntent().getStringExtra("img");
        String listid = getIntent().getStringExtra("listid");
        String PostKey = getIntent().getStringExtra("postkey");
        String userimg = getIntent().getStringExtra("userimg");
        String UserID = getIntent().getStringExtra("userID");
        String DonationMoney = getIntent().getStringExtra("DonationMoney");
        String Region = getIntent().getStringExtra("region");

        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference("donation").child("post").child(PostKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        WritingInfo writingInfo = snapshot.getValue(WritingInfo.class);
                        if (writingInfo != null){
                            String receivedMoneyString = String.valueOf(writingInfo.getReceive_Money());
                            received_money.setText(receivedMoneyString);
                            if (writingInfo.getReceive_Money() == -1 ){
                                donation_finish.setVisibility(View.VISIBLE);
                                hide.setVisibility(View.GONE);
                                GibuButton.setVisibility(View.GONE);
                                btn1.setVisibility(View.GONE);
                            } else {
                                double r_money = Double.parseDouble(received_money.getText().toString());
                                int int_r_money = (int) r_money; // 정수로 변환할 때 필요한 부분

                                double num = r_money / Double.valueOf(Money) * 100 ;
                                String percent = String.valueOf(num);

                                donationMoney.setText(percent);
                                donationpercent.setProgress((int) num);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        viewtitle.setText(Title);
        viewmoney.setText(Money);
        viewtime.setText(Dateandtime);
        viewdetail.setText(Detail);
        viewusernickname.setText(userNickName);
        user_region.setText(Region);
        storageReference.child(UserID).child(listid).child("postimage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(WritingViewActivity.this).load(uri).into(mainImg);
            }
        });
        storageReference.child(UserID).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(WritingViewActivity.this).load(uri).into(userImg);
            }
        });


        if (User != null && UserID != null){
            String UID = User.getUid();
            if(UserID.equals(UID)){
                GibuButton.setVisibility(GibuButton.INVISIBLE);
                btn1.setVisibility(btn1.INVISIBLE);
            }
        }

        // 기부 버튼
        GibuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Price_determine.class);
                intent.putExtra("thispost", PostKey);
                intent.putExtra("thistitle", Title);
                intent.putExtra("postuserID", UserID);
                intent.putExtra("PostUserNickname", userNickName);
                startActivity(intent);
            }
        });

        // 찜하기
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference donationRef = database.getReference("donation");
                DatabaseReference postRef = donationRef.child("UserAccount").child(User.getUid())
                        .child("HeartList");
                postRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot datasnapshot) {
                        if (datasnapshot.exists()){
                            mDatabase = FirebaseDatabase.getInstance();
                            mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                    .child("HeartList")
                                    .child(PostKey).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        AlertDialog.Builder listdelete = new AlertDialog.Builder(view.getContext());
                                        listdelete.setTitle("찜 목록 제거");
                                        listdelete.setMessage("헤당 게시물의 찜을 취소하시겠습니까?");
                                        listdelete.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                mDatabase = FirebaseDatabase.getInstance();
                                                mDatabase.getReference("donation").child("UserAccount")
                                                        .child(User.getUid()).child("HeartList")
                                                        .child(PostKey).setValue(null);
                                                Toast.makeText(view.getContext(), "찜 취소하셨습니다",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        listdelete.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {dialog.cancel();}
                                        });
                                        listdelete.show();
                                    } else {
                                        mDatabase = FirebaseDatabase.getInstance();
                                        mDatabase.getReference("donation").child("UserAccount")
                                                .child(User.getUid()).child("HeartList").child(PostKey).setValue(PostKey);
                                        Toast.makeText(WritingViewActivity.this, "찜했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            mDatabase = FirebaseDatabase.getInstance();
                            mDatabase.getReference("donation").child("UserAccount")
                                    .child(User.getUid()).child("HeartList").child(PostKey).setValue(PostKey);
                            Toast.makeText(WritingViewActivity.this, "찜했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Map<String, Object> data = new HashMap<>();
                data.put("id", User.getUid());
                data.put("userName", userNickName);
                data.put("detail", Detail);
                data.put("title", Title);
                data.put("money", Money);
                data.put("dateAndTime", Dateandtime);
                //data.put("clickCount", ClickCount);
                data.put("img",img);
                mDatabase.getReference("donation").child("UserAccount").child(User.getUid()).child("likedBy").child(listid).setValue(data);
                //mDatabase.getReference("donation").child("post").child(listid).child("likedBy").child(User.getUid()).setValue("true");
            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        if (User != null){
            if (User.getUid().equals(UserID)) {
                Edit_Post.setVisibility(View.VISIBLE);
                Edit_Post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(WritingViewActivity.this, view);
                        popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.action_menu1:
                                        Intent intent = new Intent(view.getContext(), Writing_change.class);
                                        intent.putExtra("title", Title);
                                        intent.putExtra("money", Money);
                                        intent.putExtra("dateandtime", Dateandtime);
                                        intent.putExtra("detail", Detail);
                                        intent.putExtra("img", img);
                                        intent.putExtra("userimg",userimg);
                                        intent.putExtra("postkey", PostKey);
                                        intent.putExtra("userID", UserID);
                                        intent.putExtra("username", userNickName);
                                        intent.putExtra("DonationMoney",DonationMoney);
                                        startActivity(intent);
                                        break;
                                    case R.id.action_menu2:
                                        AlertDialog.Builder postdelete = new AlertDialog.Builder(view.getContext());
                                        postdelete.setTitle("게시글 삭제");
                                        postdelete.setMessage("정말로 게시글을 삭제하시겠습니까?");
                                        postdelete.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                mDatabase = FirebaseDatabase.getInstance();
                                                mDatabase.getReference("donation").child("post").child(PostKey)
                                                        .removeValue();
                                                Toast.makeText(view.getContext(), "게시글을 삭제하였습니다.",
                                                        Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                        postdelete.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(view.getContext(), "취소하였습니다.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        postdelete.show();
                                        break;
                                    default:
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            } else {
                Edit_Post.setVisibility(View.GONE);
            }
        }
        // 메뉴 버튼

    }
}
