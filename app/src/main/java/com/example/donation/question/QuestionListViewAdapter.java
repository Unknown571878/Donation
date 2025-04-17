package com.example.donation.question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.donation.R;
import java.util.ArrayList;

public class QuestionListViewAdapter extends BaseAdapter {

    private TextView titleTextView;
    private TextView contentTextView;
    private ArrayList<QuestionListViewItem> listViewItemList = new ArrayList<QuestionListViewItem>();
    public QuestionListViewAdapter(){    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.question_listview_item,parent,false);
        }

        titleTextView = (TextView) convertView.findViewById(R.id.question_listview_item_title);
        contentTextView = (TextView) convertView.findViewById(R.id.question_listview_item_content);

        QuestionListViewItem listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.getTitle());
        contentTextView.setText(listViewItem.getContent());

        return convertView;
    }
    public void addItem(String title, String content){
        QuestionListViewItem item = new QuestionListViewItem();

        item.setTitle(title);
        item.setContent(content);

        listViewItemList.add(item);
    }
}
