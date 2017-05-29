package com.luisrubenrodriguez.evenly.venue_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.luisrubenrodriguez.evenly.BaseApp;
import com.luisrubenrodriguez.evenly.R;
import com.luisrubenrodriguez.evenly.adapter.ImageGalleryAdapter;
import com.luisrubenrodriguez.evenly.main_activity.MainActivity;
import com.luisrubenrodriguez.evenly.model.Venue;
import com.luisrubenrodriguez.evenly.model.VenueGroup;
import com.luisrubenrodriguez.evenly.model.VenueGroupItem;
import com.luisrubenrodriguez.evenly.model.VenueLocation;
import com.luisrubenrodriguez.evenly.service.FoursquareService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * This activity shows the Venue details. A request to the API is needed because it contains extra information
 * that it's not available when searching for nearby venues. For example if the user has liked or not
 * the venue.
 */
public class VenueActivity extends BaseApp implements VenueView {

    private static final String TAG = "VenueActivity";
    public static final int DISLIKE = 0;
    public static final int LIKE = 1;

    @BindView(R.id.venuedetail_swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.venuedetail_name)
    TextView mVenueName;
    @BindView(R.id.venuedetail_category)
    TextView mVenueCategory;
    @BindView(R.id.venuedetail_address)
    TextView mVenueAddress;
    @BindView(R.id.venuedetail_phone)
    TextView mVenuePhone;
    @BindView(R.id.venuedetail_likebutton)
    Button mVenueLikeButton;
    @BindView(R.id.venuedetail_directionbutton)
    Button mVenueDirectionButton;
    @BindView(R.id.venuedetail_layout)
    View mViewGroup;
    @BindView(R.id.imagePager)
    ViewPager mVenueGalleryPager;
    @BindView(R.id.indicator)
    CircleIndicator mCircleIndicator;
    @BindView(R.id.venuedetail_scrollview)
    ScrollView mScrollView;

    private Venue mVenue;
    private String mVenueId;

    @Inject
    public FoursquareService mFoursquareService;
    private VenuePresenterImplementation mVenuePresenterImplementation;

    //TODO save instance to avoid calling the API when rotation happens for example.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);
        ButterKnife.bind(this);

        getDependencies().inject(this);
        Intent intent = getIntent();
        mVenueId = intent.getStringExtra(MainActivity.VENUE_ID);

        mVenuePresenterImplementation = new VenuePresenterImplementation(this, mFoursquareService);
        mVenuePresenterImplementation.getVenue(mVenueId);

