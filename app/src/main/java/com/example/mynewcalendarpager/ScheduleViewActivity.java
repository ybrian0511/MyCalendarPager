package com.example.mynewcalendarpager;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class ScheduleViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 100;
    final private int REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES = 101;
    final static String TAG="SQLiteDBTest";
    public static final String SETTING_TITLE = "title";
    private DBHelper mDBHelper;
    private List<Address> Address;
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private double longitude, latitude;
    private FusedLocationProviderClient mFusedLocationClient;
    private NumberPicker StartHour, StartMinute, StartAMPM, EndHour, EndMinute, EndAMPM;
    private Button exit_btn, save_btn, delete_btn, search_btn;
    private TextView editTime;
    private Calendar date, current;
    private EditText place, memo;
    private String StartTime,EndTime,Location,Memo, Title,Date,preTitle, search, monthOfdate;
    private int position, index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);
        mDBHelper = new DBHelper(this);
        editTime = findViewById(R.id.editTextTime);
        memo = findViewById(R.id.memo);
        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        place = findViewById(R.id.place);
        Intent intent = getIntent();

        if(intent.hasExtra("ScheduleTitle"))
            Title = intent.getStringExtra("ScheduleTitle");
        if(intent.hasExtra("startTime"))
            StartTime = intent.getStringExtra("startTime");
        if(intent.hasExtra("EndTime"))
            EndTime = intent.getStringExtra("EndTime");
        if(intent.hasExtra("Location"))
            Location = intent.getStringExtra("Location");
        if(intent.hasExtra("Memo"))
            Memo = intent.getStringExtra("Memo");
        if(intent.hasExtra("Date"))
            Date = intent.getStringExtra("Date");

        index = intent.getIntExtra("Index",-1);
        date =  (Calendar)intent.getSerializableExtra("date");
        monthOfdate = intent.getStringExtra("monthOfdate");
        position = intent.getIntExtra("Time",-1);
        current = Calendar.getInstance();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if(Memo!= null) ScheduleView();
        save_btn = findViewById(R.id.save);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                if(Memo!=null) UpdateSchedule();
                else insertRecord();
                finish();
            }
        });
        exit_btn = findViewById(R.id.cancle);
        exit_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
        delete_btn = findViewById(R.id.delete);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteRecord();
                Toast.makeText(getApplicationContext(), "일정 삭제", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ScheduleViewActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES
            );
            return;
        }
        NumberPickers();
        if(position ==-1){
            if(StartTime == null) {
                editTime.setText(date.get(Calendar.YEAR)+"년 "+(date.get(Calendar.MONTH)+1)+"월 "+monthOfdate+"일 "+(current.get(Calendar.HOUR_OF_DAY)+1)+"시");
                preTitle = editTime.getText().toString();
            }
            else editTime.setText(Title);
        }
    }

    static class Schedule{
        private String date;
        private String ScheduleTitle;
        public Schedule(String date, String Title){
            this.date = date;
            this.ScheduleTitle = Title;
        }
    }
    void ScheduleView(){
        if(Title !=null)
            editTime.setText(Title);
        String[] startTime = StartTime.split(":");
        StartHour.setValue(Integer.parseInt(startTime[0]));
        StartMinute.setValue(Integer.parseInt(startTime[1]));
        StartAMPM.setValue(startTime[2].equals("AM")? 0:1);
        String[] endTime = EndTime.split(":");
        EndHour.setValue(Integer.parseInt(endTime[0]));
        EndMinute.setValue(Integer.parseInt(endTime[1]));
        EndAMPM.setValue(endTime[2].equals("AM")? 0:1);
        memo.setText(Memo);
    }
    private String StartTime(){
        int startHour = StartHour.getValue();
        int startMinute = StartMinute.getValue();
        int startAMPM= StartAMPM.getValue();
        String AMPM;
        if(startAMPM == 0) AMPM="AM";
        else AMPM="PM";
        return startHour+":"+startMinute+":"+AMPM;
    }
    private String EndTime(){
        int endHour = EndHour.getValue();
        int endMinute = EndMinute.getValue();
        int endAMPM= EndAMPM.getValue();
        String AMPM;
        if(endAMPM == 0) AMPM="AM";
        else AMPM="PM";
        return endHour+":"+endMinute+":"+AMPM;
    }
    void setEditTextTime(int Time){
        int time = Time;
        if(StartAMPM.getValue() == 1) time+=12;
        editTime.setText(date.get(Calendar.YEAR)+"년 "+(date.get(Calendar.MONTH)+1)+"월 "+monthOfdate+"일 "+time+"시");
        preTitle =date.get(Calendar.YEAR)+"년 "+(date.get(Calendar.MONTH)+1)+"월 "+monthOfdate+"일 "+time+"시";
    }
    void setEditTextTimeAMPM(){
        int time = StartHour.getValue();
        if(StartAMPM.getValue() == 1) time+=12;
        editTime.setText(date.get(Calendar.YEAR)+"년 "+(date.get(Calendar.MONTH)+1)+"월 "+monthOfdate+"일 "+time+"시");
        preTitle =date.get(Calendar.YEAR)+"년 "+(date.get(Calendar.MONTH)+1)+"월 "+monthOfdate+"일 "+time+"시";
    }
    void NumberPickers(){
        int AMPM;
        StartHour = findViewById(R.id.StartHour);
        StartMinute = findViewById(R.id.StartMinute);
        StartAMPM = findViewById(R.id.StartAMPM);
        StartHour.setMinValue(1);
        StartHour.setMaxValue(12);
        StartMinute.setMinValue(0);
        StartMinute.setMaxValue(59);
        EndHour = findViewById(R.id.EndHour);
        EndMinute = findViewById(R.id.EndMinute);
        EndAMPM = findViewById(R.id.EndAMPM);
        EndHour.setMinValue(1);
        EndHour.setMaxValue(12);
        EndMinute.setMinValue(0);
        EndMinute.setMaxValue(59);
        if(position == -1) {
            if(index != -1){}
            StartHour.setValue((current.get(Calendar.HOUR) + 1));
            EndHour.setValue((current.get(Calendar.HOUR) + 2));
            if(current.get(Calendar.AM_PM) == Calendar.AM) AMPM =0;
            else AMPM =1;
        }
        else {
            int value = (position/7)/13;
            StartHour.setValue((position/7)%13+value);
            EndHour.setValue(((position/7)%13)+1+value);
            if((position/7)<12) {
                if((position/7) ==11) {AMPM=2;}
                else AMPM =0;
            }
            else {
                if((position/7)== 23) AMPM =3;
                else AMPM =1;
            }
        }
        StartHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(editTime.getText().toString().equals(preTitle)) setEditTextTime(newVal);
            }
        });
        String[] strings = new String[]{"AM","PM"};
        StartAMPM.setMinValue(0);
        StartAMPM.setMaxValue(1);
        EndAMPM.setMinValue(0);
        EndAMPM.setMaxValue(1);
        StartAMPM.setDisplayedValues(strings);
        EndAMPM.setDisplayedValues(strings);
        if(AMPM >1){
            StartAMPM.setValue(AMPM%2);
            EndAMPM.setValue((AMPM%2+1)%2);
        }
        else{
            StartAMPM.setValue(AMPM);
            EndAMPM.setValue(AMPM);
        }
        StartAMPM.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(editTime.getText().toString().equals(preTitle))
                    setEditTextTimeAMPM();
            }
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Geocoder g = new Geocoder(this);
        Address = null;
        search_btn = findViewById(R.id.search);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    search = place.getText().toString();
                    Address = g.getFromLocationName(search,1);
                }
                catch (IOException e) {}
                finally{
                    if(Address != null) {
                        Address address = Address.get(0);
                        if (address.hasLatitude() && address.hasLongitude()) {
                            latitude = address.getLatitude();
                            longitude = address.getLongitude();
                            LatLng Road = new LatLng(latitude, longitude);
                            Marker Custom = googleMap.addMarker(new MarkerOptions()
                                    .position(Road).title(search));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Road, 16));
                        }
                    }
                }
            }
        });
        if(Memo != null){
            String[] location = Location.split(",");
            search = location[0];
            place.setText(search);
            latitude = Double.parseDouble(location[1]);
            longitude = Double.parseDouble(location[2]);
            LatLng Road = new LatLng(latitude, longitude);
            Marker Custom = googleMap.addMarker(new MarkerOptions()
                    .position(Road).title(search));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Road, 16));}
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void insertRecord() {
        EditText title = (EditText)findViewById(R.id.editTextTime);
        String dateToSting = date.get(Calendar.YEAR)+"-"+(date.get(Calendar.MONTH)+1)+"-"+monthOfdate;
        String start_time = StartTime();
        String end_time = EndTime();
        mDBHelper.insertEventBySQL(title.getText().toString(), dateToSting, start_time, end_time, search +","+ latitude +","+ longitude, memo.getText().toString());
    }
    void UpdateSchedule() {
        EditText title = (EditText)findViewById(R.id.editTextTime);
        String dateToSting = date.get(Calendar.YEAR)+"-"+(date.get(Calendar.MONTH)+1)+"-"+monthOfdate;
        String start_time = StartTime();
        String end_time = EndTime();
        mDBHelper.updateEventBySQL(String.valueOf(index),title.getText().toString(), dateToSting, start_time, end_time, search +","+ latitude +","+ longitude, memo.getText().toString());
    }
    private void deleteRecord() {
        if(index != -1) {
            mDBHelper.deleteEventBySQL(index);
        }
    }
}