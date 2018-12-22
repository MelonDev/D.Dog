package com.up.bc.myapplicationproject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.utils.DateUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riontech.calendar.CustomCalendar;
import com.riontech.calendar.dao.EventData;
import com.riontech.calendar.dao.dataAboutDate;
import com.riontech.calendar.utils.CalendarUtils;
import com.up.bc.myapplicationproject.Data.PetData;
import com.up.bc.myapplicationproject.Data.PetEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CalendarActivity extends AppCompatActivity {

    Map<String, ArrayList<PetEvent>> map;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CardView card_a_layout = (CardView) findViewById(R.id.cal_a_layout);
        card_a_layout.setVisibility(View.GONE);
        CardView card_b_layout = (CardView) findViewById(R.id.cal_b_layout);
        card_b_layout.setVisibility(View.GONE);
        CardView card_c_layout = (CardView) findViewById(R.id.cal_c_layout);
        card_c_layout.setVisibility(View.GONE);
        CardView card_d_layout = (CardView) findViewById(R.id.cal_d_layout);
        card_d_layout.setVisibility(View.GONE);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key = extras.getString("KEY");

            FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").child(key).child("Info").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        PetData petData = dataSnapshot.getValue(PetData.class);


                        //ArrayList<PetEvent> arr = new CreateEvent().load(petData.getBreed());

                        FirebaseDatabase.getInstance().getReference().child("Data").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue() != null){

                                    Integer count2 = 0;

                                    ArrayList<PetEvent> arr = new ArrayList<>();
                                    arr.clear();

                                    DataSnapshot eventData = dataSnapshot.child("Event");

                                    for (DataSnapshot subDataSnapshot : eventData.getChildren()) {

                                        count2 += 1;

                                        DataSnapshot infoShot = subDataSnapshot.child("Info");
                                        DataSnapshot dateShot = subDataSnapshot.child("Date");
                                        DataSnapshot loopShot = subDataSnapshot.child("Loop");


                                        PetEvent petEvent = new PetEvent();
                                        petEvent.createEvent(getDataToString(infoShot,"name"),getDataToString(infoShot,"title"),getDataToBoolean(infoShot,"loop"))
                                                .createDate(getDataToInteger(dateShot,"day"),getDataToInteger(dateShot,"month"),getDataToInteger(dateShot,"year"))
                                                .createLoop(getDataToInteger(loopShot,"day"),getDataToInteger(loopShot,"month"),getDataToInteger(loopShot,"year"));


                                        arr.add(petEvent);

                                        if(count2 == eventData.getChildrenCount()){


                                            CalendarAlgorithmLibrary calendarAlgorithmLibrary = new CalendarAlgorithmLibrary();
                                            map = calendarAlgorithmLibrary.load(arr, petData.getYear(), petData.getMonth() -1, petData.getDay());
                                            ArrayList<String> arrDate = calendarAlgorithmLibrary.getStringFromMap(map);



                                            List<EventDay> events = new ArrayList<>();


                                            for (int i = 0; i < arrDate.size(); i++) {

                                                String strDate = arrDate.get(i);
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                try {
                                                    Date date = sdf.parse(strDate);
                                                    Calendar cal = Calendar.getInstance();
                                                    cal.setTime(date);

                                                    events.add(new EventDay(cal, R.drawable.ic_notification));
                                                } catch (ParseException e) {
                                                    Log.e("", "");
                                                }


                                            }


                                            showCal(Calendar.getInstance());


                                            com.applandeo.materialcalendarview.CalendarView calendarViews = (com.applandeo.materialcalendarview.CalendarView) findViewById(R.id.calendarView);
                                            calendarViews.setEvents(events);
                                            calendarViews.setOnDayClickListener(eventDay -> {
                                                Calendar clickedDayCalendar = eventDay.getCalendar();

                                                showCal(clickedDayCalendar);


                                            });


                                            LinearLayout backBtn = (LinearLayout) findViewById(R.id.calendar_back_btn);
                                            backBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    finish();
                                                }
                                            });



                                        }


                                    }



                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("", "");
                            }
                        });




                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("", "");
                }
            });
        }

    }

    private void showCal(Calendar clickedDayCalendar) {

        CardView card_a_layout = (CardView) findViewById(R.id.cal_a_layout);
        CardView card_b_layout = (CardView) findViewById(R.id.cal_b_layout);
        CardView card_c_layout = (CardView) findViewById(R.id.cal_c_layout);
        CardView card_d_layout = (CardView) findViewById(R.id.cal_d_layout);


        Date c = clickedDayCalendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        ArrayList<PetEvent> event = map.get(formattedDate);

        card_a_layout.setVisibility(View.GONE);
        card_b_layout.setVisibility(View.GONE);
        card_c_layout.setVisibility(View.GONE);
        card_d_layout.setVisibility(View.GONE);


        TextView time = (TextView) findViewById(R.id.cal_time);

        time.setText(clickedDayCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (clickedDayCalendar.get(Calendar.MONTH) + 1) + "/" + clickedDayCalendar.get(Calendar.YEAR));


        if (event != null) {

            Boolean bool_a = false;
            Boolean bool_b = false;
            Boolean bool_c = false;
            Boolean bool_d = false;

            String str_a = "";
            String str_b = "";
            String str_c = "";
            String str_d = "";


            for (int i = 0; i < event.size(); i++) {
                PetEvent s = event.get(i);
                String title = s.getTitle();

                switch (title) {
                    case "กำจัดพยาธิ":
                        bool_a = true;
                        card_a_layout.setVisibility(View.VISIBLE);
                        str_a = str_a + "- " + s.getName() + "\n";
                        break;
                    case "กำจัดเห็บ/หมัด":
                        bool_b = true;
                        card_b_layout.setVisibility(View.VISIBLE);
                        str_b = str_b + "- " + s.getName() + "\n";
                        break;
                    case "ฉีดวัคซีน":
                        bool_c = true;
                        card_c_layout.setVisibility(View.VISIBLE);
                        str_c = str_c + "- " + s.getName() + "\n";
                        break;
                    case "อาบน้ำ":
                        bool_d = true;
                        card_d_layout.setVisibility(View.VISIBLE);
                        str_d = str_d + "- " + s.getName() + "\n";
                        break;
                }
            }

            TextView a_des = (TextView) findViewById(R.id.cal_a_des);
            TextView b_des = (TextView) findViewById(R.id.cal_b_des);
            TextView c_des = (TextView) findViewById(R.id.cal_c_des);
            TextView d_des = (TextView) findViewById(R.id.cal_d_des);

            a_des.setText(str_a);
            b_des.setText(str_b);
            c_des.setText(str_c);
            d_des.setText(str_d);


        }
    }

    private String getDataToString(DataSnapshot dataSnapshot,String child){
        return dataSnapshot.child(child).getValue(String.class);
    }

    private Integer getDataToInteger(DataSnapshot dataSnapshot,String child){
        return dataSnapshot.child(child).getValue(Integer.class);
    }

    private Boolean getDataToBoolean(DataSnapshot dataSnapshot,String child){
        return dataSnapshot.child(child).getValue(Boolean.class);
    }



}
