package com.example.android.popularmovies.handlers;

import android.widget.TextView;

import com.example.android.popularmovies.Review;

/**
 * Created by modassirpc on 11-06-2016.
 */
public class SimpleReviewHandler {

    private final TextView mBody;

    public SimpleReviewHandler(TextView body){
       this.mBody = body;
    }
    public void update(Review value, AnimationCreator animationCreator) {
        this.mBody.setText(value.name+" : "+value.message);
        if (animationCreator != null) {
            this.mBody.startAnimation(animationCreator.create());
        }

    }
}
