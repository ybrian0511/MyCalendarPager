package com.example.mynewcalendarpager;

import static com.example.mynewcalendarpager.MainActivity.mDbHelper;
import static com.example.mynewcalendarpager.MainActivity.firstSelecteDate;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;

public class month_GridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mdays;
    private int mResource;
    private String mDate ;
    private static ArrayList<Schedule> Schedules = new ArrayList<>();;

    public month_GridAdapter(Context context, int resource, ArrayList<String> CalendarList) {
        mContext = context;
        mdays = CalendarList;
        mResource = resource;
        int pos=0;
        firstSelecteDate.add(Calendar.MONTH,-(500-pos));
        mDate = firstSelecteDate.get(Calendar.YEAR)+"-"+(firstSelecteDate.get(Calendar.MONTH)+1);
    }
    static class Schedule{
        String mdate;
        String mtitle;
        Schedule(String date, String title){
            mdate =date;
            mtitle = title;
        }
    }
    void UpdateSchedules() {
        Cursor cursor = mDbHelper.searchSchedule(mDate);
        while (cursor.moveToNext()) {
            String[] splitStr = cursor.getString(2).split("-");
            Schedule schedule = new Schedule(splitStr[2], cursor.getString(1));
            Schedules.add(schedule);
        }
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
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // 해당 항목 뷰가 이전에 생성된 적이 없는 경우
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 항목 뷰를 정의한 xml 리소스(여기서는 mResource 값)으로부터 항목 뷰 객체를 메모리로 로드
            convertView = inflater.inflate(mResource,parent,false);
            UpdateSchedules();
            int count =0;
            ArrayList<Integer> dateSchedule = new ArrayList<>();

            for(int i =0; i<Schedules.size(); i++){
                if(getItem(position).equals(Schedules.get(i).mdate))  {
                    TextView include_Text;
                    count++;
                    switch (count){
                        case 1:
                            include_Text = convertView.findViewById(R.id.Schedule1);
                            include_Text.setText(Schedules.get(i).mtitle);
                            include_Text.setTextColor(R.color.white);
                            FrameLayout bg = convertView.findViewById(R.id.detail1);
                            bg.setVisibility(View.VISIBLE);
                            convertView.findViewById(R.id.include).setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            include_Text = convertView.findViewById(R.id.Schedule2);
                            include_Text.setText(Schedules.get(i).mtitle);
                            include_Text.setTextColor(R.color.white);
                            FrameLayout bg2 = convertView.findViewById(R.id.detail2);
                            bg2.setVisibility(View.VISIBLE);
                            convertView.findViewById(R.id.include).setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        }
        // 캘린더 예제 동영상에서 나온 코드인데 달력 날짜를 더 보기 좋게 만들게 하는 코드이다.
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() *0.166666666);
        TextView day = convertView.findViewById(R.id.MonthDay);
        day.setText(getItem(position));
        return convertView;
    }
}


