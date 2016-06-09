package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modassirpc on 08-06-2016.
 */
public class GoogleCardsAdapter extends ArrayAdapter<Movie>{
    private List<Movie> items;
    private Context context;
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
            Intent intent = new Intent(getContext(),DetailActivity.class);
            intent.putExtra("movie_id",movie_id);
            getContext().startActivity(intent);
        }
    }

    public GoogleCardsAdapter(Context context, int i, ArrayList<Movie> elements) {
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
        } catch (Exception e) {
        }
        Picasso.with(this.context).load(movie.defaultbackdropurl).into(viewHolder.imageView);
        view.setOnClickListener(new itemClick());

        return view;
    }






}
