package com.example.nghia.coo;

import java.util.ArrayList;

/**
 * Created by nghia on 20/11/2016.
 */

public class RecipeFull {
    public ArrayList<Recipe> currecipe=new ArrayList<Recipe>();
    public ArrayList<UserKey> curuserkey=new ArrayList<UserKey>();
    public ArrayList<String> curname=new ArrayList<String>();
    public ArrayList<String> curimage=new ArrayList<String>();
    public ArrayList<Boolean> curBoolean=new ArrayList<Boolean>();

    public RecipeFull(ArrayList<Recipe> currecipe, ArrayList<UserKey> curuserkey, ArrayList<String> curname, ArrayList<String> curimage,ArrayList<Boolean> curboolean) {
        this.currecipe = currecipe;
        this.curuserkey = curuserkey;
        this.curname = curname;
        this.curimage = curimage;
        this.curBoolean=curboolean;
    }

    public RecipeFull() {
    }
}
