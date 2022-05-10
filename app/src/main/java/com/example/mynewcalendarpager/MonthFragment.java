package com.example.mynewcalendarpager;

import static com.example.mynewcalendarpager.MainActivity.ToolBar;
import static com.example.mynewcalendarpager.MainActivity.month_date;
import static com.example.mynewcalendarpager.MainActivity.month_year_format;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Calendar;

public class MonthFragment extends Fragment {
    private ViewGroup viewgroup;
    private int current =100;
    public MonthFragment() {
    }

    public static void ToolBar(){ // 앱바 현재 날짜
        ToolBar.setText(month_year_format(month_date));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewgroup = (ViewGroup) inflater.inflate(R.layout.fragment_month,container,false);
        month_pager();
        return viewgroup;
    }

    private void month_pager(){ // viewpager 설정
        ViewPager2 vpPager = viewgroup.findViewById(R.id.month_vpPager);
        MonthPagerAdapter adapter = new MonthPagerAdapter(getActivity());
        vpPager.setAdapter(adapter);
        vpPager.setCurrentItem(100,false); // 현재 페이지 설정
        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { // 페이지 변화->특정 일 처리
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(current > position) { // 현재 페이지가 새 페이지보다 값이 클 경우
                    month_date.add(Calendar.MONTH,-1); // 이전 달
                }
                else if ( current < position ) { // 현재 페이지가 새 페이지보다 값이 작은 경우
                    month_date.add(Calendar.MONTH,1); // 다음 달
                }
                else ;
                current =position; // 현재 페이지와 새 페이지의 값이 같으면
                ToolBar(); // 앱바
            }
        });
    }

    public class MonthPagerAdapter extends FragmentStateAdapter {
        private int NUM_ITEMS=500;
        public MonthPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) { // 프래그먼트 반환
            MonthViewFragment monthView = new MonthViewFragment(position);
            return monthView;
        }

        @Override
        public int getItemCount() { // 전체 페이지 개수 반환
            return NUM_ITEMS;
        }
    }
}