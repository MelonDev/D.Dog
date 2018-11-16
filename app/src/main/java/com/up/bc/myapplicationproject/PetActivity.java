package com.up.bc.myapplicationproject;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.up.bc.myapplicationproject.Data.PetData;
import com.up.bc.myapplicationproject.Tools.PermissionRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class PetActivity extends AppCompatActivity {

    String key;

    PermissionRequest permissionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        final ImageView image = (ImageView) findViewById(R.id.pet_image);
        final TextView title = (TextView) findViewById(R.id.pet_title);
        final TextView gender = (TextView) findViewById(R.id.pet_gender);
        final TextView width = (TextView) findViewById(R.id.pet_width);
        final TextView des = (TextView) findViewById(R.id.pet_des_title);
        final TextView birth = (TextView) findViewById(R.id.pet_birthday);
        final TextView breed = (TextView) findViewById(R.id.pet_breed);

        permissionRequest = new PermissionRequest();

        ScrollView scrollView = (ScrollView)findViewById(R.id.pet_scrollview);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key = extras.getString("KEY");

            ImageView editBtn = (ImageView)findViewById(R.id.pet_edit_btn);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PetActivity.this,AddActivity.class);
                    intent.putExtra("ID",1);
                    intent.putExtra("KEY",key);
                    startActivity(intent);
                }
            });

            ImageView deleteBtn = (ImageView)findViewById(R.id.pet_delete_btn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    permissionRequest.setWaitDialog(PetActivity.this,"กำลังลบ");
                    FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            permissionRequest.stopWaitDialog();
                            Toast.makeText(PetActivity.this, "ลบเรียบร้อย", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });

            FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").child(key).child("Info").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        PetData petData = dataSnapshot.getValue(PetData.class);

                        title.setText(petData.getName());
                        birth.setText("วันเกิด: " + petData.getDay() + "/" + petData.getMonth() + "/" + petData.getYear());

                        if (petData.getGender().contentEquals("Male")) {
                            gender.setText("เพศ: ผู้");
                        } else {
                            gender.setText("เพศ: เมีย");
                        }

                        des.setText("ขนาด: " + new FunctionTool().getNameOfSize(petData.getSize()));

                        if (petData.getImage().length() > 0) {
                            Glide.with(PetActivity.this).load(petData.getImage()).into(image);
                            image.setVisibility(View.VISIBLE);
                        } else {
                            image.setVisibility(View.GONE);
                        }

                        width.setText("น้ำหนัก: " + petData.getWidth() + " กิโลกรัม");

                        breed.setText("พันธุ์: " + petData.getBreed());


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("", "");
                }
            });
        }

        LinearLayout backBtn = (LinearLayout) findViewById(R.id.pet_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CardView calendarBtn = (CardView)findViewById(R.id.pet_calendar_btn);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetActivity.this,CalendarActivity.class);
                intent.putExtra("KEY",key);
                startActivity(intent);
            }
        });

        CardView foodBtn = (CardView)findViewById(R.id.pet_food_btn);
        foodBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PetActivity.this,FoodActivity.class);
            intent.putExtra("KEY",key);
            intent.putExtra("ID",0);
            startActivity(intent);
        });

        CardView objectBtn = (CardView)findViewById(R.id.pet_object_btn);
        objectBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PetActivity.this,FoodActivity.class);
            intent.putExtra("KEY",key);
            intent.putExtra("ID",1);
            startActivity(intent);
        });

    }

}
