package com.up.bc.myapplicationproject;

import com.up.bc.myapplicationproject.Data.PetInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionTool {

    ArrayList<String> gender, size, breed;

    HashMap<String,ArrayList<String>> map;

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

        this.map = new HashMap<>();
        addMap("ตุ๊กตา","ชิวาว่า");
        addMap("ตุ๊กตา","ปอมเมอเรเนียน");
        addMap("เล็ก","ชิสุ");
        addMap("เล็ก","ยอร์คเชียร์เทอร์เรีย");
        addMap("เล็ก","บีเกิล");
        addMap("เล็ก","ปั๊ก");
        addMap("กลาง","อิงลิช บูลล์ด็อก");
        addMap("กลาง","ไซบีเรียน ฮัสกี้");
        addMap("ใหญ่","โกลเด้น รีทรีฟเวอร์");
        addMap("ใหญ่","ลาบราดอร์รีทรีฟเวอร์");







    }

    private void addMap(String key ,String value){
        ArrayList<String> arr = this.map.get(key);
        if(arr != null){
            arr.add(value);
            this.map.put(key,arr);
        }else {
            ArrayList<String> arrs = new ArrayList<>();
            arrs.add(value);
            this.map.put(key,arrs);
        }
    }

    /*
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
    */

    /*

    public String checkBreedPosition(ArrayList<PetInfo> arrayList, Integer position){
        return arrayList.get(position).getName();
    }


    public String checkBreedPosition(String size ,Integer position){
        return loadBreed(size).get(position);
    }

    public Integer checkBreed(String name){
        return breed.indexOf(name);
    }

    public Integer checkBreed(String name,String size){
        return loadBreed(size).indexOf(name);
    }

    public ArrayList<String> loadBreed(String name){
        return map.get(name);
    }

    */

    public String checkGender(Integer position){
        if(position == 0){
            return "Male";
        }else {
            return "Female";
        }
    }

    /*

    public List<String> getBreedList(){
        return this.breed;
    }

    public List<String> getSizeList() {
        return this.size;
    }
    */

    public List<String> getGenderList() {
        return gender;
    }

    public List<String> getBoolList(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("ทราบ");
        strings.add("ไม่ทราบ");
        return strings;
    }

}
