package com.example.mynewcalendarpager;

import static com.example.mynewcalendarpager.MainActivity.Day_Time;
import static com.example.mynewcalendarpager.MainActivity.Week_Time;
import static com.example.mynewcalendarpager.MainActivity.week_cal;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class WeekViewFragment extends Fragment {
    private static int num_of_day;
    public WeekViewFragment(int position) {
        num_of_day = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<String> week_days = week_cal.get(num_of_day); // 데이터 원본 준비
        View rootView = inflater.inflate(R.layout.fragment_week_view, container, false) ;

        DayTimeAdapter dt_adapter = new DayTimeAdapter(getActivity().getApplicationContext(),R.layout.time, Day_Time); // 어댑터 생성
        GridView times = rootView.findViewById(R.id.times); // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        times.setAdapter(dt_adapter); // 어댑터를 GridView 객체에 연결

        week_GridAdapter adapter = new week_GridAdapter( getActivity().getApplicationContext(),R.layout.week_day, week_days); // 어댑터 생성
        GridView week_gridview= rootView.findViewById(R.id.week_gridview); // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        week_gridview.setAdapter(adapter); // 어댑터를 GridView 객체에 연결
        
        WeekTimeAdapter wt_adapter = new WeekTimeAdapter(getActivity().getApplicationContext(),R.layout.month_day, Week_Time);
        GridView week_time_gridview= rootView.findViewById(R.id.week_time_gridview); // 어댑터 생성
        week_time_gridview.setAdapter(wt_adapter); // 어댑터를 GridView 객체에 연결
        week_time_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked, int position, long id) {
                String day = wt_adapter.getItem(position);
                if(day != "") { // 공백이 아닌 경우
                    Toast.makeText(getActivity(),"position = "+ day, Toast.LENGTH_SHORT).show(); // 날짜 클릭 시 position = day 토스트 메시지 출력
                }
            }
        });
        return rootView;
    }
}