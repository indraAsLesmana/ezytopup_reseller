package com.ezytopup.reseller;

import com.ezytopup.reseller.utility.Helper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        /**
         * enable this, to make user without login running.
         * */
       /* String token = FirebaseInstanceId.getInstance().getToken();
        Helper.synchronizeFCMRegToken(this, token);*/
    }
}