package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/5/17.
 */

public class BestSellerResponse {

    @SerializedName("products")
    @Expose
    public ArrayList<Product> products = null;
    @SerializedName("status")
    @Expose
    public Status status;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public Status getStatus() {
        return status;
    }

    public static class Product implements Serializable{

        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("product_name")
        @Expose
        public String productName;
        @SerializedName("category_name")
        @Expose
        public String categoryName;
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("review_url")
        @Expose
        public String reviewUrl;
        @SerializedName("background_image_url")
        @Expose
        public String backgroundImageUrl;

        public String getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getPrice() {
            return price;
        }

        public String getReviewUrl() {
            return reviewUrl;
        }

        public String getBackgroundImageUrl() {
            return backgroundImageUrl;
        }
    }

    public static class Status {

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
