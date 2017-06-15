package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 5/11/17.
 */

public class WalletbalanceResponse {

    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("history")
    @Expose
    public ArrayList<Object> history = null;
    @SerializedName("balance")
    @Expose
    public Integer balance;
    @SerializedName("status")
    @Expose
    public Status status;

    public Integer getBalance() {
        return balance;
    }

    public class User {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("avatar")
        @Expose
        public Object avatar;
        @SerializedName("background")
        @Expose
        public Object background;
        @SerializedName("phone_number")
        @Expose
        public String phoneNumber;
        @SerializedName("last_login_at")
        @Expose
        public String lastLoginAt;

        public String getId() {
            return id;
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

        public Object getAvatar() {
            return avatar;
        }

        public Object getBackground() {
            return background;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getLastLoginAt() {
            return lastLoginAt;
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
