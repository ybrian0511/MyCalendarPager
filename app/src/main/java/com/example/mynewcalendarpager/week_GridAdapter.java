package com.example.mynewcalendarpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class week_GridAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<String> mdays;
    private int mResource;

    public week_GridAdapter(Context context, int resource, ArrayList<String> CalendarList) {
        mContext = context;
        mdays = CalendarList;
        mResource = resource;
    }

    @Override
    public int getCount() {
        return mdays.size();
    }
    @Override
    public String getItem(int position) {
        return mdays.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // 해당 항목 뷰가 이전에 생성된 적이 없는 경우
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 항목 뷰를 정의한 xml 리소스(여기서는 mResource 값)으로부터 항목 뷰 객체를 메모리로 로드
            convertView = inflater.inflate(mResource, parent, false);
        }
        TextView day = convertView.findViewById(R.id.WeekDay);
        day.setText(getItem(position));
        return convertView;
    }
}


