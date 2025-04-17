package com.example.donation.login;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.donation.R;
import com.example.donation.etc.AppCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class JoinMemberActivity extends AppCompat {
    private Spinner spinner1, spinner2, spinner3, spinner4;
    private FirebaseAuth mFirebaseAuth;     // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    int ecount = 6;
    private EditText edt[] = new EditText[ecount]; //회원가입 입력필드
    private Button btn1, btn2;    // 회원가입 버튼, 아이디 중복확인
    private TextView tv1, tv2, tv3, tv4, tv5;   // 안보이는 텍스트뷰
    private ImageView img, tb_btn;  // 안보이는 프로필 이미지, 뒤로가기 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinmember);

        // 파이어 베이스
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("donation");
        UserAccout account = new UserAccout();

        btn1 = (Button) findViewById(R.id.joinmember_btn1); // 회원 가입 버튼
        btn2 = (Button) findViewById(R.id.joinmember_btn2); // 아이디 중복 확인 버튼
        tv1 = (TextView) findViewById(R.id.joinmember_tv1); // 연도
        tv2 = (TextView) findViewById(R.id.joinmember_tv2); // 월
        tv3 = (TextView) findViewById(R.id.joinmember_tv3); // 일
        tv4 = (TextView) findViewById(R.id.joinmember_tv4); // 성별
        tv5 = (TextView) findViewById(R.id.joinmember_tv5); // 닉네임
        img = (ImageView) findViewById(R.id.joinmember_img);// 프로필 이미지

        Integer edtID[] = {R.id.joinmember_edt1,R.id.joinmember_edt2,R.id.joinmember_edt3,
                        R.id.joinmember_edt4,R.id.joinmember_edt5,R.id.joinmember_edt6};
        for(int i = 0; i<ecount; i++){
            edt[i] = (EditText) findViewById(edtID[i]);
        }

        // 스피너 삽입
        setSpinner();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strID = edt[0].getText().toString();
                String strPW = edt[1].getText().toString();
                String strPWck = edt[2].getText().toString();
                String strName = edt[3].getText().toString();
                String strNickName = edt[4].getText().toString();
                String strPhone = edt[5].getText().toString();
                String strYear = tv1.getText().toString();
                String strMonth = tv2.getText().toString();
                String strDay = tv3.getText().toString();
                String strSex = tv4.getText().toString();
                // 회원 가입 처리 시작
                account.setUserName(strName);
                account.setUserYear(strYear);
                account.setUserMonth(strMonth);
                account.setUserDay(strDay);
                account.setUserNickName(strNickName);
                account.setUserPhone(strPhone);
                account.setUserSex(strSex);
                account.setUserImg("content://com.android.externalstorage.documents/document/primary%3APictures%2Fmain_img.png");

                String result = "숫자입니다.";
                for(int i=0;i<strPhone.length();i++){
                    char c = strPhone.charAt(i);
                    if(c<48 || c> 57){//숫자가 아닌 경우
                        result = "문자가 포함됨";
                        break;
                    }
                }

                if(strID.length()==0){
                    Toast.makeText(JoinMemberActivity.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if (strPW.length()<=5){
                    Toast.makeText(JoinMemberActivity.this, "비밀번호를 6자리이상 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if (strName.length()<=1) {
                    Toast.makeText(JoinMemberActivity.this, "이름을 한글자 이상 쓰세요. ", Toast.LENGTH_SHORT).show();
                }else if (strNickName.length()==0){
                    Toast.makeText(JoinMemberActivity.this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if (strPhone.length()!=11){
                    Toast.makeText(JoinMemberActivity.this, "휴대폰 번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show();
                }else if (strYear.equals("연도")){
                    Toast.makeText(JoinMemberActivity.this, "연도를 다시 고르세요", Toast.LENGTH_SHORT).show();
                }else if (strMonth.equals("월")){
                    Toast.makeText(JoinMemberActivity.this, "월을 다시 고르세요.", Toast.LENGTH_SHORT).show();
                }else if (strDay.equals("일")){
                    Toast.makeText(JoinMemberActivity.this, "일을 다시 고르세요.", Toast.LENGTH_SHORT).show();
                }else if (strSex.equals("성별")){
                    Toast.makeText(JoinMemberActivity.this, "성별을 다시 고르세요.", Toast.LENGTH_SHORT).show();
                }else if (!strPW.equals(strPWck)){
                    Toast.makeText(JoinMemberActivity.this, "비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show();
                }else if (result.equals("문자가 포함됨")){
                    Toast.makeText(JoinMemberActivity.this, "핸드폰은 숫자만 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Firebase Auth 진행
                    mFirebaseAuth.createUserWithEmailAndPassword(strID, strPW).addOnCompleteListener(JoinMemberActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                account.setIdToken(firebaseUser.getUid());
                                account.setUserID(firebaseUser.getEmail());
                                account.setUserPW(strPW);

                                // setValue = database에 insert(삽입) 행위
                                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                Toast.makeText(JoinMemberActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(JoinMemberActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // 중복 확인 버튼
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strID = edt[0].getText().toString();
                mFirebaseAuth.createUserWithEmailAndPassword(strID,"asd123").addOnCompleteListener(JoinMemberActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(JoinMemberActivity.this, "사용할 수 있는 이메일ID 입니다.", Toast.LENGTH_SHORT).show();
                            mFirebaseAuth.getCurrentUser().delete();
                        }
                        else {
                            Toast.makeText(JoinMemberActivity.this, "중복되는 이메일ID 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        // 뒤로가기 버튼
        tb_btn = (ImageView) findViewById(R.id.joinmember_tb_btn);
        tb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    // 스피너 관련 모음
    public void setSpinner(){
        String[] items1 = {"연도","2023","2022","2021","2020",
                            "2019","2018","2017","2016","2015","2014","2013","2012","2011","2010",
                            "2009","2008","2007","2006","2005","2004","2003","2002","2001","2000",
                            "1999","1998","1997","1996","1995","1994","1993","1992","1991","1990",
                            "1989","1988","1987","1986","1985","1984","1983","1982","1981","1980",
                            "1979","1978","1977","1976","1975","1974","1973","1972","1971","1970",
                            "1969","1968","1967","1966","1965","1964","1963","1962","1961","1960",
                            "1959","1958","1957","1956","1955","1954","1953","1952","1951","1950",};   //연도
        String[] items2 = {"월","1","2","3","4","5","6","7","8","9","10","11","12"};   //월
        String[] items3 = {"일","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};   //일
        String[] items4 = {"성별","남자", "여자"};      // 성별
        // 연도
        spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tv1.setText(items1[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // 월
        spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tv2.setText(items2[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // 일
        spinner3 = findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tv3.setText(items3[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // 성별 스피너
        spinner4 = findViewById(R.id.spinner4);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items4);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tv4.setText(items4[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
