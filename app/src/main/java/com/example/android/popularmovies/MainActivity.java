package com.example.android.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.android.popularmovies.adapter.DatabaseAdapter;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by modassirpc on 08-06-2016.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,itemSelected {

    NavigationView navigation_view;
    private DrawerLayout drawer_layout;
    private Toolbar toolBar;
    String category = "popular";
    boolean mTwoPane = false;
    private String guestSeesionID;
    private SharedPreferences prefs;


    private class GetGuestSession extends AsyncTask<Void, Void, Integer> {
        private GetGuestSession() {
        }

        protected Integer doInBackground(Void... params) {
            MainActivity.this.guestSeesionID = JSONHelper.GetGuestSessionID(MainActivity.this);
            return Integer.valueOf(1);
        }

        protected void onPostExecute(Integer result) {
            prefs.edit().putString("guestId",guestSeesionID).commit();


        }
    }



    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.navigation_view = ((NavigationView)findViewById(R.id.navigation_view));
        this.drawer_layout = ((DrawerLayout)findViewById(R.id.drawer_layout));
        this.toolBar = ((Toolbar)findViewById(R.id.toolBar));
        prefs = (SharedPreferences)PreferenceManager.getDefaultSharedPreferences(this);
        if(getIntent().getStringExtra("sort") != null)
            category = (String) getIntent().getStringExtra("sort");

        Log.e("Main",category);
        setSupportActionBar(this.toolBar);
        if(category.equals("popular")){
            setTitle("Popular Movies");
            navigation_view.setCheckedItem(R.id.drawer_popular);
        }
        else if(category.equals("top_rated")) {
            setTitle("Top Rated Movies");
            navigation_view.setCheckedItem(R.id.drawer_rated);
        }
        else if(category.equalsIgnoreCase("favourites")) {
            setTitle("Favourite Movies");
            navigation_view.setCheckedItem(R.id.drawer_favourites);
        }
        if(findViewById(R.id.detail) != null){
            mTwoPane = true;
            findViewById(R.id.detail).setVisibility(View.GONE);
        }
        else{
            mTwoPane = false;
        }
        new GetGuestSession().execute(new Void[0]);
        setUpDrawer();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(category,mTwoPane)).commitAllowingStateLoss();
        this.navigation_view.setNavigationItemSelectedListener(this);



    }
    private void setUpDrawer()
    {
        ActionBarDrawerToggle localActionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, this.drawer_layout, this.toolBar,R.string.drawerOpen, R.string.draweClose);
        this.drawer_layout.setDrawerListener(localActionBarDrawerToggle);
        localActionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.drawer_popular:
                Intent intent = getIntent();
                intent.putExtra("sort", "popular");
                finish();
                startActivity(intent);
                break;
            case R.id.drawer_rated:
                Intent intent1 = getIntent();
                item.setChecked(true);
                intent1.putExtra("sort", "top_rated");
                finish();
                startActivity(intent1);
                break;
            case R.id.drawer_favourites:
                Intent intent2 = getIntent();
                item.setChecked(true);
                intent2.putExtra("sort", "favourites");
                finish();
                startActivity(intent2);
                break;

        }
        return false;
    }

    @Override
    public void onItemSelected(String movie_id) {
        if(mTwoPane == true){

            findViewById(R.id.detail).setVisibility(View.VISIBLE);
            DetailFragment detailFragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString("movie_id", movie_id);
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail, detailFragment).commitAllowingStateLoss();

        }
        else {
            Intent intent = new Intent(this,DetailActivity.class);
            intent.putExtra("movie_id",movie_id);
            startActivity(intent);
        }


    }

    public static class PlaceholderFragment extends Fragment {
        private boolean loadingnewpage;
        private GoogleCardsAdapter mGoogleCardsAdapter;
        private ArrayList<Movie> moviesReceived;
        private int page;
        private boolean pageChanged;
        ProgressDialog pw;
        private GridView gridView;
        String query = "popular";
        Boolean twoPane = false;
        DatabaseAdapter mDatabaseAdapter;



        private class GetMovies extends AsyncTask<Void, Void, Integer> {
            String query;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            private GetMovies(String q) {
                query = q;
            }

            protected Integer doInBackground(Void... params) {

                PlaceholderFragment.this.moviesReceived = JSONHelper.GetMovies( query,PlaceholderFragment.this.getActivity());
                return Integer.valueOf(1);
            }

            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if (PlaceholderFragment.this.pageChanged) {
                    try {
                        ArrayList<String> listitems = new ArrayList();
                        ArrayList<Movie> listMovies = new ArrayList();
                        Iterator it;
                        Movie movie;
                        it = PlaceholderFragment.this.moviesReceived.iterator();
                        while (it.hasNext()) {
                            movie = (Movie) it.next();
                            listitems.add(movie.name);
                            listMovies.add(movie);
                        }

                        PlaceholderFragment.this.mGoogleCardsAdapter = new GoogleCardsAdapter(PlaceholderFragment.this.getActivity(),R.layout.activity_googlecards_cards, listMovies);
                        PlaceholderFragment.this.gridView.setClickable(true);
                        PlaceholderFragment.this.gridView.setAdapter(PlaceholderFragment.this.mGoogleCardsAdapter);
                        PlaceholderFragment.this.mGoogleCardsAdapter.notifyDataSetChanged();



                    } catch (Exception e) {
                    }
                }
                pw.dismiss();
            }
        }

        public static PlaceholderFragment newInstance(String cat,Boolean two) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString("query", cat);
            args.putBoolean("pane",two);
            fragment.setArguments(args);
            return fragment;
        }
        public PlaceholderFragment() {
            this.page = 1;
            this.loadingnewpage = false;
            this.pageChanged = true;
            this.moviesReceived = new ArrayList<Movie>();
        }
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            this.pageChanged = true;
            this.page = 1;
            this.loadingnewpage = true;
            query = getArguments().getString("query");

            twoPane = getArguments().getBoolean("pane");
            Log.e("Arguments",query);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            this.gridView = (GridView) rootView.findViewById(R.id.gridview);

            this.pw = new ProgressDialog(getActivity());
            pw.setMessage("Fetching Movie Info...");
            pw.setCancelable(false);
            if(query.equals("favourites")){
                mDatabaseAdapter = new DatabaseAdapter(getActivity());
                moviesReceived = mDatabaseAdapter.getFavouritesInfo();
                try {
                    ArrayList<String> listitems = new ArrayList();
                    ArrayList<Movie> listMovies = new ArrayList();
                    Iterator it;
                    Movie movie;
                    it = PlaceholderFragment.this.moviesReceived.iterator();
                    while (it.hasNext()) {
                        movie = (Movie) it.next();
                        listitems.add(movie.name);
                        listMovies.add(movie);
                    }

                    PlaceholderFragment.this.mGoogleCardsAdapter = new GoogleCardsAdapter(PlaceholderFragment.this.getActivity(),R.layout.activity_googlecards_cards, listMovies);
                    PlaceholderFragment.this.gridView.setClickable(true);
                    PlaceholderFragment.this.gridView.setAdapter(PlaceholderFragment.this.mGoogleCardsAdapter);
                    PlaceholderFragment.this.mGoogleCardsAdapter.notifyDataSetChanged();



                } catch (Exception e) {
                }


            }else {
                pw.show();

                new GetMovies(query).execute(new Void[0]);
            }
            return rootView;
        }

    }
}
