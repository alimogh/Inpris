package com.rany.albeg.wein.inpris.write;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.github.ivbaranov.rxbluetooth.BluetoothConnection;
import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.rany.albeg.wein.inpris.common.BaseViewModel;
import com.rany.albeg.wein.inpris.common.HTMLPlaceHolder;
import com.rany.albeg.wein.inpris.data.DiscoveryResult;
import com.rany.albeg.wein.inpris.schedulers.SchedulerProvider;

import java.util.UUID;

import io.reactivex.disposables.CompositeDisposable;

import static com.rany.albeg.wein.inpris.data.DiscoveryResult.*;
import static com.rany.albeg.wein.inpris.data.DiscoveryResult.loading;

/**
 * This file is a part of Inpris project.
 *
 * @author Rany Albeg Wein
 * @version 1.0.0
 * @since 03/06/2018
 */
public class WriteViewModel extends BaseViewModel {
    private static final String TAG = "WriteViewModel";

    private final RxBluetooth mRxBluetooth;
    private SchedulerProvider mSchedulerProvider;
    private final String mHtmlTemplate;
    private final String mBase64Image;
    private MutableLiveData<DiscoveryResult> mDiscovery;
    private BluetoothConnection mBluetoothConnection;

    WriteViewModel(CompositeDisposable disposables, RxBluetooth rxBluetooth,
            SchedulerProvider schedulerProvider, String htmlTemplate, String base64Image) {
        super(disposables);
        mRxBluetooth = rxBluetooth;
        mSchedulerProvider = schedulerProvider;
        mHtmlTemplate = htmlTemplate;
        mBase64Image = base64Image;
    }

    public LiveData<DiscoveryResult> discovery() {
        if (mDiscovery == null) {
            mDiscovery = new MutableLiveData<>();
        }
        return mDiscovery;
    }

    public void discoverDevices() {
        addDisposable(mRxBluetooth.observeDevices()
                                  .distinct()
                                  .subscribeOn(mSchedulerProvider.io())
                                  .observeOn(mSchedulerProvider.ui())
                                  .doOnSubscribe(__ -> mDiscovery.setValue(loading()))
                                  .doAfterTerminate(() -> mDiscovery.setValue(complete()))
                                  .subscribe(btDevice -> mDiscovery.setValue(success(btDevice)),
                                          throwable -> mDiscovery.setValue(error(throwable)))

        );

        mRxBluetooth.startDiscovery();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        cancelDiscoveryIfNeeded();
        if (mBluetoothConnection != null) {
            mBluetoothConnection.closeConnection();
        }
    }

    public void cancelDiscoveryIfNeeded() {
        if (mRxBluetooth.isDiscovering()) {
            mRxBluetooth.cancelDiscovery();
        }
        mDiscovery.setValue(complete());
    }

    public void connect(BluetoothDevice bluetoothDevice) {
        addDisposable(mRxBluetooth
                .observeConnectDevice(bluetoothDevice, UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(this::write, throwable -> Log.e(TAG, "Device connection error:", throwable))
        );
    }

    private void write(BluetoothSocket bluetoothSocket) throws Exception {
        mBluetoothConnection = new BluetoothConnection(bluetoothSocket);
        mBluetoothConnection.send(mHtmlTemplate.replace(HTMLPlaceHolder.LOGO_BASE64, mBase64Image));
    }
}
