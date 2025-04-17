package com.example.donation.customer_operational;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.donation.R;
import com.example.donation.etc.AppCompat;

public class CustomerOperationalLarge1Activity extends AppCompat {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_operational_view_large);

        ImageView img_btn = (ImageView) findViewById(R.id.customer_op_tb_btn);
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.cu_op_view_large_title);
        TextView story = (TextView) findViewById(R.id.cu_op_view_large_story);

        Intent intent = this.getIntent();

        title.setText(intent.getStringExtra("title1"));
        story.setText(intent.getStringExtra("story1"));
    }
}
