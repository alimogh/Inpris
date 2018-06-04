package com.rany.albeg.wein.inpris.data;

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
public class DataTransferResult {

    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;

    @IntDef({SUCCESS,FAILURE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status{}

    public final @Status int status;
    public final String data;
    public final Throwable error;

    private DataTransferResult(int status, String data,Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static DataTransferResult success(String data) {
        return new DataTransferResult(SUCCESS, data, null);
    }

    public static DataTransferResult failure(Throwable throwable) {
        return new DataTransferResult(FAILURE, null, throwable);
    }
}
