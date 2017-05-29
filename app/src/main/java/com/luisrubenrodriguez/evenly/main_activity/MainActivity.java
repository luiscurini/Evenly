package com.luisrubenrodriguez.evenly.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.luisrubenrodriguez.evenly.BaseApp;
import com.luisrubenrodriguez.evenly.R;
import com.luisrubenrodriguez.evenly.adapter.VenueRecyclerViewAdapter;
import com.luisrubenrodriguez.evenly.model.Venue;
import com.luisrubenrodriguez.evenly.service.FoursquareAPI;
import com.luisrubenrodriguez.evenly.service.FoursquareService;
import com.luisrubenrodriguez.evenly.venue_activity.VenueActivity;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseApp implements MainView, VenueRecyclerViewAdapter.OnVenueItemClickListener {

    //private static final String TAG = "MainActivity";
    public static final String VENUE_ID = "venue_id";
    public static final String VENUE_DATA = "venue_data";
    public static final String LAYOUT_DATA = "layout_data";

    @Inject
    public FoursquareService mFoursquareService;
    private MainPresenterImplementation mMainPresenter;

    @BindView(R.id.venue_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.mainactivity_swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mRecyclerViewLayoutManager;
    private VenueRecyclerViewAdapter mVenueRecyclerViewAdapter;

    //To restore state
    private List<Venue> mVenueList;
    private Parcelable mState;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_favorites:
                    //TODO implement favorites list.
                    Toast.makeText(MainActivity.this, "Favorite list not yet implemented", Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDependencies().inject(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initRecyclerView();
        initSwipeRefresh();
        mMainPresenter = new MainPresenterImplementation(this, mFoursquareService);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainPresenter.unsubscribe();
    }

    /**
     * Setup of the SwipeRefreshLayout.
     */
    private void initSwipeRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMainPresenter.getNearByVenues(FoursquareAPI.SEARCH_LOCATION);
            }
        });
    }

    /**
     * Setup of the recyclerView it linearLayoutManager, itemDecoration and adapter
     */
    private void initRecyclerView() {
        mRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mVenueRecyclerViewAdapter = new VenueRecyclerViewAdapter(this);
        mVenueRecyclerViewAdapter.setOnVenueItemClickListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, mRecyclerViewLayoutManager.getOrientation());
        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mVenueRecyclerViewAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mVenueList != null) {
            //Saving list with venues and RecyclerView's layoutmanager state.
            Parcelable state = mRecyclerViewLayoutManager.onSaveInstanceState();
            outState.putParcelable(LAYOUT_DATA, state);
            outState.putSerializable(VENUE_DATA, (Serializable) mVenueList);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mVenueList = (List<Venue>) savedInstanceState.getSerializable(VENUE_DATA);
            mState = savedInstanceState.getParcelable(LAYOUT_DATA);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == mVenueList) {
            mMainPresenter.getNearByVenues(FoursquareAPI.SEARCH_LOCATION);
        } else {
            mVenueRecyclerViewAdapter.loadNewData(mVenueList);
            if (mState != null) {
                mRecyclerViewLayoutManager.onRestoreInstanceState(mState);
            }
            mState = null;
        }
    }

    /**
     * Shows loading animation
     */
    @Override
    public void showLoading() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    /**
     * Hides loading animation
     */
    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Something when wrong while retrieving data from the API
     */
    @Override
    public void showConnectionError() {
        Toast.makeText(this, R.string.mainactivity_error, Toast.LENGTH_LONG).show();
    }

    /**
     * Takes the data received from the API call and informs the recyclerview adapter of the change.
     *
     * @param venues list of venues
     */
    @Override
    public void loadNearByVenues(List<Venue> venues) {
        //Log.d(TAG, "loadNearByVenues: new venues");
        mVenueList = venues;
        mVenueRecyclerViewAdapter.loadNewData(mVenueList);
    }

    /**
     * Starts a new VenueActivity and passes the venue_id
     *
     * @param venue_id Id of the venue
     */
    @Override
    public void onItemClick(String venue_id) {
        //Log.d(TAG, "onItemClick: starting activity for id: " + venue_id);
        Intent intent = new Intent(this, VenueActivity.class);
        intent.putExtra(VENUE_ID, venue_id);
        startActivity(intent);
    }
}
