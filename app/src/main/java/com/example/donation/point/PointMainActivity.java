package com.example.donation.point;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.login.UserAccout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PointMainActivity extends AppCompat {
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    private FirebaseUser User;              // 파이어 베이스 유저
    private Button btn1, btn2, btn3;   // 기부 점수 현황, 포인트 내역, 광고 보기
    private ImageView tb_back;  // 뒤로가기
    private RewardedAd rewardedAd;  // 광고 리워드
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_main_view);

        btn1 = (Button) findViewById(R.id.point_main_view_btn1);    // 기부 점수 현황
        btn2 = (Button) findViewById(R.id.point_main_view_btn2);    // 포인트 내역
        btn3 = (Button) findViewById(R.id.point_main_view_btn3);    // 광고보기
        tb_back = (ImageView) findViewById(R.id.point_main_view_tb);    // 뒤로가기
        String strTime = getTime(); //시간

        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("donation").child("UserAccount").child(User.getUid());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
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

        // 기부 점수 현황
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PointMainActivity.this, DonationScore.class);
                startActivity(intent);
            }
        });

        // 포인트 내역
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PointMainActivity.this, Point_home.class);
                startActivity(intent);
            }
        });

        // 뒤로가기
        tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 광고 관련 코드
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadAdsRequest();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rewardedAd != null) {
                    rewardedAd.show(PointMainActivity.this, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            Log.d(TAG, "The user earned the rewrad");
                            int rewardAmount = rewardItem.getAmount();
                            coin2 = coin1 + rewardAmount;
                            mDatabaseRef.child("userPoint").setValue(coin2);
                            PointListItem pointListItem = new PointListItem();
                            pointListItem.setPoint(String.valueOf(rewardAmount));
                            pointListItem.setTitle("동영상 광고 보상");
                            pointListItem.setTime(strTime);
                            pointListItem.setPostId(User.getUid());
                            mDatabase = FirebaseDatabase.getInstance();
                            mDatabase.getReference("donation").child("UserAccount").child(User.getUid())
                                    .child("Donation List").push().setValue(pointListItem);
                            Toast.makeText(PointMainActivity.this, "포인트"+rewardAmount+"을 받았습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    btn3.setText("현재 없음");
                    Log.d(TAG, "현재 광고가 없습니다.");
                }
            }
        });
    }
    // 광고
    private void loadAdsRequest(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                    }
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d(TAG, "Ad was clicked.");
                            }

                            // X 버튼 누르면 동작
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad dismissed fullscreen content.");
                                rewardedAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.");
                                rewardedAd = null;
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
