package com.luisrubenrodriguez.evenly.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by GamingMonster on 22.05.2017.
 */

public class VenuesSearchResponse {

    @SerializedName("venues")
    @Expose
    private List<Venue> venues = null;
    @SerializedName("venue")
    @Expose
    private Venue venue = null;
    @SerializedName("confident")
    @Expose
    private Boolean confident;

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public Boolean getConfident() {
        return confident;
    }

    public void setConfident(Boolean confident) {
        this.confident = confident;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}
