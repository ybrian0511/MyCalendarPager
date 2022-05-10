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

public class WeekFragment extends Fragment {
    private ViewGroup viewGroup;
    private int current =100;
    public WeekFragment() {
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
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_week,container,false);
        week_pager();
        return viewGroup;
    }

    private void week_pager(){ // viewpager 설정
        ViewPager2 vpPager = viewGroup.findViewById(R.id.week_vpPager);
        WeekPagerAdapter adapter = new WeekPagerAdapter(getActivity());
        vpPager.setAdapter(adapter);
        vpPager.setCurrentItem(100,false); // 현재 페이지 설정
        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { // 페이지 변화->특정 일 처리
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(current > position) { // 현재 페이지가 새 페이지보다 값이 클 경우
                    month_date.add(Calendar.DATE,-7); // 이전 주 (7일 전)
                }
                else if ( current < position ) { // 현재 페이지가 새 페이지보다 값이 작은 경우
                    month_date.add(Calendar.DATE,7); // 다음 주 (7일 후)
                }
                else ;
                current =position; // 현재 페이지와 새 페이지의 값이 같으면
                ToolBar(); // 앱바
            }
        });
    }

    public class WeekPagerAdapter extends FragmentStateAdapter {
        private int NUM_ITEMS=500;
        public WeekPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) { // 프래그먼트 반환
            WeekViewFragment weekView = new WeekViewFragment(position);
            return weekView;
        }

        @Override
        public int getItemCount() { // 전체 페이지 개수 반환
            return NUM_ITEMS;
        }
    }
}
