package com.up.bc.myapplicationproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.up.bc.myapplicationproject.Data.UserData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            CardView loginBtn = (CardView) findViewById(R.id.login_btn);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showNotification(LoginActivity.this,"Hello","Test",new Intent(LoginActivity.this,LoginActivity.class));
                    login();
                }
            });
        }

        TextView signup = (TextView)findViewById(R.id.login_signup_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SIgnUpActivity.class);
                startActivity(intent);
            }
        });




    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        int color = ContextCompat.getColor(context, R.color.colorPrimary);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(false)
                .setColor(color)
                .setVibrate(new long[] { 500, 1000, 500 })
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }


    private void login() {

        EditText userEdit = (EditText) findViewById(R.id.login_email_edittext);
        EditText passwordEdit = (EditText) findViewById(R.id.login_password_edittext);

        String username = userEdit.getText().toString();
        final String pass = passwordEdit.getText().toString();


        if (username.length() > 0 && passwordEdit.length() > 0) {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                String error = task.getException().getLocalizedMessage().toString();


                                if (error.indexOf("invalid") > -1) {
                                    Toast.makeText(LoginActivity.this, "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();

                                } else if (error.indexOf("badly formatted") > -1) {
                                    Toast.makeText(LoginActivity.this, "รูปแบบอีเมลไม่ถูกต้อง", Toast.LENGTH_SHORT).show();

                                } else if (error.indexOf("no user") > -1) {
                                    Toast.makeText(LoginActivity.this, "ลีเมลนี้ยังไม่มีการลงทะเบียน", Toast.LENGTH_SHORT).show();

                                } else if (error.indexOf("network error") > -1) {
                                    Toast.makeText(LoginActivity.this, "กรุณาเช็คการเชื่อมต่่ออินเทอร์เน็ต", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }
                    });


        } else {
            Toast.makeText(LoginActivity.this, "กรอกไม่ครบ", Toast.LENGTH_SHORT).show();

        }


    }

}
