package com.luisrubenrodriguez.evenly.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by GamingMonster on 22.05.2017.
 */

public class VenuesSearchResponseMeta {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("requestId")
    @Expose
    private String requestId;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


}
