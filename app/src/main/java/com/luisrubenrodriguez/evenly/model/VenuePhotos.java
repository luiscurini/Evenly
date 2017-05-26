package com.luisrubenrodriguez.evenly.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GamingMonster on 24.05.2017.
 */

public class VenuePhotos implements Serializable {

    public static final long serialVersionUID = 20170525L;

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("groups")
    @Expose
    private List<VenueGroup> groups = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<VenueGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<VenueGroup> groups) {
        this.groups = groups;
    }
}
