package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by indraaguslesmana on 5/12/17.
 */

public class ForgotpasswordResponse {
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("status")
    @Expose
    public Status status;

    public class User {

        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("avatar")
        @Expose
        public String avatar;
        @SerializedName("background")
        @Expose
        public String background;
        @SerializedName("phone")
        @Expose
        public Object phone;
        @SerializedName("created_at")
        @Expose
        public String createdAt;

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getBackground() {
            return background;
        }

        public Object getPhone() {
            return phone;
        }

        public String getCreatedAt() {
            return createdAt;
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
