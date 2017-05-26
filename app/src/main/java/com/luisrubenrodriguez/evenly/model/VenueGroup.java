package com.luisrubenrodriguez.evenly.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GamingMonster on 24.05.2017.
 */

public class VenueGroup implements Serializable{

    public static final long serialVersionUID = 20170525L;

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<VenueGroupItem> items = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<VenueGroupItem> getItems() {
        return items;
    }

    public void setItems(List<VenueGroupItem> items) {
        this.items = items;
    }
}
