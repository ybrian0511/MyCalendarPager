package com.example.mynewcalendarpager;

import static com.example.mynewcalendarpager.MainActivity.month_cal;
import static com.example.mynewcalendarpager.MainActivity.month_date;
import static com.example.mynewcalendarpager.MainActivity.mDbHelper;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;

public class MonthViewFragment extends Fragment {
    private static int num_of_day;
    private String day;
    private static View previous = null;
    private static ArrayList<month_GridAdapter.Schedule> Schedules = new ArrayList<>();;
    public MonthViewFragment(int position) {
        num_of_day = position;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onViewCreated(View view, Bundle savedInstanceState){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<String> month_days = month_cal.get(num_of_day); // 데이터 원본 준비
        View rootView = inflater.inflate(R.layout.fragment_month_view, container, false) ;
        month_GridAdapter adapter = new month_GridAdapter( getActivity().getApplicationContext(),R.layout.month_day, month_days); // 어댑터 생성
        GridView month_gridview = rootView.findViewById(R.id.month_gridview); // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        month_gridview.setAdapter(adapter); // 어댑터를 GridView 객체에 연결
        month_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked, int position, long id) {
                day = adapter.getItem(position);
                if(day != "") { // 공백이 아닌 경우 (날짜가 있는 경우)
                    UpdateSchedules(day);
                    String[] displayValues = new String[Schedules.size()];
                    if(Schedules.size() != 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(month_date.get(Calendar.YEAR)+"년 "+(month_date.get(Calendar.MONTH)+1)+"월 "+day+"일");
                        for(int i=0; i<Schedules.size(); i++)
                            displayValues[i] = (Schedules.get(i).mtitle);
                        builder.setItems(displayValues, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                Cursor cursor = loadSchedules(day,item);
                                Intent intent = new Intent(getActivity(), ScheduleViewActivity.class);
                                intent.putExtra("date", day);
                                intent.putExtra("monthOfdate", day);
                                intent.putExtra("Index",Integer.parseInt(cursor.getString(0)));
                                intent.putExtra("ScheduleTitle",cursor.getString(1));
                                intent.putExtra("Date",cursor.getString(2));
                                intent.putExtra("startTime",cursor.getString(3));
                                intent.putExtra("EndTime",cursor.getString(4));
                                intent.putExtra("Location",cursor.getString(5));
                                intent.putExtra("Memo",cursor.getString(6));
                                startActivityForResult(intent, 1);
                            }
                        });
                        builder.show();
                    }
                    adapter.notifyDataSetChanged();
                    if(previous == null) {
                        month_gridview.getChildAt(position).setBackgroundColor(Color.CYAN);
                        month_gridview.getChildAt(position).setBackgroundResource(R.drawable.border_layout);
                        previous = month_gridview.getChildAt(position);
                    }
                    else {
                        previous.setBackgroundColor(Color.WHITE);
                        month_gridview.getChildAt(position).setBackgroundColor(Color.CYAN);
                        month_gridview.getChildAt(position).setBackgroundResource(R.drawable.border_layout);
                        previous = month_gridview.getChildAt(position);
                    }
                }
                Schedules.clear();
            }
        });
        FloatingActionButton addBtn =getActivity().findViewById(R.id.detail_schedules);
        addBtn.setBackgroundColor(1);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(day != null) {
                    Intent intent = new Intent(getActivity(), ScheduleViewActivity.class);
                    intent.putExtra("date", month_date);
                    intent.putExtra("monthOfdate", day);
                    startActivityForResult(intent, 1);
                }
            }
        });
        return rootView;
    }
    void UpdateSchedules(String date) {
        Cursor cursor = mDbHelper.getAllSchedule(month_date.get(Calendar.YEAR)+"-"+(month_date.get(Calendar.MONTH)+1)+"-"+date);
        while (cursor.moveToNext()) {
            String[] splitStr = cursor.getString(2).split("-");
            month_GridAdapter.Schedule schedule = new month_GridAdapter.Schedule(splitStr[2], cursor.getString(1));
            Schedules.add(schedule);
        }
    }
    Cursor loadSchedules(String date, int position){
        Cursor cursor = mDbHelper.getAllSchedule(month_date.get(Calendar.YEAR)+"-"+(month_date.get(Calendar.MONTH)+1)+"-"+date);
        for(int i=0; i<=position; i++)
            cursor.moveToNext();
        return cursor;
    }
}