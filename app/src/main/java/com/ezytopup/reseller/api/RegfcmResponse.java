package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by indraaguslesmana on 4/25/17.
 */

public class RegfcmResponse {

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
        @SerializedName("nama")
        @Expose
        public String nama;
        @SerializedName("hp")
        @Expose
        public String hp;
        @SerializedName("image_user")
        @Expose
        public Object imageUser;
        @SerializedName("saldo")
        @Expose
        public Integer saldo;
        @SerializedName("token")
        @Expose
        public String token;
        @SerializedName("token_generated_at")
        @Expose
        public String tokenGeneratedAt;
        @SerializedName("last_login_at")
        @Expose
        public String lastLoginAt;

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getNama() {
            return nama;
        }

        public String getHp() {
            return hp;
        }

        public Object getImageUser() {
            return imageUser;
        }

        public Integer getSaldo() {
            return saldo;
        }

        public String getToken() {
            return token;
        }

        public String getTokenGeneratedAt() {
            return tokenGeneratedAt;
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
