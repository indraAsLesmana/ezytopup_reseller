package com.ezytopup.reseller.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by indraaguslesmana on 5/19/17.
 */

public class ServertimeResponse {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("status")
    @Expose
    public Status status;

    public class Result {

        @SerializedName("server_time")
        @Expose
        public String serverTime;

        public String getServerTime() {
            return serverTime;
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
