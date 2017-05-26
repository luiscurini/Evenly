package com.luisrubenrodriguez.evenly.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GamingMonster on 22.05.2017.
 */

//TODO make them Parceleable.
public class Venue implements Serializable {

    public static final long serialVersionUID = 20170525L;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contact")
    @Expose
    private VenueContact contact;
    @SerializedName("location")
    @Expose
    private VenueLocation location;
    @SerializedName("likes")
    @Expose
    private VenueLike likes;
    @SerializedName("like")
    @Expose
    private Boolean like;
    @SerializedName("categories")
    @Expose
    private List<VenueCategory> categories = null;
    @SerializedName("photos")
    @Expose
    private VenuePhotos photos;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VenueContact getContact() {
        return contact;
    }

    public void setContact(VenueContact contact) {
        this.contact = contact;
    }

    public VenueLocation getLocation() {
        return location;
    }

    public void setLocation(VenueLocation location) {
        this.location = location;
    }

    public List<VenueCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<VenueCategory> categories) {
        this.categories = categories;
    }

    public void setLikes(VenueLike likes) {
        this.likes = likes;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public VenueLike getLikes() {
        return likes;
    }

    public Boolean getLike() {
        return like;
    }

    public VenuePhotos getPhotos() {
        return photos;
    }
}
