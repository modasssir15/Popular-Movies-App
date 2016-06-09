package com.example.android.popularmovies;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by modassirpc on 08-06-2016.
 */
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance()).commitAllowingStateLoss();



    }
    public static class PlaceholderFragment extends Fragment {
        private boolean loadingnewpage;
        private GoogleCardsAdapter mGoogleCardsAdapter;
        private ArrayList<Movie> moviesReceived;
        private int page;
        private boolean pageChanged;
        ProgressDialog pw;
        private GridView gridView;



        private class GetMovies extends AsyncTask<Void, Void, Integer> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            private GetMovies() {
            }

            protected Integer doInBackground(Void... params) {

                PlaceholderFragment.this.moviesReceived = JSONHelper.GetMovies( PlaceholderFragment.this.getActivity());
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

        public static PlaceholderFragment newInstance() {
            PlaceholderFragment fragment = new PlaceholderFragment();
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            this.gridView = (GridView) rootView.findViewById(R.id.gridview);
            this.pw = new ProgressDialog(getActivity());
            pw.setMessage("Fetching Movie Info...");
            pw.setCancelable(false);
            pw.show();
            new GetMovies().execute(new Void[0]);
            return rootView;
        }

    }
}
