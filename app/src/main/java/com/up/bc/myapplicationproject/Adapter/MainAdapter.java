package com.up.bc.myapplicationproject.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.up.bc.myapplicationproject.AlarmReceiver;
import com.up.bc.myapplicationproject.CalendarAlgorithmLibrary;
import com.up.bc.myapplicationproject.Data.PetData;
import com.up.bc.myapplicationproject.Data.PetEvent;
import com.up.bc.myapplicationproject.FunctionTool;
import com.up.bc.myapplicationproject.PetActivity;
import com.up.bc.myapplicationproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    public ArrayList<PetData> data;
    public FragmentActivity act;
    public Context ct;

    public MainAdapter(FragmentActivity fragmentActivity, ArrayList<PetData> data) {

        this.data = data;
        this.act = fragmentActivity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_main_card, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        this.ct = parent.getContext();
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final PetData petData = data.get(position);


        holder.title.setText(petData.getName());
        holder.birth.setText("วันเกิด: " + petData.getDay() + "/" + petData.getMonth() + "/" + petData.getYear());

        if (petData.getGender().contentEquals("Male")) {
            holder.gender.setText("เพศ: ผู้");
        } else {
            holder.gender.setText("เพศ: เมีย");
        }

        holder.des.setText("ขนาด: " + petData.getSize());

        if (petData.getImage().length() > 0) {
            Glide.with(ct).load(petData.getImage()).into(holder.image);
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        holder.width.setText("น้ำหนัก: " + petData.getWidth() + " กิโลกรัม");


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct, PetActivity.class);
                intent.putExtra("KEY", petData.getId());
                ct.startActivity(intent);
            }
        });

        calEvent(position, petData);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title, gender, des, birth, width;
        public CardView layout;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = (CardView) itemView.findViewById(R.id.new_main_card_layout);
            des = (TextView) itemView.findViewById(R.id.new_main_card_des_title);
            title = (TextView) itemView.findViewById(R.id.new_main_card_title);
            image = (ImageView) itemView.findViewById(R.id.new_main_card_image);
            gender = (TextView) itemView.findViewById(R.id.new_main_card_gender);
            birth = (TextView) itemView.findViewById(R.id.new_main_card_birthday);
            width = (TextView) itemView.findViewById(R.id.new_main_card_width);


        }
    }

    private void calEvent(Integer position, PetData petData) {


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

                            DataSnapshot specialData = dataSnapshot.child("Special_Event");

                            if (petData.getBreed().contentEquals("ลาบราดอร์รีทรีฟเวอร์")) {

                                DataSnapshot s = specialData.child("ลาบราดอร์รีทรีฟเวอร์");

                                DataSnapshot infoShot2 = s.child("Info");
                                DataSnapshot dateShot2 = s.child("Date");
                                DataSnapshot loopShot2 = s.child("Loop");


                                PetEvent petEvent2 = new PetEvent();
                                petEvent2.createEvent(getDataToString(infoShot2,"name"),getDataToString(infoShot2,"title"),getDataToBoolean(infoShot2,"loop"))
                                        .createDate(getDataToInteger(dateShot2,"day"),getDataToInteger(dateShot2,"month"),getDataToInteger(dateShot2,"year"))
                                        .createLoop(getDataToInteger(loopShot2,"day"),getDataToInteger(loopShot2,"month"),getDataToInteger(loopShot2,"year"));


                                arr.add(petEvent2);
                            } else {

                                DataSnapshot s = specialData.child("อื่น");

                                DataSnapshot infoShot2 = s.child("Info");
                                DataSnapshot dateShot2 = s.child("Date");
                                DataSnapshot loopShot2 = s.child("Loop");


                                PetEvent petEvent2 = new PetEvent();
                                petEvent2.createEvent(getDataToString(infoShot2,"name"),getDataToString(infoShot2,"title"),getDataToBoolean(infoShot2,"loop"))
                                        .createDate(getDataToInteger(dateShot2,"day"),getDataToInteger(dateShot2,"month"),getDataToInteger(dateShot2,"year"))
                                        .createLoop(getDataToInteger(loopShot2,"day"),getDataToInteger(loopShot2,"month"),getDataToInteger(loopShot2,"year"));


                                arr.add(petEvent2);
                            }


                            CalendarAlgorithmLibrary calendarAlgorithmLibrary = new CalendarAlgorithmLibrary();
                            Map<String, ArrayList<PetEvent>> map = calendarAlgorithmLibrary.load(arr, petData.getYear(), petData.getMonth() - 1, petData.getDay());
                            ArrayList<String> arrDate = calendarAlgorithmLibrary.getStringFromMap(map);

                            Integer id = new Random().nextInt(1000000);

                            for (int i = 0; i < arrDate.size(); i++) {

                                Integer count = map.get(arrDate.get(i)).size();


                                String strDate = arrDate.get(i);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date date = sdf.parse(strDate);
                                    Calendar cal = Calendar.getInstance();
                                    Calendar today = Calendar.getInstance();

                                    cal.setTime(date);
                                    cal.set(Calendar.HOUR, 8);
                                    cal.set(Calendar.MINUTE, 0);
                                    cal.set(Calendar.SECOND, 0);


                                    if (cal.get(Calendar.YEAR) >= today.get(Calendar.YEAR)) {
                                        if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
                                            if (cal.get(Calendar.MONTH) >= today.get(Calendar.MONTH)) {
                                                if (cal.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
                                                    if (cal.get(Calendar.DAY_OF_MONTH) >= today.get(Calendar.DAY_OF_MONTH)) {
                                                        setEvent(cal, petData.getId(), petData.getName(), count, id);
                                                    }
                                                } else {
                                                    setEvent(cal, petData.getId(), petData.getName(), count, id);

                                                }

                                            }
                                        } else {
                                            if (cal.get(Calendar.YEAR) - today.get(Calendar.YEAR) <= 1) {
                                                setEvent(cal, petData.getId(), petData.getName(), count, id);
                                            }

                                        }

                                    }


                                    //Long a = cal.getTimeInMillis();

                                    //Log.e(petData.getId(),a.toString());


                                    //events.add(new EventDay(cal, R.drawable.ic_notification));
                                } catch (ParseException e) {
                                    Log.e("", "");
                                }


                            }



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

    private String getDataToString(DataSnapshot dataSnapshot,String child){
        return dataSnapshot.child(child).getValue(String.class);
    }

    private Integer getDataToInteger(DataSnapshot dataSnapshot,String child){
        return dataSnapshot.child(child).getValue(Integer.class);
    }

    private Boolean getDataToBoolean(DataSnapshot dataSnapshot,String child){
        return dataSnapshot.child(child).getValue(Boolean.class);
    }

    private void setEvent(Calendar targetCal, String petID, String petName, Integer count, Integer id) {
        final Integer _id = (int) System.currentTimeMillis();
        Long s = targetCal.getTimeInMillis();
        Integer a = targetCal.get(Calendar.DAY_OF_MONTH);
        Integer b = targetCal.get(Calendar.MONTH) + 1;
        Integer c = targetCal.get(Calendar.YEAR);

        //Integer count = event.size();
        // Log.e("ID",_id.toString());
        //Log.e("R",id.toString());


        //Log.e(petID + "/" + s, a + "/" + b + "/" + c);
        if (s >= 0) {
            Intent intent = new Intent(act.getBaseContext(), AlarmReceiver.class);
            intent.putExtra("TITLE", petName + " มีรายการที่ต้องทำ " + count + " รายการในวันนี้");
            intent.putExtra("MESSAGE", "แตะเพื่อดูรายละเอียด");
            intent.putExtra("PETID", petID);
            intent.putExtra("ID", _id);


            PendingIntent pendingIntent = PendingIntent.getBroadcast(act.getBaseContext(), _id, intent, 0);
            //PendingIntent pendingIntent = PendingIntent.getActivity(act.getBaseContext(),_id , intent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager = (AlarmManager) act.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        } else {
            Log.e("askda", "asdjasjd");
        }

    }

}
