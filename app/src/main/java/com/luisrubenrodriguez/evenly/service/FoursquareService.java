package com.luisrubenrodriguez.evenly.service;

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

    /**
     * Callback interface for the list of venues and venue details
     */
    public interface GetVenuesCallback {
        void onDataFailure();

        void onDataSuccess(VenuesSearchResponseInfo responseInfo);
    }

    /**
     * Callback interface for liking a venue.
     */
    public interface LikeVenueCallback {
        void onLikeFailure();

        void onLikeSuccess(VenuesSearchResponseInfo responseInfo);
    }

    //private static final String TAG = "FoursquareService";
    private final FoursquareAPI mFoursquareAPI;

    public FoursquareService(FoursquareAPI foursquareAPI) {
        mFoursquareAPI = foursquareAPI;
    }

    /**
     * Get the list of nearbyVenues
     *
     * @param location String with the location
     * @param callback to be called on Failure or Success.
     * @return RX Subscription
     */
    public Subscription getNearbyVenues(String location, final GetVenuesCallback callback) {
        return mFoursquareAPI.getNearbyVenues(location, FoursquareAPI.OAUTHTOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber(callback));
    }

    /**
     * Gets the details of a venue.
     *
     * @param venue_id Id of the venue
     * @param callback to be called on Failure or Sucess
     * @return RxSubscription
     */
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
                // e.printStackTrace();
                callback.onDataFailure();
            }

            @Override
            public void onNext(VenuesSearchResponseInfo responseInfo) {
                callback.onDataSuccess(responseInfo);
            }
        };
    }

    /**
     * Likes a venue
     *
     * @param venue_id Id of the venue
     * @param setLike  set like to 1 = like or 0= unlike
     * @param callback to be called on Failure or sucess
     * @return Rx Subscription
     */
    public Subscription likeVenue(String venue_id, Integer setLike, final LikeVenueCallback callback) {
        return mFoursquareAPI.likeVenue(venue_id, setLike, FoursquareAPI.OAUTHTOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VenuesSearchResponseInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //e.printStackTrace();
                        callback.onLikeFailure();
                    }

                    @Override
                    public void onNext(VenuesSearchResponseInfo responseInfo) {
                        callback.onLikeSuccess(responseInfo);
                    }
                });
    }
}
