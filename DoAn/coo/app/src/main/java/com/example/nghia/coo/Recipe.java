package com.example.nghia.coo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nghia on 07/11/2016.
 */

public class Recipe implements Serializable {
    public String namerecipe, image, material;
    public String time,ration;
    public ArrayList<String> implement=new ArrayList<String>();
    public Recipe(){}
    public Recipe(String Namerecipe, String Image, String Material, String Time, String Ration, ArrayList<String> Recipes){
        namerecipe=Namerecipe;
        image=Image;
        material=Material;
        time=Time;
        ration=Ration;

        implement=Recipes;
    }
}
