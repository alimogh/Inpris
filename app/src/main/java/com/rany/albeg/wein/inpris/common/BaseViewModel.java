package com.rany.albeg.wein.inpris.common;

import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * This file is a part of Inpris project.
 *
 * @author Rany Albeg Wein
 * @version 1.0.0
 * @since 03/06/2018
 */
public abstract class BaseViewModel extends ViewModel {

    private final CompositeDisposable mDisposables;

    protected BaseViewModel(CompositeDisposable disposables) {
        mDisposables = disposables;
    }

    protected void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        mDisposables.clear();
    }
}