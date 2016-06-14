package com.example.android.popularmovies;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by modassirpc on 09-06-2016.
 */
public class DetailActivity extends AppCompatActivity{

    ImageView movieImage;
    TextView releaseDate;
    TextView votes;
    RatingBar rate;
    Movie movie;
    TextView t;
    String movie_id;
    ProgressDialog pw;
    Toolbar toolBar;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_detail);
        movie_id = (String) getIntent().getStringExtra("movie_id");
        this.toolBar = ((Toolbar)findViewById(R.id.toolBar));
        setSupportActionBar(this.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("movie_id", movie_id);
        detailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commitAllowingStateLoss();



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
