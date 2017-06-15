package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by indraaguslesmana on 5/29/17.
 */

public class UpdateProfileResponse {

    private String first_name;
    private String phone;
    private String password;
    private String avatar;

    public UpdateProfileResponse(String first_name, String phone, String password, String avatar) {
        this.first_name = first_name;
        this.phone = phone;
        this.password = password;
        this.avatar = avatar;
    }

    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("status")
    @Expose
    public Status status;

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
        public String avatar;
        @SerializedName("image")
        @Expose
        public String image;
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

        public String getAvatar() {
            return avatar;
        }

        public String getImage() {
            return image;
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
