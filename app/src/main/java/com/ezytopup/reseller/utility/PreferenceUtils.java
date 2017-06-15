package com.ezytopup.reseller.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.VoucherprintResponse;
import com.google.gson.Gson;

/**
 * Created by indraaguslesmana on 3/30/17.
 */

public class PreferenceUtils {

    private static final String TAG = "PreferenceUtils";
    private final int STORENAME_KEY = R.string.settings_def_storename_key;
    private final int STORELOGO_KEY = R.string.settings_def_storelogo_key;
    private final static String LAST_PRODUCT = "lastProduct";

    public static void setStoreDetail (Context context, String id, String first_name,
                                       String last_name, String email, String phone_number,
                                       String access_token, String image_user,
                                       String seller_shop_name, String seller_kasir_name,
                                       String seller_phone, String seller_address,
                                       String seller_website, String seller_logo,
                                       String seller_print_logo, String seller_warna_bg,
                                       String seller_id){
        SharedPreferences.Editor editor = Eztytopup.getsPreferences().edit();
        editor.putString(context.getString(R.string.settings_def_uid_key), id);
        editor.putString(context.getString(R.string.settings_def_storefirst_name_key), first_name);
        editor.putString(context.getString(R.string.settings_def_storelast_name_key), last_name);
        editor.putString(context.getString(R.string.settings_def_storeemail_key), email);
        editor.putString(context.getString(R.string.settings_def_storephone_number_key), phone_number);
        editor.putString(context.getString(R.string.settings_def_storeaccess_token_key), access_token);
        editor.putString(context.getString(R.string.settings_def_storeimage_user_key), image_user);
        editor.putString(context.getString(R.string.settings_def_sellershopname_key), seller_shop_name);
        editor.putString(context.getString(R.string.settings_def_sellerkasirname_key), seller_kasir_name);
        editor.putString(context.getString(R.string.settings_def_sellerphone_key), seller_phone);
        editor.putString(context.getString(R.string.settings_def_selleraddress_key), seller_address);
        editor.putString(context.getString(R.string.settings_def_sellerwebsite_key), seller_website);
        editor.putString(context.getString(R.string.settings_def_sellerlogo_key), seller_logo);
        editor.putString(context.getString(R.string.settings_def_sellerprintlogo_key), seller_print_logo);
        editor.putString(context.getString(R.string.settings_def_sellerwarnabg_key), seller_warna_bg);
        editor.putString(context.getString(R.string.settings_def_sellerid_key), seller_id);
        editor.apply();
    }

    public static void setProfileUpdate(Context context, String first_name,
                                        String email, String phone_number, String image_user) {
        SharedPreferences.Editor editor = Eztytopup.getsPreferences().edit();
        editor.putString(context.getString(R.string.settings_def_storefirst_name_key), first_name);
        editor.putString(context.getString(R.string.settings_def_storeemail_key), email);
        editor.putString(context.getString(R.string.settings_def_storephone_number_key), phone_number);
        editor.putString(context.getString(R.string.settings_def_storeimage_user_key), image_user);
        editor.apply();
    }

    public static void setDeviceId (Context context, String deviceId){
        SharedPreferences.Editor editor = Eztytopup.getsPreferences().edit();
        editor.putString(context.getString(R.string.settings_def_storeidevice_key), deviceId);
        editor.apply();
    }

    public static void destroyUserSession(Context context) {
        SharedPreferences.Editor editor = Eztytopup.getsPreferences().edit();
        editor.remove(context.getString(R.string.settings_def_uid_key));
        editor.remove(context.getString(R.string.settings_def_storefirst_name_key));
        editor.remove(context.getString(R.string.settings_def_storelast_name_key));
        editor.remove(context.getString(R.string.settings_def_storeemail_key));
        editor.remove(context.getString(R.string.settings_def_storeemail_key));
        editor.remove(context.getString(R.string.settings_def_storeaccess_token_key));
        editor.remove(context.getString(R.string.settings_def_storeimage_user_key));
        editor.remove(context.getString(R.string.settings_def_sellershopname_key));
        editor.remove(context.getString(R.string.settings_def_sellerkasirname_key));
        editor.remove(context.getString(R.string.settings_def_sellerphone_key));
        editor.remove(context.getString(R.string.settings_def_selleraddress_key));
        editor.remove(context.getString(R.string.settings_def_sellerwebsite_key));
        editor.remove(context.getString(R.string.settings_def_sellerlogo_key));
        editor.remove(context.getString(R.string.settings_def_sellerprintlogo_key));
        editor.remove(context.getString(R.string.settings_def_sellerwarnabg_key));
        editor.remove(context.getString(R.string.settings_def_sellerid_key));
        destroyLastProduct();
        editor.apply();
    }

