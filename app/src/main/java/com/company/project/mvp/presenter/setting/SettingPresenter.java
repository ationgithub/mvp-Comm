package com.company.project.mvp.presenter.setting;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import com.company.project.BaseApplication;
import com.company.project.common.Constants;
import com.company.project.mvp.contract.setting.SettingContract;
import com.company.project.mvp.presenter.base.BasePresenter;
import com.company.project.utils.SharedPreferencesUtils;
import com.company.project.widget.bluetooth.BluetoothListener;
import com.company.project.widget.bluetooth.BluetoothManager;
import com.company.project.widget.bluetooth.IBluetooth;
import com.company.project.widget.bluetooth.classic.ClassicBluetooth;
import com.company.project.widget.bluetooth.le.LeBluetooth;
import com.socks.library.KLog;

import java.util.List;

import com.company.project.BaseApplication;
import com.company.project.mvp.contract.setting.SettingContract;
import com.company.project.mvp.presenter.base.BasePresenter;
import com.company.project.widget.bluetooth.BluetoothListener;
import com.company.project.widget.bluetooth.BluetoothManager;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class SettingPresenter extends BasePresenter<SettingContract.View> implements SettingContract.Presenter {
    private static final String TAG = SettingPresenter.class.getSimpleName();
    private BluetoothManager mBluetoothManager;

    public SettingPresenter(SettingContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        mBluetoothManager = BluetoothManager.newInstance(BaseApplication.mContext);
        mBluetoothManager.open();
        int intBluetoothType = (int) SharedPreferencesUtils.get(BaseApplication.mContext, Constants.BLUETOOTH_TYPE, 0);
        if (intBluetoothType == Constants.BLUETOOTH_CLASSIC) {
            getView().checkClassicBluetooth();
            mBluetoothManager.switch2Classic();
        } else if (intBluetoothType == Constants.BLUETOOTH_LE) {
            getView().checkLeBluetooth();
            mBluetoothManager.switch2Le();
        } else {
            getView().checkClassicBluetooth();
            mBluetoothManager.switch2Classic();
        }
        mBluetoothManager.setListener(mListener);
    }

    @Override
    public void switch2Classic() {
        SharedPreferencesUtils.put(BaseApplication.mContext, Constants.BLUETOOTH_TYPE, Constants.BLUETOOTH_CLASSIC);
        mBluetoothManager.switch2Classic();
        mBluetoothManager.setListener(mListener);
    }

    @Override
    public void switch2Le() {
        SharedPreferencesUtils.put(BaseApplication.mContext, Constants.BLUETOOTH_TYPE, Constants.BLUETOOTH_LE);
        mBluetoothManager.switch2Le();
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
                getView().setDialog("蓝牙连接已断开");
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
