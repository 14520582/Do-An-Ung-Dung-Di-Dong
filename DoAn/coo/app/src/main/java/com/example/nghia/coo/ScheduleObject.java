package com.example.nghia.coo;

/**
 * Created by nghia on 17/11/2016.
 */

public class ScheduleObject {
    public String nameevent,datetime,imagecover,userkey,recipekey;
    public int idevent;
    public ScheduleObject(){}
    public ScheduleObject(int idevent, String nameevent, String datetime, String imagecover, String userkey, String recipekey) {
        this.nameevent = nameevent;
        this.datetime = datetime;
        this.imagecover = imagecover;
        this.userkey = userkey;
        this.recipekey = recipekey;
        this.idevent=idevent;
    }
}
