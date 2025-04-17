package com.example.donation.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.donation.MainActivity;
import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.point.PointListItem;
import com.example.donation.point.PointMainActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompat {
    private FirebaseAuth mFirebaseAuth;         // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터 베이스
    private FirebaseUser User;                  // 파이어 베이스 유저
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private EditText login_edt1, login_edt2;    // ID, PW 입력
    private Button login_btn1;      // 로그인 버튼
    private TextView login_tv1, login_tv2;      // ID/PW 찾기, 회원가입 버튼
    private CheckBox checkBox;                      // 자동 로그인 체크 박스
    private InterstitialAd mInterstitialAd;  // 광고 리워드
    private String TAG = "Google";  // 로그 메이지
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
        setContentView(R.layout.login);

        login_edt1 = (EditText) findViewById(R.id.login_edt_1); // ID 입력
        login_edt2 = (EditText) findViewById(R.id.login_edt_2); // 비밀번호 입력
        login_btn1 = (Button) findViewById(R.id.login_btn1);    // 로그인 버튼
        login_tv1 = (TextView) findViewById(R.id.login_tv1);    // ID/PW 찾기
        login_tv2 = (TextView) findViewById(R.id.login_tv2);    // 회원가입 버튼
        checkBox = (CheckBox) findViewById(R.id.login_chk);         // 자동 로그인 체크박스

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("donation");
        User = mFirebaseAuth.getCurrentUser();


        // 로그인 버튼
        login_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_edt1.length() == 0 && login_edt2.length() == 0) {
                    Toast.makeText(LoginActivity.this, "아이디 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (login_edt2.length() == 0) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (login_edt1.length() == 0) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // 로그인 요청
                    String strID = login_edt1.getText().toString();
                    String strPW = login_edt2.getText().toString();
                    mFirebaseAuth.signInWithEmailAndPassword(strID, strPW).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 로그인 성공
                                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                //startActivity(intent);
                                //finish(); // 현재 액티비티 삭제
                                User = mFirebaseAuth.getCurrentUser();
                                mDatabaseRef.child("UserAccount").child(User.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        UserAccout account = snapshot.getValue(UserAccout.class);
                                        if (account != null) {
                                            coin1 = account.getUserPoint();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                                if (mInterstitialAd != null) {
                                    mInterstitialAd.show(LoginActivity.this);
                                } else {
                                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // 회원 가입 버튼
        login_tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원 가입 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), JoinMemberActivity.class);
                startActivity(intent);
            }
        });

        // 비밀번호 찾기
        login_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindIdPassword.class);
                startActivity(intent);
            }
        });
        // 광고 관련 코드
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadAdsRequest();
            }
        });
    }
    // 어플 시작시 호출
    @Override
    public void onStart() {
        super.onStart();
        checkBox = (CheckBox) findViewById(R.id.login_chk);// 자동 로그인 체크박스
        SharedPreferences sp = getSharedPreferences("temp", MODE_PRIVATE);
        Boolean b = sp.getBoolean("cb", false);
        checkBox.setChecked(b);

        if (checkBox.isChecked() == true) {
            User = mFirebaseAuth.getCurrentUser();
            if (User != null) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }

    }
    // 종료시
    protected void onStop(){
        super.onStop();
        SharedPreferences prefer = getSharedPreferences("temp", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefer.edit();
        editor.putBoolean("cb", checkBox.isChecked());
        editor.putBoolean("isInit", false);
        editor.putString("timer",getTime());
        editor.apply();

    }
    private void loadAdsRequest(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712",
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d(TAG, "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                String strTime = getTime(); //시간
                                int rewardAmount = 5;
                                coin2 = coin1 + rewardAmount;
                                mDatabaseRef.child("UserAccount").child(User.getUid()).child("userPoint").setValue(coin2);
                                PointListItem pointListItem = new PointListItem();
                                pointListItem.setPoint(String.valueOf(rewardAmount));
                                pointListItem.setTitle("로그인 광고 보상");
                                pointListItem.setTime(strTime);
                                pointListItem.setPostId(User.getUid());
                                mDatabase = FirebaseDatabase.getInstance();
                                mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                        .child("Donation List").push().setValue(pointListItem);
                                Toast.makeText(getApplicationContext(), "포인트 "+rewardAmount+"를 받았습니다.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                Log.d(TAG, "Ad dismissed fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(TAG, "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad showed fullscreen content.");
                            }
                        });
                    }
                });
    }
}

