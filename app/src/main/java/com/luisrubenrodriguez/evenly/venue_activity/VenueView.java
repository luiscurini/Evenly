package com.luisrubenrodriguez.evenly.venue_activity;

import com.luisrubenrodriguez.evenly.model.Venue;

/**
 * Created by GamingMonster on 24.05.2017.
 */

public interface VenueView {

    void showLoading();

    void hideLoading();

    void loadData(Venue venue);

    void showError();

    void showLikeError();

    void updateLikeButton();

}