        initViewPager();
        initDirectionButton();
        initLikeButton();
        initSwipeRefresh();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mVenuePresenterImplementation.unsubscribe();
    }

    /**
     * Initializes the ViewPager that will contain the images. As it's inside a ScrollView
     * once a touch event inside of the ViewPager happens it will disallow the touch event on the
     * scrollView.
     */
    private void initViewPager() {
        //I want the images to occupy half of the available space.
        Integer displayWidth = getResources().getDisplayMetrics().heightPixels;
        mVenueGalleryPager.getLayoutParams().height = displayWidth / 2;

        //This is needed otherwise the image scrolling is a bit weird. This might need some tweaking.
        mVenueGalleryPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE && mScrollView != null) {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;

            }
        });
    }

    /**
     * Initializes the Swipe REfresh Layout
     */
    private void initSwipeRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mVenuePresenterImplementation.getVenue(mVenueId);
            }
        });
    }

    /**
     * When the button is click it will make a request to the API and like/dislike the venue.
     */
    private void initLikeButton() {
        mVenueLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: like button");
                mVenueLikeButton.setClickable(false);
                Integer setLike = LIKE;
                if (mVenue.getLike()) setLike = DISLIKE;
                mVenuePresenterImplementation.likeVenue(mVenue.getId(), setLike);
            }
        });
    }

    /**
     * If the coordinates received from the API are not empty, this button will start Google Maps
     * if installed, or the browser redirecting to google maps and it will show the direction between
     * the set coordinates and the venue.
     */
    private void initDirectionButton() {
        mVenueDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VenueLocation location = mVenue.getLocation();
                if (location != null && location.getLat() != null && location.getLng() != null) {
                    String url = "https://www.google.com/maps/dir/?api=1&origin=52.500365,13.425170&destination="
                            + location.getLat().toString() + "," + location.getLng().toString() + "&travelmode=walking";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    showCoordinatesError();
                }
            }
        });
    }


    /**
     * Shows loading animation of the SwipeRefreshLayout
     */
    @Override
    public void showLoading() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    /**
     * Hides loading animation of the SwipeRefreshLayout
     */
    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Get venue data from the API, shows the data in the widgets.
     *
     * @param venue data receive
     */
    @Override
    public void loadData(Venue venue) {
        mVenue = venue;
        mViewGroup.setVisibility(View.VISIBLE);
        mVenueName.setText(mVenue.getName());

        //Todo make this nicer, getter methods.
        if (mVenue.getLocation().getAddress() != null) {
            VenueLocation location = mVenue.getLocation();
            String address = location.getFormattedAddress();
            mVenueAddress.setText(address);
        } else {
            mVenueAddress.setText(getString(R.string.empty_address));
        }

        if (mVenue.getCategories() != null && mVenue.getCategories().size() > 0) {
            mVenueCategory.setText(mVenue.getCategories().get(0).getName());
        } else {
            mVenueCategory.setText(R.string.empty_category);
        }

        if (mVenue.getContact() != null && mVenue.getContact().getFormattedPhone() != null) {
            mVenuePhone.setText(mVenue.getContact().getFormattedPhone());
        } else {
            mVenuePhone.setVisibility(View.INVISIBLE);
        }

        if (mVenue.getLike() != null && mVenue.getLike()) {
            mVenueLikeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_yellow_24dp, 0, 0, 0);
            mVenueLikeButton.setText(R.string.dislike_button);
        }

        List<String> imagesUrl = new ArrayList<>();


        //TODO move this to a getter in the class.
        try {
            VenueGroup photoGroup = mVenue.getPhotos().getGroups().get(0);
            List<VenueGroupItem> photoGroupItem = photoGroup.getItems();

            for (VenueGroupItem item : photoGroupItem) {
                String prefix = item.getPrefix();
                String suffix = item.getSuffix();
                String url = prefix + "width960" + suffix;
                imagesUrl.add(url);
            }

        } catch (Exception e) {
            Log.d(TAG, "loadData: no pictures");
        }

        ImageGalleryAdapter galleryAdapter = new ImageGalleryAdapter(this, imagesUrl);
        mVenueGalleryPager.setAdapter(galleryAdapter);
        mCircleIndicator.setViewPager(mVenueGalleryPager);
    }

    /**
     * Something went wrong while retrieving data from the API, shows error message.
     */
    @Override
    public void showError() {
        Toast.makeText(this, R.string.venueactivity_error, Toast.LENGTH_LONG).show();
    }

    /**
     * Something went wrong when trying to get the venue coordinates.
     */
    void showCoordinatesError() {
        Toast.makeText(this, R.string.venueactivity_coordinateerror, Toast.LENGTH_LONG).show();
    }

    /**
     * Problem liking the venue
     */
    @Override
    public void showLikeError() {
        mVenueLikeButton.setClickable(true);
        Toast.makeText(this, "The was a problem linking/disliking the venue, try again later", Toast.LENGTH_LONG).show();
    }

    /**
     * If the like request was successful update the button
     */
    @Override
    public void updateLikeButton() {
        mVenueLikeButton.setClickable(true);
        if (mVenue.getLike()) {
            mVenueLikeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_outline_24dp, 0, 0, 0);
            mVenueLikeButton.setText(R.string.like_button);
            mVenue.setLike(false);
        } else {
            mVenueLikeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_yellow_24dp, 0, 0, 0);
            mVenueLikeButton.setText(R.string.dislike_button);
            mVenue.setLike(true);
        }
    }
}
