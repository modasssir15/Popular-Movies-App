package com.example.android.popularmovies;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by modassirpc on 10-06-2016.
 */
public class SizeableTextView extends TextView {
    public static float multiplicator;

    static {
        multiplicator = 1.0F;
    }

    public SizeableTextView(Context context) {
        super(context);
        setTextSize(0, getTextSize() * multiplicator);
    }

    public SizeableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTextSize(0, getTextSize() * multiplicator);
    }

    public SizeableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextSize(0, getTextSize() * multiplicator);
    }
}
