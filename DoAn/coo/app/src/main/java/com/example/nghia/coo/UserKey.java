package com.example.nghia.coo;

import java.io.Serializable;

/**
 * Created by nghia on 16/11/2016.
 */

public class UserKey implements Serializable {
    public String user,key;
    public UserKey(){}
    public UserKey(String user, String key) {
        this.user = user;
        this.key = key;
    }
}
