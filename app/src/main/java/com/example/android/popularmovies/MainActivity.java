package com.example.android.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by modassirpc on 08-06-2016.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigation_view;
    private DrawerLayout drawer_layout;
    private Toolbar toolBar;
    String category = "popular";



    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.navigation_view = ((NavigationView)findViewById(R.id.navigation_view));
        this.drawer_layout = ((DrawerLayout)findViewById(R.id.drawer_layout));
        this.toolBar = ((Toolbar)findViewById(R.id.toolBar));
        if(getIntent().getStringExtra("sort") != null)
            category = (String) getIntent().getStringExtra("sort");

        Log.e("Main",category);
        setSupportActionBar(this.toolBar);
        if(category.equals("popular")){
            setTitle("Popular Movies");
        }
        else if(category.equals("top_rated"))
            setTitle("Top Rated Movies");
        setUpDrawer();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(category)).commitAllowingStateLoss();
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
                intent1.putExtra("sort", "top_rated");
                finish();
                startActivity(intent1);
                break;

        }
        return false;
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

        public static PlaceholderFragment newInstance(String cat) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString("query", cat);
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
            Log.e("Arguments",query);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            this.gridView = (GridView) rootView.findViewById(R.id.gridview);
            this.pw = new ProgressDialog(getActivity());
            pw.setMessage("Fetching Movie Info...");
            pw.setCancelable(false);
            pw.show();
            new GetMovies(query).execute(new Void[0]);
            return rootView;
        }

    }
}
