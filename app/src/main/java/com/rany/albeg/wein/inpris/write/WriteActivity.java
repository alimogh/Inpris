package com.rany.albeg.wein.inpris.write;

import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rany.albeg.wein.inpris.R;
import com.rany.albeg.wein.inpris.data.DiscoveryResult;
import com.rany.albeg.wein.inpris.read.ReadActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import dagger.android.AndroidInjection;

public class WriteActivity extends AppCompatActivity {
    private static final String TAG = "WriteActivity";

    @BindView(R.id.pb_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.lv_discovered_devices)
    ListView mListDiscoveredDevices;

    @Inject
    WriteViewModelFactory mWriteViewModelFactory;
    private WriteViewModel mViewModel;
    private ArrayAdapter<BluetoothDevice> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        mListDiscoveredDevices.setAdapter(mAdapter);

        mViewModel = ViewModelProviders.of(this, mWriteViewModelFactory).get(WriteViewModel.class);
        observe();
    }

    private void observe() {
        mViewModel.discovery().observe(this, this::processDiscoverResult);
    }

    private void processDiscoverResult(DiscoveryResult discoveryResult) {
        switch (discoveryResult.status) {
            case DiscoveryResult.SUCCESS:
                success(discoveryResult.data);
                break;
            case DiscoveryResult.COMPLETE:
                complete();
                break;
            case DiscoveryResult.ERROR:
                error(discoveryResult.error);
                break;
            case DiscoveryResult.LOADING:
                loading();
                break;
        }
    }

    private void success(BluetoothDevice bluetoothDevice) {
        mAdapter.add(bluetoothDevice);
    }

    private void loading() {
        mProgressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, R.string.discovery_started, Toast.LENGTH_SHORT).show();
    }

    private void error(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void complete() {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Completed discovery", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bt_discover)
    public void onDiscoverClicked() {
        mAdapter.clear();
        mViewModel.discoverDevices();
    }

    @OnClick(R.id.bt_cancel_discover)
    public void onCancelDiscoveryClicked() {
        mViewModel.cancelDiscoveryIfNeeded();
    }

    @OnItemClick(R.id.lv_discovered_devices)
    public void onBluetoothDeviceClicked(int position) {
        mViewModel.connect(mAdapter.getItem(position));
    }

    @OnClick(R.id.bt_enter_read_screen)
    public void onClickEnterReadScreen() {
        startActivity(new Intent(this, ReadActivity.class));
    }
}
