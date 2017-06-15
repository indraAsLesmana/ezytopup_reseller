package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/13/17.
 */

public class ListCategoryResponse {

    @SerializedName("0")
    @Expose
    public _0 _0;
    @SerializedName("status")
    @Expose
    public Status status;

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

    public class _0 {

        @SerializedName("categories")
        @Expose
        public ArrayList<Category> categories = null;

    }

    public class Category {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("category_url")
        @Expose
        public String categoryUrl;
        @SerializedName("categori_details")
        @Expose
        public CategoriDetails categoriDetails;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public String getCategoryUrl() {
            return categoryUrl;
        }

        public CategoriDetails getCategoriDetails() {
            return categoriDetails;
        }
    }

    public class CategoriDetails {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;

    }
}
