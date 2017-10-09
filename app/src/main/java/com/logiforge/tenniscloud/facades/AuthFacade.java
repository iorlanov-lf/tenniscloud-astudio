package com.logiforge.tenniscloud.facades;

import com.logiforge.lavolta.android.app.DefaultAuthPlugin;
import com.logiforge.tenniscloud.TCAuthPlugin;

import java.util.UUID;

/**
 * Created by iorlanov on 8/5/17.
 */

public class AuthFacade {
    public void initAuthorization() {
        if (TCAuthPlugin.needsInitialization()) {
            String appId = UUID.randomUUID().toString();
            String deviceId = UUID.randomUUID().toString();
            TCAuthPlugin.init(appId, deviceId);
        }
    }

    public boolean isCloudRegistered() {
        TCAuthPlugin authPlugin = (TCAuthPlugin)DefaultAuthPlugin.instance();
        return authPlugin.isCloudRegistered();
    }
}
