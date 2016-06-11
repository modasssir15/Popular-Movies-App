package com.example.android.popularmovies.handlers;

import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by modassirpc on 11-06-2016.
 */
public class ReloadProgressBarHandler {
    private static final int DELAY_500 = 500;
    private final Runnable mDelayedStop;
    private final SmoothProgressBar mProgressBar;
    private final AtomicInteger mReloadCounter;

    public ReloadProgressBarHandler(SmoothProgressBar progressBar) {
        this.mReloadCounter = new AtomicInteger(0);
        this.mDelayedStop = new Runnable() {
            @Override
            public void run() {
                if (ReloadProgressBarHandler.this.mReloadCounter.get() <= 0) {
                    ((SmoothProgressBar) SmoothProgressBar.class.cast(ReloadProgressBarHandler.this.mProgressBar)).progressiveStop();
                }
            }
        };
        this.mProgressBar = progressBar;
    }

    public void show() {
        if (this.mReloadCounter.incrementAndGet() > 0 && this.mProgressBar != null) {
            this.mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hide() {
        if (this.mReloadCounter.decrementAndGet() == 0 && this.mProgressBar != null) {
            this.mProgressBar.postDelayed(this.mDelayedStop, 500);
        }
    }
}
