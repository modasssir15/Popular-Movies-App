package com.example.android.popularmovies;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modassirpc on 08-06-2016.
 */
public class GoogleCardsAdapter extends ArrayAdapter<Movie>{
    private List<Movie> items;
    private Activity context;
    String movieid;
    float rating;
    boolean succes;
    Movie movie;
    int pos;

    private static class ViewHolder {
        TextView ReleaseDate;
        TextView idMovie;
        ImageView imageView;
        RatingBar rating;
        TextView textView;

        private ViewHolder() {
        }
    }
    class itemClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            String movie_id = (String) ((TextView) view.findViewById(R.id.idMovie)).getText();
            String movie_name = (String) ((TextView) view.findViewById(R.id.activity_googlecards_card_textview)).getText();
            ((MainActivity) context).onItemSelected(movie_id);

        }
    }

    public GoogleCardsAdapter(Activity context, int i, ArrayList<Movie> elements) {
        super(context, i, elements);
        this.context = context;
        this.movieid = "";
        this.rating = 0.0f;
        this.succes = false;
        this.items = elements;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        movie = (Movie) this.items.get(position);
        pos = position;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_googlecards_cards, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.activity_googlecards_card_textview);
            view.setTag(viewHolder);
            viewHolder.idMovie = (TextView) view.findViewById(R.id.idMovie);
            view.setTag(viewHolder);

            viewHolder.ReleaseDate = (TextView) view.findViewById(R.id.ReleaseDate);
            view.setTag(viewHolder);
            viewHolder.rating = (RatingBar) view.findViewById(R.id.ratingBar);
            view.setTag(viewHolder);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.activity_googlecards_card_imageview);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Typeface tfb = Typeface.createFromAsset(this.context.getAssets(), "bold.otf");
        Typeface tf = Typeface.createFromAsset(this.context.getAssets(), "font.otf");
        viewHolder.textView.setText(movie.name);
        viewHolder.textView.setTypeface(tfb);
        viewHolder.idMovie.setText(movie.id);
        viewHolder.imageView.setTag(movie.defaultbackdropurl);
        viewHolder.ReleaseDate.setText(new StringBuilder(movie.released).toString());
        viewHolder.ReleaseDate.setTypeface(tf);
        try {
            viewHolder.rating.setRating(Float.parseFloat(movie.rating) / 2.0F);
            viewHolder.rating.setTag(movie.id);
            viewHolder.rating.setOnTouchListener(new touchListener());
        } catch (Exception e) {
        }
        Picasso.with(this.context).load(movie.defaultbackdropurl).into(viewHolder.imageView);
        view.setOnClickListener(new itemClick());

        return view;
    }
    class touchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                GoogleCardsAdapter.this.addRating(v.getTag().toString());
            }
            return false;
        }
    }
    public void addRating(String mov) {
        Dialog rankDialog = new Dialog(this.context, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        this.movieid = mov;
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
            GoogleCardsAdapter.this.rating = this.ratingBar.getRating() * 2.0F;
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
                GoogleCardsAdapter.this.succes = JSONHelper.AddRating(GoogleCardsAdapter.this.rating, GoogleCardsAdapter.this.movieid, GoogleCardsAdapter.this.context);
            }
            return Integer.valueOf(1);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (GoogleCardsAdapter.this.succes) {
                Toast.makeText(GoogleCardsAdapter.this.context, "Rating Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GoogleCardsAdapter.this.context, "unexpected error occured.",Toast.LENGTH_SHORT).show();
            }
        }
    }






}
