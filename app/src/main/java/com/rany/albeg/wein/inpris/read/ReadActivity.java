package com.rany.albeg.wein.inpris.read;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.rany.albeg.wein.inpris.R;
import com.rany.albeg.wein.inpris.data.DataTransferResult;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class ReadActivity extends AppCompatActivity {
    private static final String TAG = "ReadActivity";

    @BindView(R.id.wv)
    WebView mWebView;

    @Inject
    ReadViewModelFactory mReadViewModelFactory;

    private ReadViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mViewModel = ViewModelProviders.of(this, mReadViewModelFactory).get(ReadViewModel.class);
        observe();
    }

    private void observe() {
        mViewModel.incomingMessage().observe(this, this::renderIncomingMessage);
    }

    private void renderIncomingMessage(DataTransferResult dataTransferResult) {
        switch (dataTransferResult.status) {
            case DataTransferResult.SUCCESS:
                success(dataTransferResult.data);
                break;
            case DataTransferResult.FAILURE:
                failure(dataTransferResult.error);
                break;
        }
    }

    private void failure(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void success(String data) {
        String mimeType = "text/html";
        String encoding = "utf-8";
        mWebView.loadData(data, mimeType, encoding);
    }

    @OnClick(R.id.bt_listen)
    public void onClickListen() {
        mViewModel.listen();
    }
}
