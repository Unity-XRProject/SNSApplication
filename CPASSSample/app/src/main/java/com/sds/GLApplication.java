package com.sds;

import android.app.Application;

import com.sds.xr.Communicator.SDKCommunicator;
import com.sds.xr.utils.Logger;

/**
 * Created by drift.park on 2019-04-12.
 */
public class GLApplication extends Application {

    private static GLApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Logger.init(this);
        SDKCommunicator.init(this);
    }

    public static GLApplication getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        SDKCommunicator.deinit();
        super.onTerminate();
    }
}
