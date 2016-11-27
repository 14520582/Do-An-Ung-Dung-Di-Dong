package com.example.nghia.coo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by nghia on 07/11/2016.
 */

public class Recipe implements Serializable {
    public String namerecipe, image, material;
    public String time,ration;
    public Date cal;
    public String userid;
    public int like;
    public int countcomment;
    public ArrayList<String> implement=new ArrayList<String>();
    public Recipe(){

    }
    public Recipe(String Namerecipe, String Image, String Material, String Time, String Ration, ArrayList<String> Recipes,Date Cal,String Userid,int curLike,int cm){
        namerecipe=Namerecipe;
        image=Image;
        material=Material;
        time=Time;
        ration=Ration;
        implement=Recipes;
        cal=Cal;
        userid=Userid;
        like=curLike;
        countcomment=cm;

    }
}
