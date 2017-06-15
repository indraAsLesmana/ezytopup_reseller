package com.ezytopup.reseller.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.Authrequest;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by indraaguslesmana on 4/1/17.
 */

public class Helper {

    private static ProgressDialog sProgressDialog;
    private static final String TAG = "Helper";

    /**
     * Show progress dialog, can only be called once per tier (show-hide)
     */
    public static void showProgressDialog(Context ctx, int bodyStringId) {
        if(sProgressDialog == null) {
            sProgressDialog = ProgressDialog.show(ctx,
                    ctx.getString(R.string.progress_title_default),
                    ctx.getString(bodyStringId), true, false, null);
        }
    }

    /**
     * Hide current progress dialog and set to NULL
     */
    public static void hideProgressDialog() {
        if(sProgressDialog != null && sProgressDialog.isShowing()) {
            sProgressDialog.dismiss();
            sProgressDialog = null;     // so it can be called in the next showProgressDialog
        }
    }

    public static void synchronizeFCMRegToken(final Context context, String token) {
        String deviceid = null;
        if (token == null) {
            token = FirebaseInstanceId.getInstance().getToken();
            deviceid = PreferenceUtils.getSinglePrefrenceString(context,
                    R.string.settings_def_storeidevice_key);
        }

        Call<Authrequest> skip = Eztytopup.getsAPIService()
                .setLoginskip("email", token, deviceid);
        skip.enqueue(new Callback<Authrequest>() {
            @Override
            public void onResponse(Call<Authrequest> call, Response<Authrequest> response) {
                if (response.isSuccessful() && response.body().status
                        .getCode().equals(String.valueOf(HttpURLConnection.HTTP_CREATED))) {
                    Helper.log(TAG, String.format("userToken %s",
                            response.body().getUser().getAccessToken()), null);
                    PreferenceUtils.setStoreDetail(context,

                            response.body().getUser().getId(),
                            response.body().getUser().getFirstName(),
                            response.body().getUser().getLastName(),
                            response.body().getUser().getEmail(),
                            response.body().getUser().getPhoneNumber(),
                            response.body().getUser().getAccessToken(),
                            response.body().getUser().getImageUser(),
                            response.body().getUser().getSellerShopName(),
                            response.body().getUser().getSellerKasirName(),
                            response.body().getUser().getSellerPhone(),
                            response.body().getUser().getSellerAddress(),
                            response.body().getUser().getSellerWebsite(),
                            response.body().getUser().getSellerLogo(),
                            response.body().getUser().getSellerPrintLogo(),
                            response.body().getUser().getSellerWarnaBg(),
                            response.body().getUser().getSellerId());
                }
            }

            @Override
            public void onFailure(Call<Authrequest> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void log(String TAG, String message, Throwable throwable) {
        if(Constant.ENABLE_LOG) {
            Log.v(TAG, message, throwable);
        }
    }

    public static void apiSnacbarError(Context context, Throwable t, View view){
        Helper.log(TAG, "onFailure: " + t.getMessage(), null);
        String message = t.getMessage();
        if (t.getMessage().contains("Use JsonReader.setLenient")) {
            message = context.getResources().getString(R.string.response_error);
        }
        if (t.getMessage().contains("Expected BEGIN")) {
            message = context.getResources().getString(R.string.response_error);
        }
        if (t.getMessage().contains("Unable to resolve host")) {
            message = context.getResources().getString(R.string.cantreachserver);
        }

        final Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void snacbarError(int message, View view){
        Helper.log(TAG, "onFailure: " + message, null);
        final Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void snacbarError(String message, View view){
        Helper.log(TAG, "onFailure: " + message, null);
        final Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void downloadFile(Context context, String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Ezytopup");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        @SuppressLint("SdCardPath")
        File file = new File("/mnt/sdcard/Ezytopup/print_logo.jpg");
        if (file.exists()){
            return;
        }else {
            DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(uRl);
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Ezytopup")
                    .setDescription("Ezytopup resouce folder")
                    .setDestinationInExternalPublicDir("/Ezytopup", "print_logo.jpg");

            mgr.enqueue(request);
        }
    }

    public static boolean dateCheck(String tglCetak, String reprintTime, String serverTime){
        SimpleDateFormat df = new SimpleDateFormat(getDefaultDisplayDateTimeFormat());
        Date startDate = null, currentTime = null, endDate = null, tempDate;

        try {
            startDate = df.parse(tglCetak);
            tempDate = startDate;
            Calendar cal = Calendar.getInstance();
            cal.setTime(tempDate);
            cal.add(Calendar.MINUTE, Integer.parseInt(reprintTime));
            String tempDateupdate = df.format(cal.getTime());
            endDate = df.parse(tempDateupdate);
            currentTime = df.parse(serverTime);
            Helper.log(TAG, "original   : " + tglCetak, null);
            Helper.log(TAG, "validate   : " + tempDateupdate, null);
            Helper.log(TAG, "now   : " + serverTime, null);
            Helper.log(TAG, "isWithRage   : " + Helper.isWithinRange(currentTime,
                    startDate, endDate), null);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isWithinRange(currentTime, startDate, endDate);
    }
    private static boolean isWithinRange(Date currenTime, Date startDate, Date endDate) {
        return !(currenTime.before(startDate) || currenTime.after(endDate));
    }

    /**
     * Show soft keyboard for given view
     */
    public static void hideSoftKeyboard(View view) {
        if(view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager)
                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void enableImmersiveMode(View view) {
        int systemUiVisibility = view.getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        systemUiVisibility |= flags;
        view.setSystemUiVisibility(systemUiVisibility);
    }

    /**
     * this tricky to get is keyboard hide or not.
     * and if keybord is hide, rerun again immersive mode
     * */
    public static void setImmersivebyKeyboard(final View rootView){
        rootView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                        if (heightDiff > 100) {
                            Helper.log(TAG, "keyboard opened", null);
                        } else {
                            Helper.log(TAG, "keyboard closed", null);
                            Helper.enableImmersiveMode(rootView);
                        }
                    }
                });
    }

    public static String formatCurrency(String value) {
        DecimalFormat kursIna = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        /**
         * setMin & setMax make currency remove ",00"
         * */
        kursIna.setMinimumFractionDigits(0);
        kursIna.setMaximumFractionDigits(0);

        kursIna.setDecimalFormatSymbols(formatRp);
        return kursIna.format(Double.parseDouble(value));
    }

    public static String convertStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Context context,
                                    final View uiView, final View uiProgress) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            if (uiView != null){
                uiView.setVisibility(show ? View.GONE : View.VISIBLE);
                uiView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        uiView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
            }

            uiProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            uiProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    uiProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            if (uiView != null){
                uiView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
            uiProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private static String getDefaultDisplayDateTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }

}
