package com.up.bc.myapplicationproject.Data;

public class PetEvent {

    Integer loopDay, loopMonth, loopYear, startDay, startMonth, startYear, loopCount;
    String name, title;
    Boolean loop;

    public PetEvent createLoop(Integer day,Integer month,Integer year){
        this.loopDay = day;
        this.loopMonth = month;
        this.loopYear = year;
        return this;
    }

    public PetEvent createEvent(String name,String title,Boolean loop){
        this.loop = loop;
        this.name = name;
        this.title = title;
        return this;

    }

    public PetEvent createDate(Integer day,Integer month,Integer year){
        this.startDay = day;
        this.startMonth = month;
        this.startYear = year;
        return this;
    }

    public Integer getLoopDay() {
        return loopDay;
    }

    public void setLoopDay(Integer loopDay) {
        this.loopDay = loopDay;
    }

    public Integer getLoopMonth() {
        return loopMonth;
    }

    public void setLoopMonth(Integer loopMonth) {
        this.loopMonth = loopMonth;
    }

    public Integer getLoopYear() {
        return loopYear;
    }

    public void setLoopYear(Integer loopYear) {
        this.loopYear = loopYear;
    }

    public Integer getStartDay() {
        return startDay;
    }

    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }

    public Integer getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(Integer loopCount) {
        this.loopCount = loopCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getLoop() {
        return loop;
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }
}
