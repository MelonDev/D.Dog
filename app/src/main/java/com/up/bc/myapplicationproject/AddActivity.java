package com.up.bc.myapplicationproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.up.bc.myapplicationproject.Data.PetData;
import com.up.bc.myapplicationproject.Tools.PermissionRequest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private PermissionRequest permissionRequest;
    MaterialSpinner genderSpin, sizeSpin, breedSpin;

    Integer year, month, day;

    String key;
    Integer id;
    DatePickerDialog datePickerDialog;

    PetData petDatas;

    Boolean changeImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final FunctionTool functionTool = new FunctionTool();

        LinearLayout backBtn = (LinearLayout) findViewById(R.id.add_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        genderSpin = (MaterialSpinner) findViewById(R.id.add_drop_gender);
        genderSpin.setItems(functionTool.getGenderList());

        sizeSpin = (MaterialSpinner) findViewById(R.id.add_drop_size);
        sizeSpin.setItems(functionTool.getSizeList());

        breedSpin = (MaterialSpinner) findViewById(R.id.add_drop_breed);
        breedSpin.setItems(functionTool.getBreedList());

        permissionRequest = new PermissionRequest().load(AddActivity.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("ID");

            sizeSpin.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> setBreed(item, 0));

            TextView title = (TextView) findViewById(R.id.add_title);

            if (id == 0) {

                title.setText("เพิ่มสัตว์เลี้ยง");

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                setTextDate(year, month, day);

                datePickerDialog = new DatePickerDialog(
                        AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setTextDate(year, month, dayOfMonth);


                    }
                }, year, month, day);

            } else if (id == 1) {
                title.setText("แก้ไขสัตว์เลี้ยง");

                key = extras.getString("KEY");

                Log.e("Key", "key: " + key);

                permissionRequest.setWaitDialog("กำลังโหลด");
                FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").child(key).child("Info").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            permissionRequest.stopWaitDialog();
                            PetData petData = dataSnapshot.getValue(PetData.class);

                            petDatas = petData;

                            year = petData.getYear();
                            month = petData.getMonth() - 1;
                            day = petData.getDay();

                            setTextDate(year, month, day);

                            EditText nameEdit = (EditText) findViewById(R.id.add_name_edit);
                            EditText widthEdit = (EditText) findViewById(R.id.add_width_edit);

                            nameEdit.setText(petData.getName().toString());
                            widthEdit.setText(petData.getWidth().toString());

                            if (petData.getGender().contentEquals("Male")) {
                                genderSpin.setSelectedIndex(0);
                            } else {
                                genderSpin.setSelectedIndex(1);
                            }
                            sizeSpin.setSelectedIndex(petData.getSize());

                            if (functionTool.loadBreed(functionTool.getNameOfSize(petData.getSize())) != null) {
                                Integer s = functionTool.checkBreed(petData.getBreed(), functionTool.getNameOfSize(petData.getSize()));
                                if (s >= 0) {
                                    setBreed(functionTool.getNameOfSize(petData.getSize()), s);
                                    //breedSpin.setSelectedIndex(s);
                                }
                            } else {
                                breedSpin.setItems(new ArrayList<String>());
                            }


                            datePickerDialog = new DatePickerDialog(
                                    AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    setTextDate(year, month, dayOfMonth);


                                }
                            }, year, month, day);

                            ImageView imageView = (ImageView) findViewById(R.id.add_image_image);
                            if (petData.getImage().length() > 0) {

                                try {
                                    Glide.with(AddActivity.this).load(petData.getImage()).into(imageView);
                                } catch (Exception e) {
                                    Log.e("", "");
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("", "");
                    }
                });

            }
        }


        CardView imageBtn = (CardView) findViewById(R.id.add_image_btn);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequest.request();
            }
        });

        RelativeLayout dateBtn = (RelativeLayout) findViewById(R.id.add_date_btn);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        CardView saveBtn = (CardView) findViewById(R.id.add_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });


    }

    private void setBreed(String name, Integer position) {
        ArrayList<String> list = new FunctionTool().loadBreed(name);
        if (list != null) {
            breedSpin.setItems(list);
            breedSpin.setSelectedIndex(position);

        } else {
            breedSpin.setItems(new ArrayList<String>());
        }

    }

    private void setTextDate(Integer year, Integer month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;

        TextView dateText = (TextView) findViewById(R.id.add_date_text);

        dateText.setText(day + "/" + (month + 1) + "/" + year);

    }

    private void upload() {
        permissionRequest.setWaitDialog("กำลังอัปโหลด");

        EditText nameEdit = (EditText) findViewById(R.id.add_name_edit);
        EditText widthEdit = (EditText) findViewById(R.id.add_width_edit);

        String name = nameEdit.getText().toString();
        String widthStr = widthEdit.getText().toString();

        Integer width = permissionRequest.convertStringToInteger(widthStr);
        if (width >= 0) {
            if (name.length() > 0) {

                final String keys = FirebaseDatabase.getInstance().getReference().push().getKey().toString();

                if (permissionRequest.getFilepath() != null) {

                    if (id == 0) {
                        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").child(keys);
                        refer(keys, ref);
                    } else {
                        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").child(key);
                        refer(key, ref);

                    }


                } else if (id == 1 && !changeImage) {
                    final StorageReference ref = FirebaseStorage.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").child(key);
                    refer(key, ref);
                } else {
                    Toast.makeText(AddActivity.this, "ยังไม่ได้ใส่รูป", Toast.LENGTH_SHORT).show();
                    permissionRequest.stopWaitDialog();

                }


            } else {
                Toast.makeText(this, "ไม่ได้ใส่ชื่อ", Toast.LENGTH_SHORT).show();
                permissionRequest.stopWaitDialog();

            }
        } else {
            Toast.makeText(this, "น้ำหนักไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
            permissionRequest.stopWaitDialog();

        }


    }

    private void refer(final String keys, StorageReference refs) {
        final StorageReference ref = refs;
        if (changeImage) {
            ref.putBytes(permissionRequest.getByte()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            EditText nameEdit = (EditText) findViewById(R.id.add_name_edit);
                            EditText widthEdit = (EditText) findViewById(R.id.add_width_edit);

                            String name = nameEdit.getText().toString();
                            String widthStr = widthEdit.getText().toString();

                            Integer width = permissionRequest.convertStringToInteger(widthStr);

                            PetData petData = new PetData();
                            petData.setId(keys);
                            petData.setYear(year);
                            petData.setMonth(month + 1);
                            petData.setDay(day);
                            FunctionTool functionTool = new FunctionTool();
                            if (functionTool.loadBreed(functionTool.getNameOfSize(sizeSpin.getSelectedIndex())) != null) {
                                petData.setBreed(functionTool.checkBreedPosition(functionTool.getNameOfSize(sizeSpin.getSelectedIndex()),breedSpin.getSelectedIndex()));
                            }else {
                                petData.setBreed("");
                            }
                            petData.setGender(functionTool.checkGender(genderSpin.getSelectedIndex()));
                            petData.setSize(sizeSpin.getSelectedIndex());
                            petData.setName(name);
                            petData.setWidth(width);
                            petData.setImage(uri.toString());


                            FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").child(keys).child("Info")
                                    .setValue(petData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    permissionRequest.stopWaitDialog();
                                    Toast.makeText(AddActivity.this, "เรียบร้อย", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(AddActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    permissionRequest.stopWaitDialog();
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(AddActivity.this, "ยกเลิกแล้ว", Toast.LENGTH_SHORT).show();
                    permissionRequest.stopWaitDialog();

                }
            });
        } else {
            EditText nameEdit = (EditText) findViewById(R.id.add_name_edit);
            EditText widthEdit = (EditText) findViewById(R.id.add_width_edit);

            String name = nameEdit.getText().toString();
            String widthStr = widthEdit.getText().toString();

            Integer width = permissionRequest.convertStringToInteger(widthStr);

            PetData petData = new PetData();
            petData = petDatas;
            petData.setId(keys);
            petData.setYear(year);
            petData.setMonth(month + 1);
            petData.setDay(day);
            FunctionTool functionTool = new FunctionTool();
            if (functionTool.loadBreed(functionTool.getNameOfSize(sizeSpin.getSelectedIndex())) != null) {
                petData.setBreed(functionTool.checkBreedPosition(functionTool.getNameOfSize(sizeSpin.getSelectedIndex()),breedSpin.getSelectedIndex()));
            }else {
                petData.setBreed("");
            }
            petData.setGender(new FunctionTool().checkGender(genderSpin.getSelectedIndex()));
            petData.setSize(sizeSpin.getSelectedIndex());
            petData.setName(name);
            petData.setWidth(width);

            FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Pets").child(keys).child("Info")
                    .setValue(petData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    permissionRequest.stopWaitDialog();

                    Toast.makeText(AddActivity.this, "เรียบร้อย", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        permissionRequest.setFilepath(data.getData());

        ImageView imageView = (ImageView) findViewById(R.id.add_image_image);
        Glide.with(this).load(permissionRequest.getFilepath()).into(imageView);
        changeImage = true;


    }


}
