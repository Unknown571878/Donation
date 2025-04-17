package com.example.donation.notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.donation.R;
import com.example.donation.etc.AppCompat;

public class NoticeLargeActivity extends AppCompat {
    private ImageView notice_tb_btn;    // 뒤로가기
    private TextView title, story;      // 제목, 내용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_view_large);

        notice_tb_btn = (ImageView) findViewById(R.id.notice_view_tb_btn);
        notice_tb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.notice_view_large_title);
        story = (TextView) findViewById(R.id.notice_view_large_story);

        Intent intent = this.getIntent();

        title.setText(intent.getStringExtra("title"));
        story.setText(intent.getStringExtra("story"));
    }
}
