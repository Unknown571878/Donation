package com.example.donation.option;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.donation.R;

import java.util.ArrayList;

public class OptionListViewAdapter extends BaseAdapter {
    private ImageView iconImageView;
    private TextView titleTextView;
    private ArrayList<OptionListViewItem> listViewItemList = new ArrayList<OptionListViewItem>();
    public OptionListViewAdapter(){    }

    @Override
    public int getCount(){
        return listViewItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.option_listview_item,parent,false);
        }

        titleTextView = (TextView) convertView.findViewById(R.id.option_listview_item_title);
        iconImageView = (ImageView) convertView.findViewById(R.id.option_listview_item_icon);

        OptionListViewItem listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.getTitle());
        iconImageView.setImageResource(listViewItem.getIcon());

        return convertView;
    }
    public void addItem(String title, int icon){
        OptionListViewItem item = new OptionListViewItem();

        item.setTitle(title);
        item.setIcon(icon);

        listViewItemList.add(item);
    }
}
