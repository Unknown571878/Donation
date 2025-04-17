package com.example.donation.Talent_Chat_;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.example.donation.listofwriting.ListOfWritingItem;
import com.example.donation.login.UserAccout;
import com.example.donation.profile_pg_img;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Talent_Chat extends AppCompat {
    private Uri uri;
    private ImageView img;
    private RecyclerView chat_list;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ChatAdapter> arrayList;
    private RecyclerView.Adapter adapter;
    private ArrayList<ChatData> chatdatalist;
    private EditText gettext;
    private TextView activateusernickname;
    private Button sendtext, img_add;
    private ImageView backbtn;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;   // 스토리지 참조 만들기
    private FirebaseDatabase database;
    private FirebaseUser User;
    private FirebaseAuth mFirebaseAuth;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);

        activateusernickname = findViewById(R.id.activateuserNickname);
        backbtn = findViewById(R.id.chat_list_tb_btn);
        chat_list = findViewById(R.id.chat_recyclerview);
        chat_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        chat_list.setLayoutManager(layoutManager);
        gettext = findViewById(R.id.edt_chat);  // 채팅 내용
        String hostID = getIntent().getStringExtra("hostid");
        String PostTitle = getIntent().getStringExtra("posttitle");
        sendtext = findViewById(R.id.chat_send);    // 전송
        img_add = (Button) findViewById(R.id.chat_list_btn1);    // 이미지 추가 버튼
        img = (ImageView) findViewById(R.id.chat_list_img);  // 이미지
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("donation").child("Activity Chat").child(PostTitle);
        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("chat").child(User.getUid());
        ChatData chat = new ChatData();



        database = FirebaseDatabase.getInstance();
        database.getReference("donation").child("UserAccount").child(User.getUid())
                .child("userNickName").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String NickName = snapshot.getValue(String.class);
                            activateusernickname.setText(NickName);

                            backbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            });
                            sendtext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String msg = gettext.getText().toString();
                                    String key = databaseReference.push().getKey();
                                    if (msg.length() != 0) {
                                        chat.setMsg(msg);
                                        chat.setKey(key);
                                        chat.setUserId(User.getUid());
                                        chat.setTime(getTime());
                                        chat.setNickname(NickName);
                                        if(uri != null) {
                                            chat.setImg(uri.toString());
                                            storageReference.child(key).putFile(uri);
                                            uri = null;
                                        } else
                                            chat.setImg("");
                                        databaseReference.push().setValue(chat);
                                    } else if (uri != null) {
                                        chat.setMsg(msg);
                                        chat.setUserId(User.getUid());
                                        chat.setKey(key);
                                        chat.setTime(getTime());
                                        chat.setNickname(NickName);
                                        chat.setImg(uri.toString());
                                        storageReference.child(key).putFile(uri);
                                        uri = null;
                                        databaseReference.push().setValue(chat);
                                    }
                                    gettext.setText("");
                                    img.setVisibility(View.GONE);
                                    img.setImageURI(null);
                                }
                            });
                            img_add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    img.setVisibility(View.VISIBLE);
                                    select();
                                }
                            });
                            chatdatalist = new ArrayList<>();
                            adapter = new ChatAdapter(chatdatalist, Talent_Chat.this, NickName);
                            chat_list.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatData chatData = snapshot.getValue(ChatData.class);
                // 채팅 메시지 추가
                ((ChatAdapter) adapter).addChat(chatData);
                chat_list.scrollToPosition(chat_list.getAdapter().getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // 선택
    private void select() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            img.setImageURI(uri);
            grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }
}
