package com.up.bc.myapplicationproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.up.bc.myapplicationproject.Data.PetData;
import com.up.bc.myapplicationproject.FunctionTool;
import com.up.bc.myapplicationproject.PetActivity;
import com.up.bc.myapplicationproject.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
        holder.birth.setText("วันเกิด: "+ petData.getDay()+"/"+petData.getMonth()+"/"+petData.getYear());

        if(petData.getGender().contentEquals("Male")){
            holder.gender.setText("เพศ: ผู้");
        }else {
            holder.gender.setText("เพศ: เมีย");
        }

        holder.des.setText("ขนาด: "+ new FunctionTool().getNameOfSize(petData.getSize()));

        if(petData.getImage().length() > 0){
            Glide.with(ct).load(petData.getImage()).into(holder.image);
            holder.image.setVisibility(View.VISIBLE);
        }else {
            holder.image.setVisibility(View.GONE);
        }

        holder.width.setText("น้ำหนัก: "+ petData.getWidth() +" กิโลกรัม");


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct,PetActivity.class);
                intent.putExtra("KEY",petData.getId());
                ct.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title,gender,des,birth,width;
        public CardView layout;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = (CardView)itemView.findViewById(R.id.new_main_card_layout);
            des = (TextView) itemView.findViewById(R.id.new_main_card_des_title);
            title = (TextView) itemView.findViewById(R.id.new_main_card_title);
            image = (ImageView) itemView.findViewById(R.id.new_main_card_image);
            gender = (TextView)itemView.findViewById(R.id.new_main_card_gender);
            birth = (TextView)itemView.findViewById(R.id.new_main_card_birthday);
            width = (TextView)itemView.findViewById(R.id.new_main_card_width);


        }
    }

}
