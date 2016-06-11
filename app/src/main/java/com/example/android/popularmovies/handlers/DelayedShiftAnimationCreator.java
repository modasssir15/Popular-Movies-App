package com.example.android.popularmovies.handlers;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by modassirpc on 11-06-2016.
 */
public class DelayedShiftAnimationCreator implements AnimationCreator {
    protected static final int ANIMATION_DURATION = 200;
    protected static final int ANIMATION_OFFSET_DELAY = 100;
    private int delay;

    public DelayedShiftAnimationCreator() {
        this.delay = ANIMATION_OFFSET_DELAY;
    }

    public Animation create() {
        Animation anim = new TranslateAnimation(2, 1.0F, 2, 0.0f, 2, 0.0f, 2, 0.0f);
        anim.setDuration(200);
        int i = this.delay + ANIMATION_OFFSET_DELAY;
        this.delay = i;
        anim.setStartOffset((long) i);
        return anim;
    }
}