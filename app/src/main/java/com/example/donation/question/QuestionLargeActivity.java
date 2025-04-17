package com.example.donation.question;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.donation.R;
import com.example.donation.etc.AppCompat;

public class QuestionLargeActivity extends AppCompat {
    private TextView title, story;      // 제목, 내용
    private ImageView question_tb_btn;  // 뒤로가기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_view_large);

        question_tb_btn = (ImageView) findViewById(R.id.question_view_tb_btn);
        question_tb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.question_view_large_title);
        story = (TextView) findViewById(R.id.question_view_large_story);

        Intent intent = this.getIntent();

        title.setText(intent.getStringExtra("title"));
        story.setText(intent.getStringExtra("story"));
    }
}
