package com.example.donation;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.donation.Talent.talent_detail;
import com.example.donation.etc.AppCompat;
import com.example.donation.listofwriting.ListOfWritingActivity;
import com.example.donation.listofwriting.ListOfWritingAdapter;
import com.example.donation.listofwriting.ListOfWritingItem;
import com.example.donation.login.LoginActivity;
import com.example.donation.login.UserAccout;
import com.example.donation.notice.NoticeActivity;
import com.example.donation.option.OptionActivity;
import com.example.donation.point.PointListItem;
import com.example.donation.point.PointMainActivity;
import com.example.donation.question.QuestionActivity;
import com.example.donation.Talent.TalentActivity;
import com.example.donation.watchlist.WatchListActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompat {

    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private StorageReference storageReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private  RecyclerView.LayoutManager layoutManager;
    private ArrayList<DonationListItem> arrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseUser User;
    private TextView name, email, myeditpost, joinactivity;
    private ImageView img;
    private CheckBox checkBox;  //자동 로그인 체크박스
    private SharedPreferences sharedPreferences; // SharedPreferences 객체 생성
    private View dialogView;
    int coin1;  // 이전 포인트
    int coin2;  // 보상 받은 포인트
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
        setContentView(R.layout.activity_main);

        name = (TextView) findViewById(R.id.main_name); // 유저 이름
        email = (TextView) findViewById(R.id.main_email); // 유저 이메일
        checkBox = (CheckBox) findViewById(R.id.login_chk); // 자동 로그인 체크박스
        img = (ImageView) findViewById(R.id.activity_main_img); // 유저 이미지
        joinactivity = findViewById(R.id.joinactivity);
        myeditpost = findViewById(R.id.myeditpost);

        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile");
        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccout account = snapshot.getValue(UserAccout.class);
                if (account != null) {
                    if (account.getUserNickName().length() == 0) {
                        name.setText(account.getUserName()+"님");
                    } else if (account.getUserNickName() == null) {
                        name.setText(account.getUserName()+"님");
                    } else {
                        name.setText(account.getUserNickName()+"님");
                    }
                    email.setText(account.getUserID());
                    coin1 = account.getUserPoint();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        storageReference.child(User.getUid()).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MainActivity.this).load(uri).into(img);
            }
        });

        myeditpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyEditPostList.class);
                startActivity(intent);
            }
        });
        joinactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        // 전체화면인 DrawerLayout 객체 참조
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Drawer 화면(뷰) 객체 참조
        final View drawerView = (View) findViewById(R.id.drawer);

        // 드로어 화면을 열고 닫을 버튼 객체 참조
        ImageView btnOpenDrawer = (ImageView) findViewById(R.id.btn_OpenDrawer);
        ImageView btnCloseDrawer = (ImageView) findViewById(R.id.btn_CloseDrawer);

        // 드로어 여는 버튼 리스너
        btnOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });


        // 드로어 닫는 버튼 리스너
        btnCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);
            }
        });

        // 게시글
        TextView btn_list = (TextView) findViewById(R.id.btn_list);
        if (btn_list != null) {
            btn_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ListOfWritingActivity.class);
                    startActivity(intent);
                }
            });
        }

        // 설정
        TextView btn_setting = (TextView) findViewById(R.id.btn_setting);
        if (btn_setting != null) {
            btn_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), OptionActivity.class);
                    startActivity(intent);
                }
            });
        }

        // 공지사항
        TextView btn_notice = (TextView) findViewById(R.id.btn_notice);
        if (btn_notice != null) {
            btn_notice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                    startActivity(intent);
                }
            });
        }

        // 자주 묻는 질문
        TextView btn_question = (TextView) findViewById(R.id.btn_question);
        if (btn_question != null) {
            btn_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                    startActivity(intent);
                }
            });
        }

        // 관심 목록
        TextView btn_like = (TextView) findViewById(R.id.btn_like);
        if (btn_like != null) {
            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), WatchListActivity.class);
                    startActivity(intent);
                }
            });
        }
        // 포인트
        TextView btn_point = (TextView) findViewById(R.id.btn_point);
        if (btn_point != null) {
            btn_point.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), PointMainActivity.class);
                    startActivity(intent);
                }
            });
        }
        // 재능기부
        TextView btn_Talent = (TextView) findViewById(R.id.btn_talent);
        if (btn_Talent != null) {
            btn_Talent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), TalentActivity.class);
                    startActivity(intent);
                }
            });
        }

        // 로그아웃 버튼
        TextView btn_logout = (TextView) findViewById(R.id.btn_logout);
        if (btn_logout != null) {
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 자동 로그인 종료
                    SharedPreferences prefer = getSharedPreferences("temp", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefer.edit();
                    Boolean b = prefer.getBoolean("cb", false);
                    editor.putBoolean("cb", b);
                    editor.apply();

                    mFirebaseAuth.signOut();
                    Toast.makeText(MainActivity.this, "로그아웃 했습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        sharedPreferences = getSharedPreferences("temp", MODE_PRIVATE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",java.util.Locale.getDefault());
        try {
            Date date1 = dateFormat.parse(sharedPreferences.getString("timer",getTime()));
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);    // 이전 종료 시간
            cal2.setTime(mDate);    // 현재 접속 시간
            cal1.add(Calendar.MINUTE, 5); // 분 계산
            Date datebefore = new Date(cal1.getTimeInMillis());
            Date datenow = new Date(cal2.getTimeInMillis());
            Log.d("timer1",datebefore.toString());
            Log.d("timer2",datenow.toString());
            // "isInit" 값 가져오기 (기본값은 false)
            if(datenow.before(datebefore)){
                Log.d("timer",datenow+"은"+datebefore+"보다 이전입니다.");
            }else{
                Log.d("timer",datenow+"은"+datebefore+"보다 나중입니다.");
                boolean isInit = sharedPreferences.getBoolean("isInit", false);
                if (!isInit) {
                    // 최초 실행 시 다이얼로그 생성 및 띄우기
                    showAlertDialog();
                    Log.d("test-log", "최초 실행");
                    // "isInit" 값을 true로 설정하여 최초 실행으로 표시
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isInit", true);
                    editor.apply();
                }
            }
        }catch (Exception e){}


        // 게시글 표시 & 클릭 이벤트
        recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 인스턴스
        User = mFirebaseAuth.getCurrentUser();      // 파이어베이스 유저 확인

        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                .child("User Donation");
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonationListItem item = snapshot.getValue(DonationListItem.class);
                    arrayList.add(0,item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new DonationAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.main_swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                overridePendingTransition(0,0);
                finish();
                startActivity(intent);
                overridePendingTransition(0,0);
                Toast.makeText(getApplicationContext(),"새로고침 하였습니다.", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    // 다이얼 로그 보기
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogView = (View) View.inflate(MainActivity.this, R.layout.activity_main_dialog, null);
        builder.setView(dialogView).setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button btn1 = dialogView.findViewById(R.id.main_dialog_btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTime = getTime(); //시간
                int rewardAmount = 5;
                coin2 = coin1 + rewardAmount;
                mDatabase = FirebaseDatabase.getInstance();
                mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid());
                mDatabaseRef.child("userPoint").setValue(coin2);
                PointListItem pointListItem = new PointListItem();
                pointListItem.setPoint(String.valueOf(rewardAmount));
                pointListItem.setTitle("메인화면 광고 보상");
                pointListItem.setTime(strTime);
                pointListItem.setPostId(User.getUid());
                mDatabase = FirebaseDatabase.getInstance();
                mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                        .child("Donation List").push().setValue(pointListItem);
                Toast.makeText(getApplicationContext(), "포인트 "+rewardAmount+"를 받았습니다.", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티가 파괴되지 않았는지 확인
        if (!isFinishing()) {
            // Glide 작업 중지
            Glide.with(this).onDestroy();
        }
    }
}