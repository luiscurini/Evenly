package com.luisrubenrodriguez.evenly.service;

import android.util.Log;

import com.luisrubenrodriguez.evenly.model.VenuesSearchResponseInfo;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by GamingMonster on 22.05.2017.
 * Service create subscriptions and call the API.
 */

public class FoursquareService {

    public interface GetVenuesCallback {
        void onDataFailure();
        void onDataSuccess(VenuesSearchResponseInfo responseInfo);
    }

    public interface LikeVenueCallback {
        void onLikeFailure();
        void onLikeSuccess(VenuesSearchResponseInfo responseInfo);
    }

    private static final String TAG = "FoursquareService";
    private final FoursquareAPI mFoursquareAPI;

    public FoursquareService(FoursquareAPI foursquareAPI) {
        mFoursquareAPI = foursquareAPI;
    }

    public Subscription getNearbyVenues(String location, final GetVenuesCallback callback) {
        return mFoursquareAPI.getNearbyVenues(location, FoursquareAPI.OAUTHTOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber(callback));
    }

    public Subscription getVenue(String venue_id, GetVenuesCallback callback) {
        return mFoursquareAPI.getVenue(venue_id, FoursquareAPI.OAUTHTOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber(callback));
    }

    private Subscriber<VenuesSearchResponseInfo> getSubscriber(final GetVenuesCallback callback) {
        return new Subscriber<VenuesSearchResponseInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                callback.onDataFailure();
            }

            @Override
            public void onNext(VenuesSearchResponseInfo responseInfo) {
                Log.d(TAG, "onNext: Success");
                callback.onDataSuccess(responseInfo);
            }
        };
    }

    public Subscription likeVenue(String venue_id, Integer setLike, final LikeVenueCallback callback) {
        return mFoursquareAPI.likeVenue(venue_id, setLike, FoursquareAPI.OAUTHTOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VenuesSearchResponseInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "onError: ");
                        callback.onLikeFailure();
                    }

                    @Override
                    public void onNext(VenuesSearchResponseInfo responseInfo) {
                        Log.d(TAG, "onNext: ");
                        callback.onLikeSuccess(responseInfo);
                    }
                });
    }
}
