package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by indraaguslesmana on 3/30/17.
 */

public class Authrequest {
    private String provider;
    private String email;
    private String password;
    private String device_id;

    public Authrequest() {
    }

    public Authrequest(String provider, String email, String password, String device_id) {
        this.provider = provider;
        this.email = email;
        this.password = password;
        this.device_id = device_id;
    }

    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("status")
    @Expose
    public Status status;

    public User getUser() {
        return user;
    }

    public Status getStatus() {
        return status;
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

    public class User {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("image_user")
        @Expose
        public String imageUser;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("access_token")
        @Expose
        public String accessToken;
        @SerializedName("token_generated_at")
        @Expose
        public String tokenGeneratedAt;
        @SerializedName("phone_number")
        @Expose
        public String phoneNumber;
        @SerializedName("last_login_at")
        @Expose
        public String lastLoginAt;
        @SerializedName("seller_shop_name")
        @Expose
        public String sellerShopName;
        @SerializedName("seller_kasir_name")
        @Expose
        public String sellerKasirName;
        @SerializedName("seller_phone")
        @Expose
        public String sellerPhone;
        @SerializedName("seller_address")
        @Expose
        public String sellerAddress;
        @SerializedName("seller_website")
        @Expose
        public String sellerWebsite;
        @SerializedName("seller_logo")
        @Expose
        public String sellerLogo;
        @SerializedName("seller_print_logo")
        @Expose
        public String sellerPrintLogo;
        @SerializedName("seller_warna_bg")
        @Expose
        public String sellerWarnaBg;
        @SerializedName("seller_id")
        @Expose
        public String sellerId;
        @SerializedName("device_id")
        @Expose
        public String deviceId;

        public String getId() {
            return id;
        }

        public String getImageUser() {
            return imageUser;
        }

        public String getEmail() {
            return email;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getTokenGeneratedAt() {
            return tokenGeneratedAt;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getLastLoginAt() {
            return lastLoginAt;
        }

        public String getSellerShopName() {
            return sellerShopName;
        }

        public String getSellerKasirName() {
            return sellerKasirName;
        }

        public String getSellerPhone() {
            return sellerPhone;
        }

        public String getSellerAddress() {
            return sellerAddress;
        }

        public String getSellerWebsite() {
            return sellerWebsite;
        }

        public String getSellerLogo() {
            return sellerLogo;
        }

        public String getSellerPrintLogo() {
            return sellerPrintLogo;
        }

        public String getSellerWarnaBg() {
            return sellerWarnaBg;
        }

        public String getSellerId() {
            return sellerId;
        }

        public String getDeviceId() {
            return deviceId;
        }
    }

}
