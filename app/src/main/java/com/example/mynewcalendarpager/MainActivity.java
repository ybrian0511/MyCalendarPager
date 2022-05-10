package com.example.mynewcalendarpager;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static Calendar month_date; // 현재 날짜
    public static TextView ToolBar; // 툴바
    public static ArrayList<String> Week_Time; // 1주일의 시간
    public static ArrayList<String> Day_Time; // 하루동안의 시간
    public static ArrayList<ArrayList> month_cal; // 월간 달력 배열
    public static ArrayList<ArrayList> week_cal; // 주간 달력 배열
    Toolbar myToolbar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        Main(); // Main() 호출
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 액션 아이템 선택시 이벤트 처리
        switch (item.getItemId()) {
            case R.id.action_monthview:
                MonthView();
                return true;
            case R.id.action_weekview:
                WeekView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 앱바에 오버플로우 메뉴 추가
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Main(){ // 메인 화면 (시작 시 첫 화면)
        ToolBar = findViewById(R.id.toolbar_text);
        month_date = Calendar.getInstance(); // 현재 날짜
        Month(); // Month() 호출
        MonthView(); // MonthView() 호출
    }

    static String month_year_format(Calendar date){ // 현재 날짜를 String으로 불러온다.
        SimpleDateFormat dataFormat =  new SimpleDateFormat("yyyy년 MM월"); // 날짜 포맷 지정
        return dataFormat.format(date.getTime()); // 현재 날짜
    }

    private void MonthView() { // 월간 달력 뷰
        ToolBar.setText(month_year_format(month_date)); // 툴바에 현재 년월 출력
        getSupportFragmentManager().beginTransaction().replace(R.id.dayGridView, new MonthFragment()).commit();
    }
    private void WeekView() { // 주간 달력 뷰
        ToolBar.setText(month_year_format(month_date)); // 앱바에 현재 년월 출력
        getSupportFragmentManager().beginTransaction().replace(R.id.dayGridView, new WeekFragment()).commit();
        WeekTimeView(); // WeekTimeView() 호출
        DayTimeView(); // DayTimeView() 호출
        Week(); // Week() 호출
    }
    private void DayTimeView() { // 0~24
        Day_Time = Day_Time();
    }
    private void WeekTimeView(){ // 24 x 7
        Week_Time = Week_Time();
    }


    public ArrayList<String> MonthDate(Calendar date, int month){ // 월간 달력 배열
        ArrayList <String> days_in_month = new ArrayList();
        Calendar cal = (Calendar) date.clone(); // 복사본
        cal.add(Calendar.MONTH,month);
        cal.set(Calendar.DATE,1); // 날짜 지정(1일)
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK)-1; // 현재 월의 1일의 요일 (일:1~토:7) -1을 한 이유는 밑에서 설명
        int num_of_day = cal.getActualMaximum(Calendar.DATE); // 현재 월의 날짜의 최대 수 (1월:31일 2월:28일..)

        for (int i = 1; i <= 42; i++) { // 6행 7열 >> 42개
            if(i<= day_of_week || i> (num_of_day + day_of_week)) // 첫째날이나 마지막날이 아닌 경우
                days_in_month.add(""); // 공백 추가 (빈칸)
            else // 날짜 내에 있는 경우
                days_in_month.add(String.valueOf(i-day_of_week));
            // 예를들어 4월의 경우 6번째가 1일,금요일(6) -1을 하지 않으면 첫째날이 1이 아닌 0이 나타나게된다.
        }
        return days_in_month;
    }
    private void Month(){ // 월간 달력 날짜 저장
        month_cal = new ArrayList<>();
        for(int i=100; i>0; i--){
            month_cal.add(MonthDate(month_date,-i)); // 100달 전
        }
        for(int i=0; i<100; i++){
            month_cal.add(MonthDate(month_date,i)); // 100달 후
        }
    }

    public ArrayList<String> WeekDate(Calendar date, int month){ // 주간 달력 배열
        ArrayList<String> days_in_week = new ArrayList();
        Calendar cal = (Calendar) date.clone(); // 복사본
        Calendar Pre_cal = (Calendar) cal.clone(); // 이전 달 복사본
        Calendar Next_cal = (Calendar) cal.clone(); // 다음 달 복사본
        cal.add(Calendar.DATE,month); // 현재 날짜 월
        int first_sunday = cal.get(Calendar.DATE) - (cal.get(Calendar.DAY_OF_WEEK) - 1); // 현재 주의 일요일 날짜
        int day_of_month = cal.get(Calendar.MONTH); // 해당 날짜 월
        Pre_cal.set(Calendar.MONTH,day_of_month-1); // 현재 달 - 1
        Next_cal.set(Calendar.MONTH,day_of_month+1); // 현재 달 + 1
        for (int i = 0; i < 7; i++) {
            days_in_week.add(String.valueOf(first_sunday++)); // 예를들어 first_sunday가 1이면 1~7 2이면 2~8...
        }
        return days_in_week;
    }
    private void Week(){ // 주간 달력 날짜 저장
        week_cal = new ArrayList<>();
        for(int i=100; i>0; i--){
            week_cal.add(WeekDate(month_date,-7*i)); // 100주 전 (700일 전)
        }
        for(int i=0; i<100; i++){
            week_cal.add(WeekDate(month_date,7*i)); // 100주 후 (700일 후)
        }
    }
    public  ArrayList<String> Day_Time() { // 하루 시간 배열
        ArrayList<String> hours = new ArrayList();
        for(int i=0; i<24; i++){ // 0시 ~ 23시
            hours.add(String.valueOf(i)); // 0~23
        }
        return hours;
    }
    public  ArrayList<String> Week_Time(){ // 주별 시간 배열
        ArrayList<String> times = new ArrayList();
        for(int i=0; i<168; i++){ // 24행 7열 >> 168개
            times.add(String.valueOf(i)); // 0~167
        }
        return times;
    }
}