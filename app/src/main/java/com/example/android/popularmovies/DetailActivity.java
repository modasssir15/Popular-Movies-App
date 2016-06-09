package com.example.android.popularmovies;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_detail);
        movie_id = getIntent().getStringExtra("movie_id");
        
        movieImage = (ImageView) findViewById(R.id.imageMovie);
        releaseDate = (TextView) findViewById(R.id.release);
        t = (TextView) findViewById(R.id.text);
        votes = (TextView) findViewById(R.id.votes);
        rate = (RatingBar) findViewById(R.id.ratingBar);
        new GetMovies(movie_id).execute(new Void[0]);
        

    }
    private class GetMovies extends AsyncTask<Void, Void, Integer> {
        String movieid;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           pw = new ProgressDialog(DetailActivity.this);
            pw.setMessage("Fetching Movies Info...");
            pw.setCancelable(false);
            pw.show();
        }


        private GetMovies(String movieId) {
            movieid = movieId;
        }

        protected Integer doInBackground(Void... params) {

            DetailActivity.this.movie = JSONHelper.GetMovieById(DetailActivity.this, movieid);
            return Integer.valueOf(1);
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Picasso.with(DetailActivity.this).load(movie.defaultbackdropurl).into(movieImage);
            releaseDate.setText("Released on: " + movie.released);
            votes.setText("Total Votes: " + movie.votes_total);
            t.setText(movie.overview);
            rate.setRating(Float.parseFloat(movie.rating) / 2.0F);

            pw.dismiss();
        }
    }
}
