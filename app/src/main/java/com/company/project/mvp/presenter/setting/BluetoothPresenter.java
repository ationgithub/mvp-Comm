package com.company.project.mvp.presenter.setting;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import com.company.project.BaseApplication;
import com.company.project.common.Constants;
import com.company.project.mvp.contract.setting.BluetoothContract;
import com.company.project.mvp.presenter.base.BasePresenter;
import com.company.project.utils.SharedPreferencesUtils;
import com.company.project.widget.bluetooth.BluetoothListener;
import com.company.project.widget.bluetooth.BluetoothManager;
import com.socks.library.KLog;
import java.util.List;

import com.company.project.BaseApplication;
import com.company.project.mvp.contract.setting.BluetoothContract;
import com.company.project.mvp.presenter.base.BasePresenter;
import com.company.project.widget.bluetooth.BluetoothListener;
import com.company.project.widget.bluetooth.BluetoothManager;

public class BluetoothPresenter extends BasePresenter<BluetoothContract.View> implements BluetoothContract.Presenter {
    private static final String TAG = BluetoothPresenter.class.getSimpleName();
    private BluetoothManager mBluetoothManager;

    public BluetoothPresenter(BluetoothContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        mBluetoothManager = BluetoothManager.newInstance(BaseApplication.mContext);
        mBluetoothManager.open();//开启蓝牙
        mBluetoothManager.setListener(mListener);
    }

    @Override
    public void disconnect() {
        mBluetoothManager.disconnect();
    }

    @Override
    public void startScan() {
        mBluetoothManager.startScan();
    }

    @Override
    public void connect(String address) {
        mBluetoothManager.connect(address);
    }

    @Override
    public void connectPaired(Activity mActivity) {
        mBluetoothManager.connectPaired(mActivity);
    }

    @Override
    public void sendData(byte[] data) {
        mBluetoothManager.sendData(data);
    }

    private BluetoothListener mListener = new BluetoothListener() {
        @Override
        public void onBluetoothNotSupported() {
            getView().setDialog("未找到蓝牙设备");
        }

        @Override
        public void onBluetoothNotEnabled() {
//            DialogHelper.successSnackbar(viewGroup, "蓝牙未打开，请打开手机蓝牙", DialogHelper.APPEAR_FROM_TOP_TO_DOWN);
        }

        @Override
        public void onConnecting(BluetoothDevice device) {
            getView().onConnecting(device);
        }

        @Override
        public void onConnected(BluetoothDevice device) {
            KLog.e("已经连接：" + device.getName());
            SharedPreferencesUtils.put(BaseApplication.mContext, Constants.BLUETOOTH_ADDRESS, device.getAddress());
            getView().onConnected(device);
        }

        @Override
        public void onDisconnected() {
            if (isViewAttached()) {
                getView().onDisconnected();
            }
        }

        @Override
        public void onConnectionFailed(BluetoothDevice device) {
            getView().setDialog("蓝牙连接失败");
        }

        @Override
        public void onDiscoveryStarted() {
            getView().onDiscoveryStarted();
        }

        @Override
        public void onDiscoveryFinished() {
            getView().onDiscoveryFinished();
        }

        @Override
        public void onNoDevicesFound() {
            getView().setDialog("未发现蓝牙设备");
        }

        @Override
        public void onDevicesFound(List<BluetoothDevice> deviceList) {
            getView().onDevicesFound(deviceList);
        }

        @Override
        public void onDataReceived(String str) {
            getView().onDataReceived(str);
            KLog.e("currentThreadName::" + Thread.currentThread().getName());
        }
    };

    @Override
    public void detachView() {
//        mBluetoothManager.onDestroy();
        mBluetoothManager = null;
        mListener = null;
        KLog.e("mBluetoothManager被销毁");
        super.detachView();
    }
}
