package com.example.donation.question;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.donation.etc.AppCompat;
import com.example.donation.R;
import com.example.donation.notice.NoticeActivity;
import com.example.donation.notice.NoticeLargeActivity;

public class QuestionActivity extends AppCompat {

    private ListView listView1;
    private QuestionListViewAdapter adapter1;
    private Button button1;
    private TextView hid,ntitle,nstory;
    String[] title = {"[질문] 내가 학교에 가서도 잠이 오는 이유는?","[질문] 내가 학교에 가서도 잠이 오는 이유는?","[질문] 내가 학교에 가서도 잠이 오는 이유는?","[질문] 내가 학교에 가서도 잠이 오는 이유는?",
            "[질문] 내가 학교에 가서도 잠이 오는 이유는?","[질문] 내가 학교에 가서도 잠이 오는 이유는?","[질문] 내가 학교에 가서도 잠이 오는 이유는?"};
    String[] date = {"2023-09-09","2023-09-09","2023-09-09","2023-09-09","2023-09-09","2023-09-09","2023-09-09"};
    String[] story = {"오늘 나는 아직도 학굑에 있다","오늘 나는 아직도 학굑에 있다","오늘 나는 아직도 학굑에 있다","오늘 나는 아직도 학굑에 있다","오늘 나는 아직도 학굑에 있다",
            "오늘 나는 아직도 학굑에 있다","오늘 나는 아직도 학굑에 있다"};
    String[] title1 ={"[공지] 2023년 대학생 교육기부자 모집 안내"};
    String[] date1 = {"2023-10-11"};
    String[] story1 ={"안녕하세요, 귀한 학생 여러분! 우리 대학은 교육의 중요성을 높이 평가하며, 이를 실현하기 위해 귀하의 기부와 참여를 기다립니다. 2023년 교육기부자 모집을 진행하며, 함께 꿈을 키우고 미래를 밝게 비춰보고자 합니다. 교육 경험을 나누고, 지식과 열정을 전달하세요. 여러분의 기부로 더 많은 학생들에게 교육의 기회를 제공할 수 있습니다. 저희와 함께 더 나은 사회를 만들어갈 여러분의 참여를 기대합니다. 교육에 대한 열정을 함께 키워보시겠어요? 자세한 정보와 지원 방법은 웹사이트를 방문하여 확인해 주세요. 함께하는 여정을 기대합니다!"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_view);

        adapter1 = new QuestionListViewAdapter();

        listView1 = (ListView) findViewById(R.id.question_listView_question);
        listView1.setAdapter(adapter1);

        button1 = (Button)  findViewById(R.id.question_view_button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                startActivity(intent);
            }
        });

        ImageView question_tb_btn = (ImageView) findViewById(R.id.question_view_tb_btn);
        question_tb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(QuestionActivity.this, QuestionLargeActivity.class);
                intent.putExtra("title", title[position]);
                intent.putExtra("story",story[position]);
                startActivity(intent);
            }
        });

        for(int i=0;i<title.length;i++){
            adapter1.addItem(title[i],date[i]);
        }
        // 공지사항 및 작은 부분
        ntitle = (TextView) findViewById(R.id.question_listview_item_title);
        nstory = (TextView) findViewById(R.id.question_listview_item_content);
        hid = (TextView) findViewById(R.id.question_view_hid);
        ntitle.setText(title1[0]);
        nstory.setText(date1[0]);

        hid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionActivity.this, QuestionLargeActivity.class);
                intent.putExtra("title",title1[0]);
                intent.putExtra("story",story1[0]);
                startActivity(intent);
            }
        });

    }

}
