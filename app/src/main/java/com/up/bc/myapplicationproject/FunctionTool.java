package com.up.bc.myapplicationproject;

import java.util.ArrayList;
import java.util.List;

public class FunctionTool {

    ArrayList<String> gender, size, breed;

    public FunctionTool() {

        this.gender = new ArrayList<String>();
        this.size = new ArrayList<String>();
        this.breed = new ArrayList<String>();


        gender.add("เพศผู้");
        gender.add("เพศเมีย");

        size.add("ตุ๊กตา");
        size.add("เล็ก");
        size.add("กลาง");
        size.add("ใหญ่");

        breed.add("ชิวาว่า");
        breed.add("ปอมเมอเรเนียน");
        breed.add("ชิสุ");
        breed.add("ยอร์คเชียร์เทอร์เรีย");
        breed.add("บีเกิล");
        breed.add("ปั๊ก");
        breed.add("อิงลิช บูลล์ด็อก");
        breed.add("ไซบีเรียน ฮัสกี้");
        breed.add("โกลเด้น รีทรีฟเวอร์");
        breed.add("ลาบราดอร์รีทรีฟเวอร์");


    }

    public String getNameOfSize(Integer size) {
        switch (size) {
            case 0:
                return this.size.get(0);
            case 1:
                return this.size.get(1);
            case 2:
                return this.size.get(2);
            case 3:
                return this.size.get(3);
            default:
                return "";

        }
    }


    public String checkBreedPosition(Integer position){
        return breed.get(position);
    }

    public Integer checkBreed(String name){
        return breed.indexOf(name);
    }

    public String checkGender(Integer position){
        if(position == 0){
            return "Male";
        }else {
            return "Female";
        }
    }


    public List<String> getBreedList(){
        return this.breed;
    }

    public List<String> getSizeList() {
        return this.size;
    }

    public List<String> getGenderList() {
        return gender;
    }

}
