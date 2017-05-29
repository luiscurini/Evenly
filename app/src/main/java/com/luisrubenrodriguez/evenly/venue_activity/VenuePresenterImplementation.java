package com.luisrubenrodriguez.evenly.venue_activity;

import com.luisrubenrodriguez.evenly.model.VenuesSearchResponseInfo;
import com.luisrubenrodriguez.evenly.service.FoursquareService;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by GamingMonster on 24.05.2017.
 * Venue Detail Presenter
 */

class VenuePresenterImplementation implements FoursquareService.GetVenuesCallback,
        FoursquareService.LikeVenueCallback {

    //private static final String TAG = "VenuePresenterImplement";
    private final VenueView mVenueView;
    private final FoursquareService mFoursquareService;
    private CompositeSubscription mSubscription;

    VenuePresenterImplementation(VenueView venueView, FoursquareService foursquareService) {
        mVenueView = venueView;
        mFoursquareService = foursquareService;
        mSubscription = new CompositeSubscription();
    }

    /**
     * Calls the API method to get details of a venue and gets a subscription
     *
     * @param venue_id venue ID
     */
    void getVenue(String venue_id) {
        mVenueView.showLoading();
        Subscription subscription = mFoursquareService.getVenue(venue_id, this);
        mSubscription.add(subscription);
    }

    /**
     * Calls the API method to like a venue and get a subscription
     *
     * @param venue_id venue ID
     * @param setLike  0 = dislike. 1 = like
     */
    void likeVenue(String venue_id, Integer setLike) {
        Subscription subscription = mFoursquareService.likeVenue(venue_id, setLike, this);
        mSubscription.add(subscription);
    }


    /**
     * Get venue detail failure, shows error
     */
    @Override
    public void onDataFailure() {
        mVenueView.hideLoading();
        mVenueView.showError();
    }

    /**
     * Data retrieve from the API. It hides the loading animation and sends the data to the View.
     *
     * @param responseInfo JSON response transformed into an object
     */
    @Override
    public void onDataSuccess(VenuesSearchResponseInfo responseInfo) {
        if (responseInfo.getResponse() != null && responseInfo.getResponse().getVenue() != null) {
            mVenueView.hideLoading();
            mVenueView.loadData(responseInfo.getResponse().getVenue());
        }
    }

    /**
     * Something went wrong while liking the venue. Shows error
     */
    @Override
    public void onLikeFailure() {
        mVenueView.showLikeError();
    }

    /**
     * Data retrieved from the API, updates the like button or shows an error.
     *
     * @param responseInfo JSON response transformed into an object
     */
    @Override
    public void onLikeSuccess(VenuesSearchResponseInfo responseInfo) {
        //Here I'm assuming that if it's successful the like was process, data received from the
        //API is irrelevant unless the number of liked venues is tracked beforehand.
        if (responseInfo.getMeta().getCode() == 200) {
            mVenueView.updateLikeButton();
        } else {
            mVenueView.showLikeError();
        }
    }

    void unsubscribe() {
        mSubscription.unsubscribe();
    }
}
