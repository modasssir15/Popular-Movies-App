package com.example.android.popularmovies.handlers;

import android.view.View;
import android.widget.TextView;

/**
 * Created by modassirpc on 11-06-2016.
 */
public class SimpleDetailViewHandler {
    private final TextView mBody;
    private final TextView mHeader;

    public SimpleDetailViewHandler(TextView mHeader, TextView mBody) {
        this.mHeader = mHeader;
        this.mBody = mBody;
    }

    public void update(String value, String check, AnimationCreator animationCreator) {
        if (this.mBody == null) {
            return;
        }
        if ((check).equals("")) {
            this.mBody.setVisibility(View.GONE);
            if (this.mHeader != null) {
                this.mHeader.setVisibility(View.GONE);
                return;
            }
            return;
        }
        this.mBody.setText(value);
        this.mBody.setVisibility(View.VISIBLE);
        if (this.mHeader != null) {
            this.mHeader.setVisibility(View.VISIBLE);
            if (animationCreator != null) {
                this.mHeader.startAnimation(animationCreator.create());
            }
        }
        if (animationCreator != null) {
            this.mBody.startAnimation(animationCreator.create());
        }
    }
}