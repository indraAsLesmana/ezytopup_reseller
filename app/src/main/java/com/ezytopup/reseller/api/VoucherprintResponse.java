package com.ezytopup.reseller.api;

import com.ezytopup.reseller.utility.Helper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by indraaguslesmana on 5/17/17.
 */

public class VoucherprintResponse implements Serializable{

    private static final String TAG = "VoucherprintResponse";
    private static VoucherprintResponse.Result userInstance;
    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("status")
    @Expose
    public Status status;

    public static VoucherprintResponse.Result getInstance() {
        if (userInstance == null) {
            Helper.log(TAG, " last product null", null);
        }
        return userInstance;
    }

    public static void setUserInstance(Result userInstance) {
        VoucherprintResponse.userInstance = userInstance;
    }

    public static class Result implements Serializable{

        @SerializedName("seller_id")
        @Expose
        public String sellerId;
        @SerializedName("seller_kasir_name")
        @Expose
        public String sellerKasirName;
        @SerializedName("seller_print_logo")
        @Expose
        public String sellerPrintLogo;
        @SerializedName("seller_shop_name")
        @Expose
        public String sellerShopName;
        @SerializedName("no_nota")
        @Expose
        public String noNota;
        @SerializedName("tgl_cetak")
        @Expose
        public String tglCetak;
        @SerializedName("reprint_time")
        @Expose
        public String reprintTime;
        @SerializedName("baris01")
        @Expose
        public String baris01;
        @SerializedName("baris02")
        @Expose
        public String baris02;
        @SerializedName("baris03")
        @Expose
        public String baris03;
        @SerializedName("baris04")
        @Expose
        public String baris04;
        @SerializedName("baris05")
        @Expose
        public String baris05;
        @SerializedName("baris06")
        @Expose
        public String baris06;
        @SerializedName("baris07")
        @Expose
        public String baris07;
        @SerializedName("baris08")
        @Expose
        public String baris08;
        @SerializedName("baris09")
        @Expose
        public String baris09;
        @SerializedName("baris10")
        @Expose
        public String baris10;
        @SerializedName("baris11")
        @Expose
        public String baris11;
        @SerializedName("baris12")
        @Expose
        public String baris12;
        @SerializedName("baris13")
        @Expose
        public String baris13;
        @SerializedName("baris14")
        @Expose
        public String baris14;
        @SerializedName("baris15")
        @Expose
        public String baris15;
        @SerializedName("baris16")
        @Expose
        public String baris16;
        @SerializedName("baris17")
        @Expose
        public String baris17;
        @SerializedName("baris18")
        @Expose
        public String baris18;
        @SerializedName("baris19")
        @Expose
        public String baris19;
        @SerializedName("baris20")
        @Expose
        public String baris20;
        @SerializedName("baris21")
        @Expose
        public String baris21;
        @SerializedName("baris22")
        @Expose
        public String baris22;
        @SerializedName("baris23")
        @Expose
        public String baris23;
        @SerializedName("baris24")
        @Expose
        public String baris24;
        @SerializedName("baris25")
        @Expose
        public String baris25;
        @SerializedName("baris26")
        @Expose
        public String baris26;
        @SerializedName("baris27")
        @Expose
        public String baris27;
        @SerializedName("baris28")
        @Expose
        public String baris28;
        @SerializedName("baris29")
        @Expose
        public String baris29;
        @SerializedName("baris30")
        @Expose
        public String baris30;
        @SerializedName("baris31")
        @Expose
        public String baris31;
        @SerializedName("baris32")
        @Expose
        public String baris32;
        @SerializedName("baris33")
        @Expose
        public String baris33;
        @SerializedName("baris34")
        @Expose
        public String baris34;
        @SerializedName("baris35")
        @Expose
        public String baris35;

        public String getSellerId() {
            return sellerId;
        }

        public String getSellerKasirName() {
            return sellerKasirName;
        }

        public String getSellerPrintLogo() {
            return sellerPrintLogo;
        }

        public String getSellerShopName() {
            return sellerShopName;
        }

        public String getNoNota() {
            return noNota;
        }

        public String getTglCetak() {
            return tglCetak;
        }

        public String getReprintTime() {
            return reprintTime;
        }

        public String getBaris01() {
            return baris01;
        }

        public String getBaris02() {
            return baris02;
        }

        public String getBaris03() {
            return baris03;
        }

        public String getBaris04() {
            return baris04;
        }

        public String getBaris05() {
            return baris05;
        }

        public String getBaris06() {
            return baris06;
        }

        public String getBaris07() {
            return baris07;
        }

        public String getBaris08() {
            return baris08;
        }

        public String getBaris09() {
            return baris09;
        }

        public String getBaris10() {
            return baris10;
        }

        public String getBaris11() {
            return baris11;
        }

        public String getBaris12() {
            return baris12;
        }

        public String getBaris13() {
            return baris13;
        }

        public String getBaris14() {
            return baris14;
        }

        public String getBaris15() {
            return baris15;
        }

        public String getBaris16() {
            return baris16;
        }

        public String getBaris17() {
            return baris17;
        }

        public String getBaris18() {
            return baris18;
        }

        public String getBaris19() {
            return baris19;
        }

        public String getBaris20() {
            return baris20;
        }

        public String getBaris21() {
            return baris21;
        }

        public String getBaris22() {
            return baris22;
        }

        public String getBaris23() {
            return baris23;
        }

        public String getBaris24() {
            return baris24;
        }

        public String getBaris25() {
            return baris25;
        }

        public String getBaris26() {
            return baris26;
        }

        public String getBaris27() {
            return baris27;
        }

        public String getBaris28() {
            return baris28;
        }

        public String getBaris29() {
            return baris29;
        }

        public String getBaris30() {
            return baris30;
        }

        public String getBaris31() {
            return baris31;
        }

        public String getBaris32() {
            return baris32;
        }

        public String getBaris33() {
            return baris33;
        }

        public String getBaris34() {
            return baris34;
        }

        public String getBaris35() {
            return baris35;
        }
    }

    public class Status implements Serializable{

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
