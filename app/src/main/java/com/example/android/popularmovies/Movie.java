package com.example.android.popularmovies;

import java.io.Serializable;

/**
 * Created by modassirpc on 08-06-2016.
 */
public class Movie implements Serializable{
    public String defaultbackdropurl;
    public String defaultposterurl;
    public String id;
    public String name;
    public String overview;
    public String rating;
    public String released;
    public String revenue;
    public String score;
    public String type;
    public String votes;
    public String votes_total;

    public Movie() {
        this.votes = "";
        this.votes_total = "";
        this.rating = "";
        this.id = "";
        this.type = "";
        this.name = "";
        this.released = "";
        this.score = "";
        this.overview = "";
        this.revenue = "";
        this.defaultposterurl = "";
        this.defaultbackdropurl = "";
    }
}
