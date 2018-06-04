package com.rany.albeg.wein.inpris.data;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This file is a part of Inpris project.
 *
 * @author Rany Albeg Wein
 * @version 1.0.0
 * @since 03/06/2018
 */
public class DiscoveryResult {

    public static final int SUCCESS = 1;
    public static final int LOADING = 2;
    public static final int ERROR = 3;
    public static final int COMPLETE = 4;

    @IntDef({ SUCCESS, LOADING, ERROR, COMPLETE })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {}

    public final BluetoothDevice data;
    public final @Status int status;
    public final Throwable error;

    public DiscoveryResult(int status, BluetoothDevice data, Throwable error) {
        this.data = data;
        this.status = status;
        this.error = error;
    }

    public static DiscoveryResult success(BluetoothDevice data) {
        return new DiscoveryResult(SUCCESS, data, null);
    }

    public static DiscoveryResult loading() {
        return new DiscoveryResult(LOADING, null, null);
    }

    public static DiscoveryResult error(Throwable throwable) {
        return new DiscoveryResult(ERROR, null, throwable);
    }

    public static DiscoveryResult complete() {
        return new DiscoveryResult(COMPLETE, null, null);
    }
}
