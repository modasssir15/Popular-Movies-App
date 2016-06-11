package com.example.android.popularmovies;

import java.io.Serializable;

/**
 * Created by modassirpc on 11-06-2016.
 */
public class Trailer implements Serializable {
    public String movieId;
    public String name;
    public String trailerId;
    public String trailer_url;


    public Trailer(){
        movieId = "";
        name = "";
        trailerId = "";
        trailer_url = "";

    }

}
