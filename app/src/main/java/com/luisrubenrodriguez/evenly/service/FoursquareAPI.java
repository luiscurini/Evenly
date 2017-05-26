package com.luisrubenrodriguez.evenly.service;

import com.luisrubenrodriguez.evenly.model.VenuesSearchResponseInfo;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by GamingMonster on 22.05.2017.
 * Retrofit Interface. It will call the FoursquareAPI to get the list of nearby venues.
 */

public interface FoursquareAPI {
    String BASE_URL = "https://api.foursquare.com/";
    String OAUTHTOKEN = "4TQA0SSHJBDSSX5IEYMG3HHGTPV021EK4P3K01BRWCE2RMYK";
    String SEARCH_LOCATION = "52.500342,13.425170";

    //TODO avoid hardcoded query parameters, move the versioning.
    String SEARCHVENUESPATH = "v2/venues/search?&v=20170522&m=foursquare&limit=50";
    String GETVENUEPATH = "v2/venues/{venue_id}?&v=20170522&m=foursquare";
    String LIKEVENUEPATH = "v2/venues/{venue_id}/like?v=20170522&m=foursquare";


    @GET(SEARCHVENUESPATH)
    Observable<VenuesSearchResponseInfo> getNearbyVenues(@Query("ll") String location, @Query("oauth_token") String oauth_token);

    @GET(GETVENUEPATH)
    Observable<VenuesSearchResponseInfo> getVenue(@Path("venue_id") String venue_id, @Query("oauth_token") String oauth_token);

    @POST(LIKEVENUEPATH)
    Observable<VenuesSearchResponseInfo> likeVenue(@Path("venue_id") String venue_id, @Query("set") Integer setLike, @Query("oauth_token") String oauth_token);


}
