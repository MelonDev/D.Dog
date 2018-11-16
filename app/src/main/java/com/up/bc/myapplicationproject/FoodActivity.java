package com.up.bc.myapplicationproject;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.up.bc.myapplicationproject.Data.PetData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class FoodActivity extends AppCompatActivity {

    String key;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key = extras.getString("KEY");
            id = extras.getInt("ID");

            TextView title = (TextView) findViewById(R.id.food_title);
            TextView text = (TextView) findViewById(R.id.food_text);
            TextView textTitle = (TextView) findViewById(R.id.food_text_title);
            ImageView image = (ImageView) findViewById(R.id.food_image);


            if (id == 0) {
                title.setText("อาหาร");
            } else if (id == 1) {
                title.setText("อุปกรณ์เสริมทักษะ");
            }

            FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").child(key).child("Info").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        PetData petData = dataSnapshot.getValue(PetData.class);

                        textTitle.setText("ช่วงอายุ: " + getTextAge(petData.getYear(), petData.getMonth(), petData.getDay()));

                        if (id == 0) {

                            if (getTextAge(petData.getYear(), petData.getMonth(), petData.getDay()).contentEquals("0-10 สัปดาห์")) {
                                Picasso.get().load(R.drawable.dog_milk).noFade().into(image);

                            } else {
                                Picasso.get().load(R.drawable.dog_food).noFade().into(image);

                            }

                            ArrayList<String> foodList = new CreateEvent().getFood(petData.getBreed());
                            text.setText(foodList.get(getAgePosition(petData.getYear(), petData.getMonth(), petData.getDay())));
                        } else if (id == 1) {
                            Picasso.get().load(R.drawable.dog_toy).noFade().into(image);

                            Boolean e = false;
                            if (petData.getBreed().contentEquals("ลาบราดอร์รีทรีฟเวอร์") || petData.getBreed().contentEquals("โกลเด้น รีทรีฟเวอร์")) {
                                e = true;
                            } else {
                                e = false;
                            }
                            ArrayList<String> toyList = new CreateEvent().getObject(e);
                            text.setText(toyList.get(getAgePosition(petData.getYear(), petData.getMonth(), petData.getDay())));

                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("", "");
                }
            });


            LinearLayout backBtn = (LinearLayout) findViewById(R.id.food_back_btn);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


    }

    public Integer getAgePosition(Integer year, Integer month, Integer day) {

        Calendar start = Calendar.getInstance();

        Calendar end = Calendar.getInstance();

        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month - 1);
        start.set(Calendar.DAY_OF_MONTH, day);

        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        Long between = TimeUnit.MILLISECONDS.toDays(Math.abs(end.getTimeInMillis() - start.getTimeInMillis()));

        if (between < 70) {
            return 0;
        } else if (between < 84) {
            return 1;
        } else if (between < 360) {
            return 2;
        } else {
            return 3;
        }

    }

    private String getTextAge(Integer year, Integer month, Integer day) {


        Calendar start = Calendar.getInstance();

        Calendar end = Calendar.getInstance();

        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month - 1);
        start.set(Calendar.DAY_OF_MONTH, day);

        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        Long between = TimeUnit.MILLISECONDS.toDays(Math.abs(end.getTimeInMillis() - start.getTimeInMillis()));


        if (between < 70) {
            return "0-10 สัปดาห์";
        } else if (between < 84) {
            return "10-12 สัปดาห์";
        } else if (between < 360) {
            return "3-12 เดือน";
        } else {
            return "โตเต็มวัย";
        }
    }

}
