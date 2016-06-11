package com.example.android.popularmovies.handlers;

import android.text.Html;
import android.widget.TextView;

import com.example.android.popularmovies.Movie;

/**
 * Created by modassirpc on 11-06-2016.
 */
public class MediumTitleViewHandler {
    private final int mColorGrey;
    private final TextView mTitleView;

    public MediumTitleViewHandler(TextView mTitle, int colorGrey) {
        this.mTitleView = mTitle;
        this.mColorGrey = colorGrey;
    }

    public void update(Movie movie) {
        if (this.mTitleView != null) {
            Object title;
            String str;
            TextView textView = this.mTitleView;

            title =  ( "<br><font color=\"" + this.mColorGrey + "\"><small>(" + movie.name + ")</small></font>" );

            StringBuilder stringBuilder = new StringBuilder(String.valueOf(title));
            textView.setText(Html.fromHtml(stringBuilder.toString()));
        }
    }
}
