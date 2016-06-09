package com.example.android.popularmovies;

import android.content.Context;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by modassirpc on 08-06-2016.
 */
public class JSONHelper {
    public static ArrayList<Movie> PopularList = new ArrayList<Movie>();
    public static String baseImage_url =  "http://cf2.imgobject.com/t/p/";
    public static String sizeBackdrop_small = "w300";
    public static String sizePoster_small = "w154";

    public static String makeGetRequest(String path) throws JSONException, ClientProtocolException, Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httget = new HttpGet(path);
        httget.setHeader("Accept", "application/json");
        httget.setHeader("Content-type", "application/json");
        return (String) httpclient.execute(httget, new BasicResponseHandler());
    }

    public static ArrayList<Movie> GetMovies(Context paramContext)
    {
        try {
            JSONArray localJSONArray = new JSONObject(makeGetRequest("http://api.themoviedb.org/3/movie/top_rated" + "?api_key=" + paramContext.getString(R.string.api_key))).getJSONArray("results");
            for(int i =0;i<localJSONArray.length();i++){
                JSONObject localJSONObject = localJSONArray.getJSONObject(i);
                Movie localMovie = new Movie();
                localMovie.id = localJSONObject.getString("id");
                localMovie.name = localJSONObject.getString("title");
                localMovie.rating = localJSONObject.getString("vote_average");
                localMovie.released = localJSONObject.getString("release_date");
                localMovie.votes_total = localJSONObject.getString("vote_count");
                localMovie.overview = localJSONObject.getString("overview");
                localMovie.defaultposterurl = (baseImage_url + sizePoster_small + localJSONObject.getString("poster_path"));
                localMovie.defaultbackdropurl = (baseImage_url + sizeBackdrop_small + localJSONObject.getString("backdrop_path"));
                PopularList.add(localMovie);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

      return PopularList;
    }
    public static Movie GetMovieById(Context paramContext,String movieId){
        Movie localMovie = null;

        try {
            JSONObject localJSONObject = new JSONObject(makeGetRequest("http://api.themoviedb.org/3/movie/"+movieId + "?api_key=" + paramContext.getString(R.string.api_key)));
            localMovie = new Movie();
            localMovie.id = localJSONObject.getString("id");
            localMovie.name = localJSONObject.getString("title");
            localMovie.rating = localJSONObject.getString("vote_average");
            localMovie.released = localJSONObject.getString("release_date");
            localMovie.votes_total = localJSONObject.getString("vote_count");
            localMovie.overview = localJSONObject.getString("overview");
            localMovie.defaultposterurl = (baseImage_url + sizePoster_small + localJSONObject.getString("poster_path"));
            localMovie.defaultbackdropurl = (baseImage_url + sizeBackdrop_small + localJSONObject.getString("backdrop_path"));
            return localMovie;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  localMovie;

    }
}
