package com.example.donation.Talent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donation.R;
import com.example.donation.listofwriting.ListOfWritingAdapter;
import com.example.donation.listofwriting.ListOfWritingItem;
import com.example.donation.writing.WritingViewActivity;
import com.example.donation.writing.Writing_change;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

interface OnItemClickListener {
    void onItemClick(View view, int position);
}

public class TalentListAdapter extends RecyclerView.Adapter<TalentListAdapter.CustomViewHolder>{
    private Context context;
    private ArrayList<TalentListViewItem> arrayList;
    private FirebaseAuth mFirebaseAuth;         // 파이어 베이스 인증
    private FirebaseUser User;              // 파이어 베이스 유저
    private DatabaseReference databaseReference;
    private FirebaseDatabase mDatabase;     // 파이어 베이스 데이터베이스
    private FirebaseFirestore firebaseFirestore;
    public TalentListAdapter(ArrayList<TalentListViewItem> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {this.listener = listener;}
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.talent_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TalentListAdapter.CustomViewHolder holder, int position) {
        holder.tv_t_title.setText(arrayList.get(position).getTitle());
        holder.tv_t_activity.setText(arrayList.get(position).getActivity());
        holder.tv_t_dateandtime.setText(arrayList.get(position).getDateAndTime());
        holder.tv_t_userName.setText(arrayList.get(position).getUserNickName());
        holder.tv_t_userimg.setText(arrayList.get(position).getUserImg());
        holder.tv_t_mainimg.setText(arrayList.get(position).getImg());
        holder.tv_t_location.setText(arrayList.get(position).getRegion());

        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference("donation").child("Activity Post").child(arrayList.get(position).getKey())
                .child("Apply User").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            holder.End_Message.setVisibility(View.VISIBLE);
                            holder.hide.setVisibility(View.GONE);
                        }
                    }
                });

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
                                        Intent intent = new Intent(view.getContext(), Talent_change.class);
                                        intent.putExtra("title", arrayList.get(holder.getAdapterPosition()).getTitle());
                                        intent.putExtra("dateandtime", arrayList.get(holder.getAdapterPosition()).getDateAndTime());
                                        intent.putExtra("detail", arrayList.get(holder.getAdapterPosition()).getDetail());
                                        intent.putExtra("img", arrayList.get(holder.getAdapterPosition()).getImg());
                                        intent.putExtra("userimg", arrayList.get(holder.getAdapterPosition()).getUserImg());
                                        intent.putExtra("postkey", arrayList.get(holder.getAdapterPosition()).getKey());
                                        intent.putExtra("userID", arrayList.get(holder.getAdapterPosition()).getId());
                                        intent.putExtra("username",arrayList.get(holder.getAdapterPosition()).getUserNickName());
                                        intent.putExtra("activity", arrayList.get(holder.getAdapterPosition()).getActivity());
                                        intent.putExtra("region", arrayList.get(holder.getAdapterPosition()).getRegion());
                                        view.getContext().startActivity(intent);
                                        break;
                                    case R.id.action_menu2:
                                        AlertDialog.Builder postdelete = new AlertDialog.Builder(view.getContext());
                                        postdelete.setTitle("게시글 삭제");
                                        postdelete.setMessage("정말로 게시글을 삭제하시겠습니까?");
                                        postdelete.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                mDatabase = FirebaseDatabase.getInstance();
                                                mDatabase.getReference("donation").child("Activity Post").child(arrayList.get(holder.getAdapterPosition()).getKey())
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
                Intent intent = new Intent(view.getContext(), talent_detail.class);
                intent.putExtra("userID", arrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("title", arrayList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("activity", arrayList.get(holder.getAdapterPosition()).getActivity());
                intent.putExtra("dateandtime", arrayList.get(holder.getAdapterPosition()).getDateAndTime());
                intent.putExtra("detail", arrayList.get(holder.getAdapterPosition()).getDetail());
                intent.putExtra("username",arrayList.get(holder.getAdapterPosition()).getUserNickName());
                intent.putExtra("userimg",arrayList.get(holder.getAdapterPosition()).getUserImg());
                intent.putExtra("mainimg",arrayList.get(holder.getAdapterPosition()).getImg());
                intent.putExtra("thispost", arrayList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("region", arrayList.get(holder.getAdapterPosition()).getRegion());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {return (arrayList != null ? arrayList.size() : 0);}

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_t_title;
        TextView tv_t_activity;
        TextView tv_t_dateandtime;
        TextView tv_t_userName;
        TextView tv_t_userimg;
        TextView tv_t_mainimg;
        TextView tv_t_location;
        ImageView postoption;
        TextView End_Message;
        LinearLayout hide;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_t_title = itemView.findViewById(R.id.iv_talent_title);
            this.tv_t_activity = itemView.findViewById(R.id.iv_talent_activity);
            this.tv_t_dateandtime = itemView.findViewById(R.id.iv_talent_dateandtime);
            this.tv_t_userName = itemView.findViewById(R.id.iv_talent_userName);
            this.tv_t_userimg = itemView.findViewById(R.id.talent_item_userimg);
            this.tv_t_mainimg = itemView.findViewById(R.id.talent_item_mainimg);
            this.tv_t_location = itemView.findViewById(R.id.iv_talent_location);
            this.postoption = itemView.findViewById(R.id.postoption);
            this.End_Message = itemView.findViewById(R.id.end_telent);
            this.hide = itemView.findViewById(R.id.telent_linear);
        }
    }
}
