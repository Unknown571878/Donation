package com.example.donation.listofwriting;

import android.content.Intent;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.writing.writing_pg;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;

public class ListOfWritingActivity extends AppCompat {
    private FirebaseAuth mFirebaseAuth;         // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터 베이스
    private FirebaseUser User;              // 파이어 베이스 유저
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private  RecyclerView.LayoutManager layoutManager;
    private ArrayList<ListOfWritingItem> arrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private Button creatett;        // 글쓰기 버튼
    Toolbar tb;
    ImageView Mw;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_writing);

        tb = findViewById(R.id.tb_main);

        setSupportActionBar(tb);
        getSupportActionBar().setTitle("");

        // 글쓰기 버튼
        creatett = (Button) findViewById(R.id.writing_page);
        creatett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), writing_pg.class);
                startActivity(intent);
            }
        });

        Mw = findViewById(R.id.main_window);
        Mw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 게시글 표시 & 클릭 이벤트
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.donation_gauge);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 인스턴스
        User = mFirebaseAuth.getCurrentUser();      // 파이어베이스 유저 확인

        mDatabaseRef = database.getReference("donation").child("post");
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    ListOfWritingItem item = snapshot.getValue(ListOfWritingItem.class);
                    arrayList.add(0,item);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ListOfWritingActivity", String.valueOf(error.toException()));
            }
        });
        adapter = new ListOfWritingAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        // 새로고침
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
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

    public ArrayList<ListOfWritingItem> getPosts(String region) {
        // 지역을 기반으로 게시글 목록을 필터링한다.
        ArrayList<ListOfWritingItem> posts = new ArrayList<>();
        for (ListOfWritingItem post : arrayList) {
            if (post.getRegion().equals(region)) {
                posts.add(post);
            }
        }

        return posts;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.option_sort, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu1:

                adapter.notifyDataSetChanged();
                break;

            case R.id.menu1_1:
                String region1_1 = "서울";
                ArrayList<ListOfWritingItem> posts1_1 = getPosts(region1_1);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_1, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_2:
                String region1_2 = "경기";
                ArrayList<ListOfWritingItem> posts1_2 = getPosts(region1_2);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_2, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_3:
                String region1_3 = "강원";
                ArrayList<ListOfWritingItem> posts1_3 = getPosts(region1_3);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_3, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_4:
                String region1_4 = "인천";
                ArrayList<ListOfWritingItem> posts1_4 = getPosts(region1_4);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_4, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_5:
                String region1_5 = "대구";
                ArrayList<ListOfWritingItem> posts1_5 = getPosts(region1_5);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_5, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_6:
                String region1_6 = "광주";
                ArrayList<ListOfWritingItem> posts1_6 = getPosts(region1_6);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_6, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_7:
                String region1_7 = "부산";
                ArrayList<ListOfWritingItem> posts1_7 = getPosts(region1_7);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_7, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_8:
                String region1_8 = "울산";
                ArrayList<ListOfWritingItem> posts1_8 = getPosts(region1_8);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_8, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_9:
                String region1_9 = "대전";
                ArrayList<ListOfWritingItem> posts1_9 = getPosts(region1_9);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_9, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_10:
                String region1_10 = "충북";
                ArrayList<ListOfWritingItem> posts1_10 = getPosts(region1_10);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_10, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_11:
                String region1_11 = "충남";
                ArrayList<ListOfWritingItem> posts1_11 = getPosts(region1_11);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_11, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_12:
                String region1_12 = "전북";
                ArrayList<ListOfWritingItem> posts1_12 = getPosts(region1_12);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_12, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_13:
                String region1_13 = "전남";
                ArrayList<ListOfWritingItem> posts1_13 = getPosts(region1_13);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_13, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_14:
                String region1_14 = "경북";
                ArrayList<ListOfWritingItem> posts1_14 = getPosts(region1_14);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_14, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_15:
                String region1_15 = "경남";
                ArrayList<ListOfWritingItem> posts1_15 = getPosts(region1_15);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_15, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu1_16:
                String region1_16 = "제주";
                ArrayList<ListOfWritingItem> posts1_16 = getPosts(region1_16);

                // 게시글 목록을 업데이트
                adapter = new ListOfWritingAdapter(posts1_16, ListOfWritingActivity.this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.menu2:
                arrayList.sort(new Comparator<ListOfWritingItem>() {
                    @Override
                    public int compare(ListOfWritingItem o1, ListOfWritingItem o2) {
                        return o2.getDateAndTime().compareTo(o1.getDateAndTime());
                    }
                });

                adapter.notifyDataSetChanged();
                break;


            case R.id.menu3:

                arrayList.sort(new Comparator<ListOfWritingItem>() {
                    @Override
                    public int compare(ListOfWritingItem o1, ListOfWritingItem o2) {
                        return o1.getDateAndTime().compareTo(o2.getDateAndTime());
                    }
                });

                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}