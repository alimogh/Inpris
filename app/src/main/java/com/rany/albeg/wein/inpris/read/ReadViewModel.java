package com.rany.albeg.wein.inpris.read;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.github.davidmoten.rx2.Strings;
import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.rany.albeg.wein.inpris.common.BaseViewModel;
import com.rany.albeg.wein.inpris.data.DataTransferResult;
import com.rany.albeg.wein.inpris.schedulers.SchedulerProvider;

import java.util.UUID;

import io.reactivex.disposables.CompositeDisposable;

import static com.rany.albeg.wein.inpris.data.DataTransferResult.*;

/**
 * This file is a part of Inpris project.
 *
 * @author Rany Albeg Wein
 * @version 1.0.0
 * @since 03/06/2018
 */
public class ReadViewModel extends BaseViewModel {
    private static final String TAG = "ReadViewModel";

    private final RxBluetooth mRxBluetooth;
    private final SchedulerProvider mSchedulerProvider;
    private MutableLiveData<DataTransferResult> mIncomingMessage;


    ReadViewModel(CompositeDisposable disposables, RxBluetooth rxBluetooth,
            SchedulerProvider schedulerProvider) {
        super(disposables);
        mRxBluetooth = rxBluetooth;
        mSchedulerProvider = schedulerProvider;
    }

    public LiveData<DataTransferResult> incomingMessage() {
        if (mIncomingMessage == null) {
            mIncomingMessage = new MutableLiveData<>();
        }
        return mIncomingMessage;
    }

    public void listen() {
        addDisposable(mRxBluetooth
                .observeBluetoothSocket("InprisConnection", UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(this::observeIncomingMessages,
                        throwable -> Log.e(TAG, "Unable to open socket:", throwable))
        );
    }

    private void observeIncomingMessages(BluetoothSocket btSocket) throws Exception {
        StringBuilder builder = new StringBuilder();
        addDisposable(Strings.from(btSocket.getInputStream())
                             .subscribeOn(mSchedulerProvider.io())
                             .observeOn(mSchedulerProvider.ui())
                             .subscribe(data -> {
                                         builder.append(data);
                                         if (data.endsWith("</HTML>")) {
                                             mIncomingMessage.setValue(success(builder.toString()));
                                         }
                                     },
                                     throwable -> mIncomingMessage.setValue(failure(throwable)))
        );
    }
}
