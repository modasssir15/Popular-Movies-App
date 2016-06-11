package com.example.android.popularmovies;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.handlers.AnimationCreator;
import com.example.android.popularmovies.handlers.DelayedShiftAnimationCreator;
import com.example.android.popularmovies.handlers.MediumTitleViewHandler;
import com.example.android.popularmovies.handlers.ReloadProgressBarHandler;
import com.example.android.popularmovies.handlers.SimpleDetailViewHandler;
import com.squareup.picasso.Picasso;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by modassirpc on 10-06-2016.
 */
public class DetailFragment extends Fragment {
    String movie_id;
    Movie movie;
    TextView mTitle;
    private MediumTitleViewHandler mTitleViewHandler;
    private SimpleDetailViewHandler mReleaseDetailHandler;
    private SimpleDetailViewHandler mDescriptionDetailHandler;
    private RatingBar rate;
    private ReloadProgressBarHandler mReloadProgressBarHandler;
    private ImageView mPlayTrailer;
    private ImageView mImageBackdrop;
    private ImageView mImgDetailCover;
    private Trailer trailer;
    float rating;
    Boolean succes;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_movie_details, container, false);
        movie_id = getArguments().getString("movie_id");
        this.mTitle = (TextView) view.findViewById(R.id.txtDetailTitle);
        this.mTitleViewHandler = new MediumTitleViewHandler(this.mTitle, getResources().getColor(R.color.grey));
        TextView releaseHeader = (TextView) view.findViewById(R.id.txtDetailReleaseHeader);
        TextView release = (TextView) view.findViewById(R.id.txtDetailRelease);
        this.mReleaseDetailHandler = new SimpleDetailViewHandler(releaseHeader, release);
        TextView descriptionHeader = (TextView) view.findViewById(R.id.txtDetailDescriptionHeader);
        TextView description = (TextView) view.findViewById(R.id.txtDetailDescription);
        this.mDescriptionDetailHandler = new SimpleDetailViewHandler(descriptionHeader, description);
        rate = (RatingBar) view.findViewById(R.id.ratingBar);
        this.mReloadProgressBarHandler = new ReloadProgressBarHandler((SmoothProgressBar) view.findViewById(R.id.loadingProgressBarSmooth));
        this.mReloadProgressBarHandler.show();
        this.mPlayTrailer = (ImageView) view.findViewById(R.id.iconPlayTrailers);
        this.mImageBackdrop = (ImageView) view.findViewById(R.id.imgBackdrop);
        this.mImgDetailCover =  (ImageView) view.findViewById(R.id.imgDetailCover);
        Typeface fontRobotoLight = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        setTypeFace(fontRobotoLight, description, release);
        Typeface fontRobotoItalic = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
        this.mTitle.setTypeface(fontRobotoItalic);

        fontRobotoLight = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Light.ttf");
        setTypeFace(fontRobotoLight, releaseHeader,descriptionHeader);
        this.mPlayTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trailer != null)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.trailer_url)));
            }
        });
        new GetMovies(movie_id).execute(new Void[0]);


     return view;
    }
    public static void setTypeFace(Typeface tf, TextView... views) {
        for (TextView view : views) {
            if (view != null) {
                view.setTypeface(tf);
            }
        }
    }
    protected void loadMovieDataIntoViews()
    {


        AnimationCreator animationCreator = new DelayedShiftAnimationCreator();
        this.mTitleViewHandler.update(movie);
        this.mReleaseDetailHandler.update(movie.released, movie.released, animationCreator);
        this.mDescriptionDetailHandler.update(movie.overview, movie.overview, animationCreator);
        Picasso.with(getActivity()).load(movie.defaultbackdropurl).into(mImgDetailCover);
        Picasso.with(getActivity()).load(movie.defaultposterurl).into(mImageBackdrop);
        rate.setRating(Float.parseFloat(movie.rating) / 2.0F);
        rate.setOnTouchListener(new touchListener());
        new GetTrailers(movie_id).execute(new Void[0]);

    }
    class touchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                DetailFragment.this.addRating();
            }
            return false;
        }
    }
    public void addRating() {
        Dialog rankDialog = new Dialog(getActivity(), R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);
        RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(0.0f);
        ((Button) rankDialog.findViewById(R.id.rank_dialog_button)).setOnClickListener(new ratingClick(ratingBar, rankDialog));
        rankDialog.show();
    }
    class ratingClick implements View.OnClickListener{
        Dialog rankDialog;
        RatingBar ratingBar;
        ratingClick(RatingBar ratingBar1, Dialog dialog) {
            ratingBar = ratingBar1;
            rankDialog = dialog;
        }

        @Override
        public void onClick(View v) {
            DetailFragment.this.rating = this.ratingBar.getRating() * 2.0F;
            new AddRating().execute(new Void[0]);
            rankDialog.dismiss();

        }
    }
    private class AddRating extends AsyncTask<Void, Void, Integer> {
        private AddRating() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (!JSONHelper.GuestSessionID.equals("") && JSONHelper.GuestSessionID.length() > 0) {
                DetailFragment.this.succes = JSONHelper.AddRating(DetailFragment.this.rating, DetailFragment.this.movie_id, DetailFragment.this.getActivity());
            }
            return Integer.valueOf(1);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (DetailFragment.this.succes) {
                Toast.makeText(DetailFragment.this.getActivity(), "Rating Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DetailFragment.this.getActivity(), "unexpected error occured.",Toast.LENGTH_SHORT).show();
            }
        }
    }


    protected void loadTrailer(){
     this.mReloadProgressBarHandler.hide();

    }

    private class GetMovies extends AsyncTask<Void, Void, Integer> {
        String movieid;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        private GetMovies(String movieId) {
            movieid = movieId;
        }

        protected Integer doInBackground(Void... params) {

           movie = JSONHelper.GetMovieById(getActivity(), movieid);
            return Integer.valueOf(1);
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            loadMovieDataIntoViews();


        }
    }
    private class GetTrailers extends AsyncTask<Void, Void, Integer> {
        String movieid;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        private GetTrailers(String movieId) {
            movieid = movieId;
        }

        protected Integer doInBackground(Void... params) {

            trailer = JSONHelper.GetTrailerById(getActivity(), movieid);
            return Integer.valueOf(1);
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            loadTrailer();

        }
    }
}
