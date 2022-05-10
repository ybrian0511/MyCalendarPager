package com.example.mynewcalendarpager;

import static com.example.mynewcalendarpager.MainActivity.month_cal;
import static com.example.mynewcalendarpager.MainActivity.month_date;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Calendar;

public class MonthViewFragment extends Fragment {
    private static int num_of_day;
    public MonthViewFragment(int position) {
        num_of_day = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<String> month_days = month_cal.get(num_of_day); // 데이터 원본 준비
        View rootView = inflater.inflate(R.layout.fragment_month_view, container, false) ;
        month_GridAdapter adapter = new month_GridAdapter( getActivity().getApplicationContext(),R.layout.month_day, month_days); // 어댑터 생성
        GridView month_gridview = rootView.findViewById(R.id.month_gridview); // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        month_gridview.setAdapter(adapter); // 어댑터를 GridView 객체에 연결

        month_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked, int position, long id) {
                String day = adapter.getItem(position);
                if(day != "") { // 공백이 아닌 경우 (날짜가 있는 경우)
                    Toast.makeText(getActivity(), month_date.get(Calendar.YEAR)+"."+(month_date.get(Calendar.MONTH)+1)+"."+ day+"일", Toast.LENGTH_SHORT).show(); // 날짜 클릭 시 년.월.일 토스트 메시지 출력
                }
            }
        });
        return rootView;
    }
}