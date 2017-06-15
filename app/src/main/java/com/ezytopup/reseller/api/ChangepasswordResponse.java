package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by indraaguslesmana on 5/12/17.
 */

public class ChangepasswordResponse {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public ChangepasswordResponse(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    @SerializedName("user")
    @Expose
    public String user;
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
}
