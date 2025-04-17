package com.example.donation.listofwriting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.donation.R;
import com.example.donation.login.UserAccout;
import com.example.donation.writing.WritingViewActivity;
import com.example.donation.writing.Writing_change;
import com.example.donation.writing.writing_pg;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

interface OnItemClickListener {
    void onItemClick(View view, int position);
}


public class ListOfWritingAdapter extends RecyclerView.Adapter<ListOfWritingAdapter.CustomViewHolder> {
    private ArrayList<ListOfWritingItem> arrayList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mFirebaseAuth;         // 파이어 베이스 인증
    private FirebaseUser User;              // 파이어 베이스 유저
    private DatabaseReference mdatabaseref;
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    public ListOfWritingAdapter(ArrayList<ListOfWritingItem> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_writing_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfWritingAdapter.CustomViewHolder holder, int position) {
        if (arrayList.get(position).getReceive_Money() == -1){
            holder.finish.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        } else {
            int r_money = arrayList.get(position).getReceive_Money();
            int _money = Integer.parseInt(arrayList.get(position).getMoney());
            double percent = Double.valueOf(r_money) / Double.valueOf(_money) * 100 ;
            int num = (int) percent;
            holder.progressBar.setProgress(num);
            holder.tv_percent.setText(String.valueOf(num));
        }
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_money.setText(String.valueOf(arrayList.get(position).getMoney()));
        holder.tv_dateandtime.setText(arrayList.get(position).getDateAndTime());
        holder.tv_userName.setText(arrayList.get(position).getUserNickName());
        holder.tv_img.setText(arrayList.get(position).getImg());
        holder.tv_listid.setText(arrayList.get(position).getKey());
        holder.tv_userimg.setText(arrayList.get(position).getUserImg());
        holder.tv_location.setText(arrayList.get(position).getRegion());

        mFirebaseAuth = FirebaseAuth.getInstance();
        User = mFirebaseAuth.getCurrentUser();
        if (User != null){
            String userID = arrayList.get(position).getId();
            String UID = User.getUid();
            if(userID.equals(UID)) {
                holder.postoption.setTag(position);
                holder.postoption.setVisibility(View.VISIBLE);
                //버튼 클릭시
                holder.postoption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), view);
                        popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.action_menu1:
                                        Intent intent = new Intent(view.getContext(), Writing_change.class);
                                        intent.putExtra("title", arrayList.get(holder.getAdapterPosition()).getTitle());
                                        intent.putExtra("money", String.valueOf(arrayList.get(holder.getAdapterPosition()).getMoney()));
                                        intent.putExtra("dateandtime", arrayList.get(holder.getAdapterPosition()).getDateAndTime());
                                        intent.putExtra("detail", arrayList.get(holder.getAdapterPosition()).getDetail());
                                        intent.putExtra("img", arrayList.get(holder.getAdapterPosition()).getImg());
                                        intent.putExtra("userimg", arrayList.get(holder.getAdapterPosition()).getUserImg());
                                        intent.putExtra("postkey", arrayList.get(holder.getAdapterPosition()).getKey());
                                        intent.putExtra("userID", arrayList.get(holder.getAdapterPosition()).getId());
                                        intent.putExtra("username",arrayList.get(holder.getAdapterPosition()).getUserNickName());
                                        intent.putExtra("DonationMoney",String.valueOf(arrayList.get(holder.getAdapterPosition()).getReceive_Money()));
                                        view.getContext().startActivity(intent);
                                        break;
                                    case R.id.action_menu2:
                                        AlertDialog.Builder postdelete = new AlertDialog.Builder(view.getContext());
                                        postdelete.setTitle("게시글 삭제");
                                        postdelete.setMessage("정말로 게시글을 삭제하시겠습니까?");
                                        postdelete.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                mDatabase = FirebaseDatabase.getInstance();
                                                mDatabase.getReference("donation").child("post").child(arrayList.get(holder.getAdapterPosition()).getKey())
                                                                .removeValue();
                                                Toast.makeText(view.getContext(), "게시글을 삭제하였습니다.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        postdelete.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(view.getContext(), "취소하였습니다.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        postdelete.show();
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
            } else {
                holder.postoption.setTag(position);
                holder.postoption.setVisibility(View.GONE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자 인증
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();
                    mDatabase = FirebaseDatabase.getInstance();
                    mDatabase.getReference("donation").child("UserAccount").child(uid)
                            .child("Watch List").child(arrayList.get(holder.getAdapterPosition()).getKey())
                            .setValue(arrayList.get(holder.getAdapterPosition()).getKey());
                } else {
                    // 사용자가 인증되지 않은 경우 처리하십시오.
                }
                Intent intent = new Intent(view.getContext(), WritingViewActivity.class);
                intent.putExtra("title", arrayList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("money", String.valueOf(arrayList.get(holder.getAdapterPosition()).getMoney()));
                intent.putExtra("dateandtime", arrayList.get(holder.getAdapterPosition()).getDateAndTime());
                intent.putExtra("detail", arrayList.get(holder.getAdapterPosition()).getDetail());
                intent.putExtra("username",arrayList.get(holder.getAdapterPosition()).getUserNickName());
                intent.putExtra("img", arrayList.get(holder.getAdapterPosition()).getImg());
                intent.putExtra("listid", arrayList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("postkey", arrayList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("userID", arrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("userimg",arrayList.get(holder.getAdapterPosition()).getUserImg());
                intent.putExtra("DonationMoney",String.valueOf(arrayList.get(holder.getAdapterPosition()).getReceive_Money()));
                intent.putExtra("region", arrayList.get(holder.getAdapterPosition()).getRegion());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;  // 제목
        TextView tv_money;  // 기부금액
        TextView tv_dateandtime;    // 시간
        TextView tv_userName;   // 유저 이름
        TextView tv_img;        // 이미지
        TextView tv_userimg;    // 유저 이미지
        TextView tv_listid;     // 리스트 키
        TextView tv_percent;    //기부퍼센트
        ProgressBar progressBar; // 바
        ImageView postoption;
        TextView tv_location;
        TextView finish;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_location = itemView.findViewById(R.id.iv_location);
            this.tv_title = itemView.findViewById(R.id.iv_title);
            this.tv_money = itemView.findViewById(R.id.iv_money);
            this.tv_dateandtime = itemView.findViewById(R.id.iv_dateandtime);
            this.tv_userName = itemView.findViewById(R.id.iv_userName);
            this.tv_img = itemView.findViewById(R.id.iv_img);
            this.tv_listid = itemView.findViewById(R.id.iv_listid);
            this.tv_percent = itemView.findViewById(R.id.iv_percent);
            this.progressBar = itemView.findViewById(R.id.donation_gauge);
            this.postoption = itemView.findViewById(R.id.writing_option);
            this.tv_userimg = itemView.findViewById(R.id.iv_userimg);
            this.finish = itemView.findViewById(R.id.donation_finish);
        }
    }
}
