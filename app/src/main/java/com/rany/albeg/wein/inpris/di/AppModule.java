package com.rany.albeg.wein.inpris.di;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.polidea.rxandroidble2.RxBleClient;
import com.rany.albeg.wein.inpris.App;
import com.rany.albeg.wein.inpris.R;
import com.rany.albeg.wein.inpris.di.qualifier.Base64Image;
import com.rany.albeg.wein.inpris.di.qualifier.HTMLTemplate;
import com.rany.albeg.wein.inpris.schedulers.AppSchedulerProvider;
import com.rany.albeg.wein.inpris.schedulers.SchedulerProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This file is a part of Inpris project.
 *
 * @author Rany Albeg Wein
 * @version 1.0.0
 * @since 03/06/2018
 */
@Module
public class AppModule {

    @Provides
    public Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    public SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @Singleton
    public RxBluetooth provideRxBluetooth(Context context) {
        return new RxBluetooth(context);
    }

    @Provides
    @Singleton
    public RxBleClient provideRxBleClient(Context context) {
        return RxBleClient.create(context);
    }

    @Provides
    @HTMLTemplate
    public String provideHtmlTemplate(Context context) {
        InputStream is = null;
        try {
            is = context.getAssets().open("inpris.html");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);

            return new String(buffer);
        } catch (IOException ex) {
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Fatal.
                }
            }
        }
    }

    @Provides
    @Base64Image
    public String provideBase64Logo(Context context) {
        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.inpris_logo);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        logoBitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
        byte[] ba = bao.toByteArray();
        return Base64.encodeToString(ba, Base64.DEFAULT);
    }
}