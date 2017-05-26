package com.luisrubenrodriguez.evenly.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by GamingMonster on 22.05.2017.
 * Pojo created using http://www.jsonschema2pojo.org/.
 * For simplicity purposes, only required fields are stored.
 * TODO a personalised Gson or Moshi parser could be added
 */

public class VenuesSearchResponseInfo {

    @SerializedName("meta")
    @Expose
    private VenuesSearchResponseMeta meta;

    @SerializedName("response")
    @Expose
    private VenuesSearchResponse response;

    public VenuesSearchResponseMeta getMeta() {
        return meta;
    }

    public void setMeta(VenuesSearchResponseMeta meta) {
        this.meta = meta;
    }

    public VenuesSearchResponse getResponse() {
        return response;
    }

    public void setResponse(VenuesSearchResponse response) {
        this.response = response;
    }


}
