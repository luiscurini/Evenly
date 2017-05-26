package com.luisrubenrodriguez.evenly.main_activity;

import com.luisrubenrodriguez.evenly.model.Venue;

import java.util.List;

/**
 * Created by GamingMonster on 22.05.2017.
 * Interface that MainActivity must implement
 */

interface MainView {

    void showLoading();

    void hideLoading();

    void showConnectionError();

    void loadNearByVenues(List<Venue> venues);

}
