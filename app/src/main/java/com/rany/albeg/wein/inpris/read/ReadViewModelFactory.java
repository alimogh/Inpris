package com.rany.albeg.wein.inpris.read;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;
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
public class ReadViewModelFactory implements ViewModelProvider.Factory {

    private final CompositeDisposable mDisposables;
    private RxBluetooth mRxBluetooth;
    private final SchedulerProvider mSchedulerProvider;

    @Inject
    public ReadViewModelFactory(CompositeDisposable disposables, RxBluetooth rxBluetooth,
            SchedulerProvider schedulerProvider) {
        mDisposables = disposables;
        mRxBluetooth = rxBluetooth;
        mSchedulerProvider = schedulerProvider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ReadViewModel.class)) {
            //noinspection unchecked
            return (T) new ReadViewModel(mDisposables, mRxBluetooth, mSchedulerProvider);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
