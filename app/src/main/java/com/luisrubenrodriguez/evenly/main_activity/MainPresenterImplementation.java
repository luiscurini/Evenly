package com.luisrubenrodriguez.evenly.main_activity;

import com.luisrubenrodriguez.evenly.model.Venue;
import com.luisrubenrodriguez.evenly.model.VenuesSearchResponse;
import com.luisrubenrodriguez.evenly.model.VenuesSearchResponseInfo;
import com.luisrubenrodriguez.evenly.service.FoursquareService;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by GamingMonster on 22.05.2017.
 * Main Presenter Implementation, following MVP architecture;
 */

public class MainPresenterImplementation implements FoursquareService.GetVenuesCallback {

    private static final String TAG = "MainPresenterImplementa";
    private final MainView mMainView;
    private final FoursquareService mFoursquareService;
    private CompositeSubscription mSubscription;

    public MainPresenterImplementation(MainView mainView, FoursquareService foursquareService) {
        mMainView = mainView;
        mFoursquareService = foursquareService;
        mSubscription = new CompositeSubscription();
    }

    /**
     * Calls the APi to get the Venues near the specified location
     * @param location coordinates in String (e.g '52.2,14.1')
     */
    public void getNearByVenues(String location) {
        mMainView.showLoading();
        Subscription subscription = mFoursquareService.getNearbyVenues(location, this);
        mSubscription.add(subscription);
    }

    /**
     * Something went wrong with the api call, no internet, service unavailable.
     * It shows the error message.
     */
    @Override
    public void onDataFailure() {
        mMainView.hideLoading();
        mMainView.showConnectionError();
    }

    /**
     * In case that the API call is successful, it will pass the list of venues to the View.
     * @param responseInfo Json response converted object.
     */
    @Override
    public void onDataSuccess(VenuesSearchResponseInfo responseInfo) {
        mMainView.hideLoading();

        VenuesSearchResponse response = responseInfo.getResponse();
        if (response != null) {
            List<Venue> venues = response.getVenues();
            if (venues != null && venues.size() > 0) {
                mMainView.loadNearByVenues(venues);
            }
        } else {
            mMainView.showConnectionError();
        }

    }

    public void unsubscribe() {
        mSubscription.unsubscribe();
    }
}
