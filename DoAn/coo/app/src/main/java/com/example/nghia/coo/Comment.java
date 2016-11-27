package com.example.nghia.coo;

import java.io.Serializable;

/**
 * Created by nghia on 23/11/2016.
 */

public class Comment implements Serializable {
    public String usercomment;
    public String comment;

    public Comment() {
    }

    public Comment(String usercomment, String comment) {
        this.usercomment = usercomment;
        this.comment = comment;
    }
}