    public static void saveLastProduct(String gsonStringObj){
        SharedPreferences.Editor editor = Eztytopup.getsPreferences().edit();
        editor.putString(LAST_PRODUCT, gsonStringObj);
        editor.apply();
    }
    
    public static VoucherprintResponse.Result getLastProduct() {
        SharedPreferences dataPreferece = Eztytopup.getsPreferences();
        Gson gson = new Gson();
        String data = dataPreferece.getString(LAST_PRODUCT, Constant.PREF_NULL);
        VoucherprintResponse.Result lastProduct = null;
        if (!data.equals(Constant.PREF_NULL)){
            lastProduct = gson.fromJson(data,
                    VoucherprintResponse.Result.class);
        }
        return lastProduct;
    }
    public static void destroyLastProduct(){
        SharedPreferences.Editor editor = Eztytopup.getsPreferences().edit();
        editor.remove(LAST_PRODUCT);
        editor.apply();
    }


    public static String getSinglePrefrenceString(Context context, int prefereceKeyName){
        String result = null;
        SharedPreferences dataPreferece = Eztytopup.getsPreferences();
        switch (prefereceKeyName){
            case R.string.settings_def_storename_key:
                result = dataPreferece.getString(context.getString(R.string.settings_def_storename_key),
                        context.getString(R.string.settings_def_storename_default));
                break;
            case R.string.settings_def_storelogo_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storelogo_key),
                        context.getString(R.string.settings_def_storelogo_default));
                break;
            case R.string.settings_def_storefirst_name_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storefirst_name_key),
                        context.getString(R.string.settings_def_storefirst_name_default));
                break;

            case R.string.settings_def_storelast_name_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storelast_name_key),
                        context.getString(R.string.settings_def_storelast_name_default));
                break;
            case R.string.settings_def_storeemail_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storeemail_key),
                        context.getString(R.string.settings_def_storeemail_default));
                break;
            case R.string.settings_def_storephone_number_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storephone_number_key),
                        context.getString(R.string.settings_def_storephone_number_default));
                break;
            case R.string.settings_def_storeaccess_token_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storeaccess_token_key),
                        context.getString(R.string.settings_def_storeaccess_token_default));
                break;
            case R.string.settings_def_storeimage_user_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storeimage_user_key),
                        context.getString(R.string.settings_def_storeimage_user_default));
                break;
            case R.string.settings_def_storeidevice_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storeidevice_key),
                        context.getString(R.string.settings_def_storeidevice_default));
                break;

            case R.string.settings_def_sellershopname_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_sellershopname_key),
                        context.getString(R.string.settings_def_sellershopname_default));
                break;
            case R.string.settings_def_sellerkasirname_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_sellerkasirname_key),
                        context.getString(R.string.settings_def_sellerkasirname_default));
                break;
            case R.string.settings_def_sellerphone_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_sellerphone_key),
                        context.getString(R.string.settings_def_sellerphone_default));
                break;
            case R.string.settings_def_selleraddress_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_selleraddress_key),
                        context.getString(R.string.settings_def_selleraddress_default));
                break;
            case R.string.settings_def_sellerwebsite_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_sellerwebsite_key),
                        context.getString(R.string.settings_def_sellerwebsite_default));
                break;
            case R.string.settings_def_sellerlogo_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_sellerlogo_key),
                        context.getString(R.string.settings_def_sellerlogo_default));
                break;
            case R.string.settings_def_sellerprintlogo_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_sellerprintlogo_key),
                        context.getString(R.string.settings_def_sellerprintlogo_default));
                break;
            case R.string.settings_def_uid_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_uid_key),
                        context.getString(R.string.settings_def_uid_default));
                break;
            case R.string.settings_def_sellerwarnabg_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_sellerwarnabg_key),
                        context.getString(R.string.settings_def_sellerwarnabg_default));
                break;
            case R.string.settings_def_sellerid_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_sellerid_key),
                        context.getString(R.string.settings_def_sellerid_default));
                break;
        }
        return result;
    }
}
