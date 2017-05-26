package com.luisrubenrodriguez.evenly.venue_activity;

import android.util.Log;

import com.luisrubenrodriguez.evenly.model.VenuesSearchResponseInfo;
import com.luisrubenrodriguez.evenly.service.FoursquareService;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by GamingMonster on 24.05.2017.
 */

public class VenuePresenterImplementation implements FoursquareService.GetVenuesCallback,
        FoursquareService.LikeVenueCallback {

    private static final String TAG = "VenuePresenterImplement";
    private final VenueView mVenueView;
    private final FoursquareService mFoursquareService;
    private CompositeSubscription mSubscription;

    public VenuePresenterImplementation(VenueView venueView, FoursquareService foursquareService) {
        mVenueView = venueView;
        mFoursquareService = foursquareService;
        mSubscription = new CompositeSubscription();
    }

    public void getVenue(String venue_id) {
        mVenueView.showLoading();
        Subscription subscription = mFoursquareService.getVenue(venue_id, this);
        mSubscription.add(subscription);
    }

    public void likeVenue(String venue_id, Integer setLike) {
        Subscription subscription = mFoursquareService.likeVenue(venue_id, setLike, this);
        mSubscription.add(subscription);
    }

    @Override
    public void onDataFailure() {
        mVenueView.hideLoading();
        mVenueView.showError();
        Log.d(TAG, "onDataFailure: ");
    }

    @Override
    public void onDataSuccess(VenuesSearchResponseInfo responseInfo) {
        Log.d(TAG, "onDataSuccess: ");
        if (responseInfo.getResponse() != null && responseInfo.getResponse().getVenue() != null) {
            mVenueView.hideLoading();
            mVenueView.loadData(responseInfo.getResponse().getVenue());
        }
    }

    @Override
    public void onLikeFailure() {
        mVenueView.showLikeError();
    }

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

    public void unsubscribe() {
        mSubscription.unsubscribe();
    }
}
