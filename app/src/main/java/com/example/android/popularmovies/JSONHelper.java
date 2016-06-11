package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
    public static String baseImage_url =  "http://cf2.imgobject.com/t/p/";
    public static String sizeBackdrop_small = "w300";
    public static String sizePoster_small = "w154";
    public static String GuestSessionID;

    public static String makeGetRequest(String path) throws JSONException, ClientProtocolException, Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httget = new HttpGet(path);
        httget.setHeader("Accept", "application/json");
        httget.setHeader("Content-type", "application/json");
        return (String) httpclient.execute(httget, new BasicResponseHandler());
    }
    public static String GetGuestSessionID(Context paramContext)
    {

        try {
            JSONObject localJSONObject = new JSONObject(makeGetRequest("http://api.themoviedb.org/3/authentication/guest_session/new"+"?api_key=" + paramContext.getString(R.string.api_key)));
            GuestSessionID = localJSONObject.getString("guest_session_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GuestSessionID;
    }

    public static boolean AddRating(float paramFloat, String movieid, Context context)
    {
        JSONObject localJSONObject1 = new JSONObject();
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            String guestId = pref.getString("guestId","0");
            localJSONObject1.put("value", paramFloat);
            JSONObject localJSONObject2 = makePostRequest("http://api.themoviedb.org/3/movie/"+movieid+"/rating"+ "?api_key=" + context.getString(R.string.api_key) + "&guest_session_id=" + guestId, localJSONObject1);
           return true;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    public static JSONObject makePostRequest(String path, JSONObject params) throws ClientProtocolException, JSONException, Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(path);
        httpost.setEntity(new StringEntity(params.toString()));
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");
        return new JSONObject((String) httpclient.execute(httpost, new BasicResponseHandler()));
    }

    public static ArrayList<Movie> GetMovies(String query,Context paramContext)
    {
        ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
        try {

            JSONArray localJSONArray = new JSONObject(makeGetRequest("http://api.themoviedb.org/3/movie/"+query + "?api_key=" + paramContext.getString(R.string.api_key))).getJSONArray("results");
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
                movieArrayList.add(localMovie);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

      return movieArrayList;
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
    public static Trailer GetTrailerById(Context paramContext,String movieId){
        Trailer localTrailer = null;

        try {
            JSONArray localJSONArray = new JSONObject(makeGetRequest("http://api.themoviedb.org/3/movie/"+movieId +"/videos"+ "?api_key=" + paramContext.getString(R.string.api_key))).getJSONArray("results");
            for(int i =0;i<localJSONArray.length();i++) {
                JSONObject localJSONObject = localJSONArray.getJSONObject(i);
                if(localJSONObject.getString("site").equalsIgnoreCase("YouTube")) {
                    localTrailer = new Trailer();

                    localTrailer.movieId = movieId;
                    localTrailer.name = localJSONObject.getString("name");
                    localTrailer.trailer_url = "https://www.youtube.com/watch?v="+localJSONObject.getString("key");
                    return localTrailer;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  localTrailer;

    }
}
