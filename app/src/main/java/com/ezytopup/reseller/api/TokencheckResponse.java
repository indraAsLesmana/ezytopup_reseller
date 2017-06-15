package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by indraaguslesmana on 4/21/17.
 */

public class TokencheckResponse {
    @SerializedName("token_validity")
    @Expose
    public Boolean tokenValidity;
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
