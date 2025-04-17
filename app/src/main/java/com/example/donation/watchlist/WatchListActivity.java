package com.example.donation.watchlist;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.donation.R;
import com.example.donation.Talent.TalentListAdapter;
import com.example.donation.Talent.TalentListViewItem;
import com.example.donation.etc.AppCompat;
import com.example.donation.listofwriting.ListOfWritingAdapter;
import com.example.donation.listofwriting.ListOfWritingItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class WatchListActivity extends AppCompat {
    private FirebaseDatabase mDatabase, mDatabase2;          // 파이어 베이스 데이터베이스
    private FirebaseAuth mFirebaseAuth;         // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터 베이스
    private DatabaseReference mDatabaseRef1;     // 실시간 데이터 베이스
    private FirebaseUser User;              // 파이어 베이스 유저
    private Button btn1, btn2;      // 찜 목록, 최근 본 목록
    private RecyclerView list1, list2;  // 찜 목록 리스트, 최근 본 목록 리스트
    private RecyclerView.LayoutManager layoutManager1;  // 찜 목록
    private RecyclerView.LayoutManager layoutManager2;  // 최근 본 목록
    private ArrayList<ListOfWritingItem> arrayList, Watch_arrayList;
    private ArrayList<TalentListViewItem> Watch_arrayList2;
    private RecyclerView.Adapter adapter, Watch_adapter, activity_adapter;
    private ImageView btnmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchlist);

        btnmenu = findViewById(R.id.btnmenu);
        btn1 = (Button) findViewById(R.id.watchlist_btn1);
        btn2 = (Button) findViewById(R.id.watchlist_btn2);
        list1 = (RecyclerView) findViewById(R.id.watchlist_rv1);
        list2 = (RecyclerView) findViewById(R.id.watchlist_rv2);

        list1.setHasFixedSize(true);
        list2.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this);
        layoutManager2 = new LinearLayoutManager(this);
        list1.setLayoutManager(layoutManager1);
        list2.setLayoutManager(layoutManager2);

        arrayList = new ArrayList<>();
        ArrayList<String> pushList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 인스턴스
        User = mFirebaseAuth.getCurrentUser();      // 파이어베이스 유저 확인

        // push값을 가져옵니다.
        DatabaseReference reference = mDatabase.getReference("donation").child("UserAccount")
                .child(User.getUid()).child("HeartList");

        // pushList가 모두 로드된 후에 adapter.notifyDataSetChanged()를 호출하기 위해 addValueEventListener()를 사용합니다.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Heart_dataSnapshot) {
                // arrayList를 초기화합니다.
                arrayList.clear();
                // 최근 본 글의 key값을 가져옵니다
                for (DataSnapshot heart_snapshot : Heart_dataSnapshot.getChildren()) {
                    // 데이터베이스 참조 가져오기
                    String Heart_key = heart_snapshot.getValue(String.class);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference donationRef = database.getReference("donation");
                    DatabaseReference postRef = donationRef.child("post");
                    // 특정 항목이 존재하는지 확인합니다.
                    postRef.child(Heart_key).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                pushList.add(heart_snapshot.getValue(String.class));
                                // post 데이터를 가져옵니다.
                                mDatabase2.getReference("donation").child("post")
                                        .child(heart_snapshot.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot Watch_dataSnapshot) {
                                                ListOfWritingItem watch_item = Watch_dataSnapshot.getValue(ListOfWritingItem.class);
                                                arrayList.add(0, watch_item);

                                                // pushList가 모두 로드되었고 arrayList에 모든 post 데이터가 추가되었으므로 adapter.notifyDataSetChanged()를 호출합니다.
                                                if (pushList.size() == arrayList.size()) {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                            } else {reference.child(heart_snapshot.getValue(String.class)).setValue(null);}
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // adapter를 초기화합니다.
        adapter = new ListOfWritingAdapter(arrayList, this);
        // list1에 adapter를 설정합니다.
        list1.setAdapter(adapter);

        Watch_arrayList = new ArrayList<>();
        Watch_arrayList2 = new ArrayList<>();
        ArrayList<String> Watch_pushList = new ArrayList<>();
        mDatabase2 = FirebaseDatabase.getInstance();

        // push값을 가져옵니다.
        DatabaseReference watch_reference = mDatabase2.getReference("donation").child("UserAccount")
                .child(User.getUid()).child("Watch List");

        // push값을 pushList ArrayList에 추가합니다.

        // pushList가 모두 로드된 후에 adapter.notifyDataSetChanged()를 호출하기 위해 addValueEventListener()를 사용합니다.
        watch_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Watch_dataSnapshot) {
                // arrayList를 초기화합니다.
                Watch_arrayList.clear();
                Watch_arrayList2.clear();
                // 최근 본 글의 key값을 가져옵니다
                for (DataSnapshot watch_snapshot : Watch_dataSnapshot.getChildren()) {
                    // 데이터베이스 참조 가져오기
                    String key = watch_snapshot.getValue(String.class);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference donationRef = database.getReference("donation");
                    DatabaseReference postRef = donationRef.child("post");
                    // 특정 항목이 존재하는지 확인합니다.
                    postRef.child(key).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                    Watch_pushList.add(watch_snapshot.getValue(String.class));
                                    // post 데이터를 가져옵니다.
                                    mDatabase2 = FirebaseDatabase.getInstance();
                                    mDatabase2.getReference("donation").child("post")
                                            .child(watch_snapshot.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot Watch_dataSnapshot) {
                                                    ListOfWritingItem watch_item = Watch_dataSnapshot.getValue(ListOfWritingItem.class);
                                                    Watch_arrayList.add(0, watch_item);

                                                    // pushList가 모두 로드되었고 arrayList에 모든 post 데이터가 추가되었으므로 adapter.notifyDataSetChanged()를 호출합니다.
                                                    if (Watch_pushList.size() == Watch_arrayList.size()) {
                                                        Watch_adapter.notifyDataSetChanged();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });
                            } else {
                                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                                DatabaseReference donationRef2 = database2.getReference("donation");
                                DatabaseReference postRef2 = donationRef2.child("Activity Post");
                                postRef2.child(key).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            Watch_pushList.add(watch_snapshot.getValue(String.class));
                                            mDatabase2 = FirebaseDatabase.getInstance();
                                            mDatabase2.getReference("donation").child("Activity Post")
                                                    .child(watch_snapshot.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            TalentListViewItem talentListViewItem = snapshot.getValue(TalentListViewItem.class);
                                                            Watch_arrayList2.add(0, talentListViewItem);

                                                            if (Watch_pushList.size() == Watch_arrayList2.size()) {
                                                                Watch_adapter.notifyDataSetChanged();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        } else {watch_reference.child(watch_snapshot.getValue(String.class)).setValue(null);}
                                    }
                                });
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // adapter를 초기화합니다.
        Watch_adapter = new ListOfWritingAdapter(Watch_arrayList, this);
        activity_adapter = new TalentListAdapter(Watch_arrayList2, this);
        list2.setAdapter(Watch_adapter);

        // list2에 adapter를 설정합니다.
        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(WatchListActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.watch_list, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.watch_menu1:
                                list2.setAdapter(Watch_adapter);
                                break;
                            case R.id.watch_menu2:
                                list2.setAdapter(activity_adapter);
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

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list1.setVisibility(View.VISIBLE);  // 찜 목록 리스트는 보이게
                list2.setVisibility(View.GONE);     // 최근 본 목록 리스트는 안보이게
                btnmenu.setVisibility(View.GONE);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list1.setVisibility(View.GONE);     // 찜 목록 리스트는 안 보이게
                list2.setVisibility(View.VISIBLE);  // 최근 본 목록 리스트는 보이게
                btnmenu.setVisibility(View.VISIBLE);
            }
        });

        // 뒤로가기 버튼
        ImageView btn_home = (ImageView) findViewById(R.id.watchlist_tb_btn);
        if (btn_home != null){
            btn_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
}
