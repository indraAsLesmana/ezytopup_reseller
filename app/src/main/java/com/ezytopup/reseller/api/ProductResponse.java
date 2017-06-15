package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/2/17.
 */

public class ProductResponse {

    @SerializedName("result")
    @Expose
    private ArrayList<Result> result = null;

    public ArrayList<Result> getResult() {
        return result;
    }

    public static class Result {

        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("products")
        @Expose
        private ArrayList<Product> products = null;

        public String getCategoryId() {
            return categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public ArrayList<Product> getProducts() {
            return products;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public void setProducts(ArrayList<Product> products) {
            this.products = products;
        }

    }

    public static class Product implements Serializable{

        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("harga_toko")
        @Expose
        private String hargaToko;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("background_image_url")
        @Expose
        private String backgroundImageUrl;

        public String getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getHargaToko() {
            return hargaToko;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getBackgroundImageUrl() {
            return backgroundImageUrl;
        }
    }

}
