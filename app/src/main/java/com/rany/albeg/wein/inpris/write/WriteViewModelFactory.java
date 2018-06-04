package com.rany.albeg.wein.inpris.write;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.rany.albeg.wein.inpris.di.qualifier.Base64Image;
import com.rany.albeg.wein.inpris.di.qualifier.HTMLTemplate;
import com.rany.albeg.wein.inpris.schedulers.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * This file is a part of Inpris project.
 *
 * @author Rany Albeg Wein
 * @version 1.0.0
 * @since 03/06/2018
 */
public class WriteViewModelFactory implements ViewModelProvider.Factory {

    private final CompositeDisposable mDisposable;
    private RxBluetooth mRxBluetooth;
    private final SchedulerProvider mSchedulerProvider;
    private String mHtmlTemplate;
    private String mBase64Image;

    @Inject
    public WriteViewModelFactory(CompositeDisposable disposables, RxBluetooth rxBluetooth,
            SchedulerProvider schedulerProvider, @HTMLTemplate String htmlTemplate, @Base64Image String base64Image) {

        mDisposable = disposables;
        mRxBluetooth = rxBluetooth;
        mSchedulerProvider = schedulerProvider;
        mHtmlTemplate = htmlTemplate;
        mBase64Image = base64Image;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WriteViewModel.class)) {
            //noinspection unchecked
            return (T) new WriteViewModel(mDisposable, mRxBluetooth, mSchedulerProvider, mHtmlTemplate, mBase64Image);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
