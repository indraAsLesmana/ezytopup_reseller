package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by luteh on 18/05/17.
 */

public class SendFeedBackResponse {
    @SerializedName("user")
    @Expose
    public Object user;
    @SerializedName("status")
    @Expose
    public SendFeedBackResponse.Status status;

    public Object getUser() {
        return user;
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
