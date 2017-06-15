package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/4/17.
 */

public class HeaderimageResponse {

    @SerializedName("result")
    @Expose
    public ArrayList<Result> result = null;
    @SerializedName("status")
    @Expose
    public Status status;

    public static class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("short_description")
        @Expose
        public String shortDescription;
        @SerializedName("image_url")
        @Expose
        public String imageUrl;

        public String getId() {
            return id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public class Status {

        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("message")
        @Expose
        public String message;

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
