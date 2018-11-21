package com.up.bc.myapplicationproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.up.bc.myapplicationproject.Data.UserData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SIgnUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        LinearLayout backBtn = (LinearLayout) findViewById(R.id.signup_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText e1 = (EditText) findViewById(R.id.signup_e1_btn);
        EditText e2 = (EditText) findViewById(R.id.signup_e2_btn);
        EditText e3 = (EditText) findViewById(R.id.signup_e3_btn);
        EditText e4 = (EditText) findViewById(R.id.signup_e4_btn);
        EditText e5 = (EditText) findViewById(R.id.signup_e5_btn);
        EditText e6 = (EditText) findViewById(R.id.signup_e6_btn);


        CardView save = (CardView) findViewById(R.id.signup_save_btn);

        save.setOnClickListener(v -> {

            String a1 = e1.getText().toString();
            String a2 = e2.getText().toString();
            String a3 = e3.getText().toString();
            String a4 = e4.getText().toString();
            String a5 = e5.getText().toString();
            String a6 = e6.getText().toString();


            if (a1.length() > 0 && a2.length() > 0 && a3.length() > 0 && a4.length() > 0 && a5.length() > 0) {
                if (a4.contentEquals(a5)) {

                    if (a3.indexOf("@gmail.com") > -1 || a3.indexOf("@hotmail.com") > -1 || a3.indexOf("@hotmail.co.th") > -1) {

                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(a3, a4).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    UserData userData = new UserData();
                                    userData.setName(a1);
                                    userData.setUsername(a2);
                                    userData.setEmail(a3);
                                    userData.setId(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                    userData.setPhone(a6);

                                    FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Info").setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(SIgnUpActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                                    //Toast.makeText(this, "Authentication Successful.", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(SIgnUpActivity.this, "อีเมลนี้ได้ลงทะเบียนแล้ว", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SIgnUpActivity.this,"กรุณาใส่เฉพาะ Gmail และ Hotmail",Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(SIgnUpActivity.this, "รหัสผ่านไม่ตรง", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SIgnUpActivity.this, "ข้อมูลไม่ครบ", Toast.LENGTH_SHORT).show();
            }

        });


    }

}
