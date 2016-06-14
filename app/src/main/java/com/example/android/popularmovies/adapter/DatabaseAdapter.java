package com.example.android.popularmovies.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.Movie;

import java.util.ArrayList;

/**
 * Created by modassirpc on 11-06-2016.
 */
public class DatabaseAdapter {
    private final DatabaseHelper mDatabaseHelper;
    private final SQLiteDatabase mdb;

    public DatabaseAdapter(Context paramContext) {
        this.mDatabaseHelper = new DatabaseHelper(paramContext);
        this.mdb = this.mDatabaseHelper.getWritableDatabase();
    }
    public long insertInToFavouritesInfo(String paramString1, String paramString2, String paramString3,String paramString4,String paramString5,String paramString6,String paramString7,String paramString8)
    {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("movieId", paramString1);
        localContentValues.put("movieName", paramString2);
        localContentValues.put("movieOverview", paramString3);
        localContentValues.put("movieRelease", paramString4);
        localContentValues.put("movieRating", paramString5);
        localContentValues.put("movieVotes", paramString6);
        localContentValues.put("backdropUrl", paramString7);
        localContentValues.put("posterUrl", paramString8);

        return this.mdb.insert("favourites", null, localContentValues);
    }

    public ArrayList<Movie> getFavouritesInfo() {
        ArrayList<Movie> localObject1 = new ArrayList<Movie>();

        try {
            String[] arrayOfString = {"movieId", "movieName", "movieOverview","movieRelease","movieRating","movieVotes","backdropUrl","posterUrl"};
            Cursor localCursor = this.mdb.query("favourites", arrayOfString, null, null, null, null, null);


            while (localCursor.moveToNext()) {
               Movie localObject2 = new Movie();
                ((Movie) localObject2).id = (localCursor.getString(0));
                ((Movie) localObject2).name = (localCursor.getString(1));
                ((Movie) localObject2).overview = (localCursor.getString(2));
                ((Movie) localObject2).released = (localCursor.getString(3));
                ((Movie) localObject2).rating = (localCursor.getString(4));
                ((Movie) localObject2).votes_total = (localCursor.getString(5));
                ((Movie) localObject2).defaultbackdropurl = (localCursor.getString(6));
                ((Movie) localObject2).defaultposterurl = (localCursor.getString(7));
                localObject1.add(localObject2);

            }
            return localObject1;
        } catch (Exception e) {
        }
        return localObject1 ;
    }
    public static class DatabaseHelper
            extends SQLiteOpenHelper
    {

        public DatabaseHelper(Context paramContext)
        {
            super(paramContext,"wrapDatabase", null, 2);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE favourites(movieId TEXT PRIMARY KEY,movieName TEXT,movieOverview TEXT,movieRelease TEXT,movieRating TEXT,movieVotes TEXT,backdropUrl TEXT,posterUrl TEXT);");



        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("CREATE TABLE favourites(movieId TEXT PRIMARY KEY,movieName TEXT,movieOverview TEXT,movieRelease TEXT,movieRating TEXT,movieVotes TEXT,backdropUrl TEXT,posterUrl TEXT);");

        }
    }
}
