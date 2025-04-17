package com.example.donation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class MyEditPostList extends AppCompat {
    private FirebaseDatabase mDatabase;      // 파이어 베이스 데이터베이스
    private FirebaseAuth mFirebaseAuth;         // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터 베이스
    private FirebaseUser User;              // 파이어 베이스 유저
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ListOfWritingItem> arrayList;
    private RecyclerView.Adapter adapter;
    private ImageView mw;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myeditpostlist);

        mw = findViewById(R.id.main_window_);
        mw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}});

        recyclerView = findViewById(R.id.mypostrecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();
        ArrayList<String> donationList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 인스턴스
        User = mFirebaseAuth.getCurrentUser();      // 파이어베이스 유저 확인

        DatabaseReference reference = mDatabase.getReference("donation").child("UserAccount")
                .child(User.getUid()).child("userpost");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot usereditpostlist : dataSnapshot.getChildren()) {
                    // 데이터베이스 참조 가져오기
                    String post_key = usereditpostlist.getValue(String.class);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference donationRef = database.getReference("donation");
                    DatabaseReference postRef = donationRef.child("post");
                    postRef.child(post_key).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                donationList.add(usereditpostlist.getValue(String.class));
                                mDatabase.getReference("donation").child("post").child(usereditpostlist.getValue(String.class))
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ListOfWritingItem item = snapshot.getValue(ListOfWritingItem.class);
                                                arrayList.add(0, item);

                                                if (donationList.size() == arrayList.size()) {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            } else {
                                reference.child(usereditpostlist.getValue(String.class)).setValue(null);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adapter = new ListOfWritingAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        // 새로고침
        swipeRefreshLayout = findViewById(R.id.mypostswiperefreshlayout);
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
}
