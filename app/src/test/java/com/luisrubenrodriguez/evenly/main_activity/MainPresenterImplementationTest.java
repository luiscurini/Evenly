package com.luisrubenrodriguez.evenly.main_activity;

import com.luisrubenrodriguez.evenly.model.Venue;
import com.luisrubenrodriguez.evenly.model.VenuesSearchResponse;
import com.luisrubenrodriguez.evenly.model.VenuesSearchResponseInfo;
import com.luisrubenrodriguez.evenly.service.FoursquareService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by GamingMonster on 26.05.2017.
 */
public class MainPresenterImplementationTest {

    @Mock
    private MainView mMainView;

    @Mock
    private FoursquareService mFoursquareService;

    @Mock
    private Subscription mSubscription;

    private MainPresenterImplementation mMainPresenterImplementation;

    @Before
    public void setupClass() {
        MockitoAnnotations.initMocks(this);

        mMainPresenterImplementation = new MainPresenterImplementation(mMainView, mFoursquareService);
    }

    @Test
    public void testOnDataFailure() throws Exception {
        mMainPresenterImplementation.onDataFailure();
        verify(mMainView).hideLoading();
        verify(mMainView).showConnectionError();
    }

    @Test
    public void testOnDataSuccessEmptyResponse() throws Exception {
        VenuesSearchResponseInfo responseInfo = new VenuesSearchResponseInfo();
        mMainPresenterImplementation.onDataSuccess(responseInfo);
        verify(mMainView).hideLoading();
        verify(mMainView).showConnectionError();
    }

    @Test
    public void testOnDataSuccessWithData() throws Exception {
        VenuesSearchResponseInfo responseInfo = new VenuesSearchResponseInfo();

        VenuesSearchResponse response = new VenuesSearchResponse();
        List<Venue> venues = new ArrayList<>();
        Venue venue = new Venue();
        venue.setId("TestId");
        venues.add(venue);
        response.setVenues(venues);
        responseInfo.setResponse(response);

        mMainPresenterImplementation.onDataSuccess(responseInfo);
        verify(mMainView).hideLoading();
        verify(mMainView).loadNearByVenues(venues);
    }

    @Test
    public void testGetNearByVenues() throws Exception {
        String location = "52.1,13,1";
        when(mFoursquareService.getNearbyVenues(location, mMainPresenterImplementation)).thenReturn(mSubscription);

        mMainPresenterImplementation.getNearByVenues(location);
        verify(mMainView).showLoading();
    }


}