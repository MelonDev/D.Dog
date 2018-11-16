package com.up.bc.myapplicationproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.up.bc.myapplicationproject.Adapter.MainAdapter;
import com.up.bc.myapplicationproject.Data.PetData;
import com.up.bc.myapplicationproject.Tools.Tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import th.ac.up.se.takingbear.Tools.FSTool;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter adapter;

    ArrayList<PetData> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new ArrayList<>();
        clearAlarm();


        /*
        Constraints myConstraints = new Constraints.Builder()
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .build();

        OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInitialDelay(2, TimeUnit.SECONDS)
                .setConstraints(myConstraints)
                .build();
        WorkManager.getInstance().enqueue(mywork);
*/

        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);

        FirebaseMessaging.getInstance().subscribeToTopic("APP");


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        adapter = new MainAdapter(this, data);
        recyclerView.setAdapter(adapter);
        Tools tool = new Tools();

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.main_progress);
        progressBar.setVisibility(View.VISIBLE);

        TextView nullView = (TextView)findViewById(R.id.main_null);
        nullView.setVisibility(View.GONE);

        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    data.clear();

                    Integer count = 0;

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        count += 1;

                        PetData petData = dataSnapshot1.child("Info").getValue(PetData.class);
                        data.add(petData);
                        adapter.notifyDataSetChanged();


                        if (tool.toLong(count) == dataSnapshot.getChildrenCount()) {
                            progressBar.setVisibility(View.GONE);

                        }
                    }


                }else {
                    progressBar.setVisibility(View.GONE);
                    nullView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("", "");
            }
        });

        ImageView signOut = (ImageView) findViewById(R.id.main_sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                clearAlarm();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        CardView addBtn = (CardView) findViewById(R.id.main_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("ID", 0);
                startActivity(intent);
            }
        });

        //Calendar c = Calendar.getInstance();
        //c.add(Calendar.SECOND,10);

        //setEvent(c);

    }

    private void clearAlarm() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent updateServiceIntent = new Intent(this, AlarmReceiver.class);
        updateServiceIntent.putExtra("CANCEL", false);
        PendingIntent pendingUpdateIntent = PendingIntent.getService(this, 0, updateServiceIntent, 0);

        // Cancel alarms
        try {
            alarmManager.cancel(pendingUpdateIntent);
        } catch (Exception e) {
            Log.e("Alarm", "AlarmManager update was not canceled. " + e.toString());
        }
    }

    private void setEvent(Calendar targetCal) {
        final int _id = (int) System.currentTimeMillis();
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("TITLE", "หัวข้อ");
        intent.putExtra("MESSAGE", "ทดสอบ");
        intent.putExtra("CANCEL", false);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), _id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }


}
