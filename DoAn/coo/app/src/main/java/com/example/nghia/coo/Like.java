package com.example.nghia.coo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nghia on 23/11/2016.
 */

public class Like implements Serializable {
    public int count;
  //  public ArrayList<String> liker=new ArrayList<String>();

    public Like() {
        this.count=0;
    //    this.liker.add("default");
    }

    public Like(int count, ArrayList<String> liker) {
        this.count = count;
      //  this.liker = liker;
    }
}
