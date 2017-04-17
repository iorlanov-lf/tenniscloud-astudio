package com.logiforge.tenniscloud;

import android.content.Context;

import com.logiforge.lavolta.android.app.AuthUrlFactory;
import com.logiforge.lavolta.android.app.DefaultAuthPlugin;
import com.logiforge.lavolta.android.model.config.UserNotificationSpecs;

/**
 * Created by iorlanov on 3/8/2017.
 */
public class TCAuthPlugin extends DefaultAuthPlugin {
    private static final String GCM_SENDER_ID = "774752912984";

    TCAuthPlugin(Context context) {
        super(context);
    }

    @Override
    protected AuthUrlFactory instantiateUrlFactory() {
        return new SkedioAuthUrlFactory();
    }

    @Override
    protected UserNotificationSpecs instantiateSigninNotificationSpec() {
        return null;
    }

    @Override
    protected String getGcmSenderId() {
        return GCM_SENDER_ID;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    private static class SkedioAuthUrlFactory implements AuthUrlFactory {
        private static final String PATH_AUTH = "/auth";

        @Override
        public String authUrl(String op) {
            return PATH_AUTH + "/" + op;
        }
    }

    public boolean isCloudRegistered() {
        return false;
    }
}