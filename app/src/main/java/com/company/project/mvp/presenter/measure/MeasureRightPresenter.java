package com.company.project.mvp.presenter.measure;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import com.company.project.BaseApplication;
import com.company.project.common.AudioPlayer;
import com.company.project.common.Constants;
import com.company.project.mvp.contract.measure.MeasureContract;
import com.company.project.mvp.model.entity.db.CezhanData;
import com.company.project.mvp.model.entity.db.JidianData;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;
import com.company.project.mvp.presenter.base.BasePresenter;
import com.company.project.utils.SharedPreferencesUtils;
import com.company.project.widget.bluetooth.BluetoothListener;
import com.company.project.widget.bluetooth.BluetoothManager;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import com.company.project.BaseApplication;
import com.company.project.mvp.contract.measure.MeasureContract;
import com.company.project.mvp.model.entity.db.CezhanData;
import com.company.project.mvp.model.entity.db.JidianData;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;
import com.company.project.mvp.presenter.base.BasePresenter;
import com.company.project.widget.bluetooth.BluetoothListener;
import com.company.project.widget.bluetooth.BluetoothManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class MeasureRightPresenter extends BasePresenter<MeasureContract.View> implements MeasureContract.Presenter {
    private static final String TAG = MeasureRightPresenter.class.getSimpleName();
    private BluetoothManager mBluetoothManager;
//    PingCha instance;
    public boolean isSaved;

    public MeasureRightPresenter(MeasureContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        mBluetoothManager = BluetoothManager.newInstance(BaseApplication.mContext);
        mBluetoothManager.setListener(mListener);
        if (!mBluetoothManager.isConnected()) {
            mBluetoothManager.connectDefault();
        }
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

    @Override
    public void requestJidianData() {
        mRxManager.add(Observable.create(new Observable.OnSubscribe<List<JidianData>>() {
                    @Override
                    public void call(Subscriber<? super List<JidianData>> subscriber) {
                        List<JidianData> mJidianData = null;
                        try {
                            //借用一下这个异步线程初始化SoundPool，因为有检测到这个操作耗时。
                            AudioPlayer.init(BaseApplication.mContext);

                            mJidianData = DataSupport.findAll(JidianData.class);
                            subscriber.onNext(mJidianData);

                        } catch (Exception ex) {
                            subscriber.onError(ex);
                        }
                    }
                })
                        .map(new Func1<List<JidianData>, List<String>>() {
                            @Override
                            public List<String> call(List<JidianData> jidianDatas) {
                                List<String> listJidianBianhao = new ArrayList<String>();
                                for (JidianData jidianData : jidianDatas) {
                                    listJidianBianhao.add(jidianData.getBianhao());
                                }

                                return listJidianBianhao;
                            }
                        })
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<List<String>>() {
                            @Override
                            public void _onNext(List<String> strings) {
                                getView().responseJidianData(strings);
                            }
                        })
        );
    }

    @Override
    public void requestCezhanData(final YusheshuizhunxianData mYusheshuizhunxianData){
        mRxManager.add(Observable.create(new Observable.OnSubscribe<List<CezhanData>>() {
                    @Override
                    public void call(Subscriber<? super List<CezhanData>> subscriber) {
                        //生成测站并存到数据库中这种操作应考虑放到编辑水准线，保存的那里。后面点击测量的时候应该读取数据库，而产生数据后应该是修改相应测站的行。
                        try {

                            ShuizhunxianData mShuizhunxianData = DataSupport.where("yusheshuizhunxianID = ? and chuangjianshijian = ? "
                                    , String.valueOf(mYusheshuizhunxianData.getId()), mYusheshuizhunxianData.getXiugaishijian())
                                    .findFirst(ShuizhunxianData.class);

                            List<CezhanData> listCezhan = DataSupport.where("shuizhunxianID = ? ", String.valueOf(mShuizhunxianData.getId()))
                                    .order("number").find(CezhanData.class);
                            subscriber.onNext(listCezhan);
                            KLog.e(TAG,"-----------requestCezhanData-----------");
                        } catch (Exception ex) {
                            subscriber.onError(ex);
                        }
                    }
                }).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<List<CezhanData>>() {
                            @Override
                            public void _onNext(List<CezhanData> mCezhanData) {
                                getView().responseCezhanData(mCezhanData);
                            }
                        })
        );
    }

    //通过rxjava异步做平差操作，减少主界面的卡顿，然后在view层调用
    public void requestResultData(final List<CezhanData> mCezhanData){
        mRxManager.add(Observable.create(new Observable.OnSubscribe<List<CezhanData> >() {

            @Override
            public void call(Subscriber<? super List<CezhanData>> subscriber) {
//                if(null==instance){
//                    instance= new PingCha();
//                }
//                KLog.e(TAG,"==========0000000000000===========");
//                instance.initGaocha(mCezhanData);
//                instance.initData(mCezhanData);
                isSaved=true;
                subscriber.onNext(mCezhanData);
                KLog.e(TAG,"============111111111111111========");
            }

        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<CezhanData>>() {
                    @Override
                    public void _onNext(List<CezhanData> cezhanDatas) {
                        if(isSaved){
                            getView().saveResult();
                            KLog.e(TAG,"=================222222222222222=================");
                        }else{
                            getView().setDialog("成果数据保存失败");
                            KLog.e(TAG,"===============3333333333333333==============");
                        }
                    }
                })
        );
    }

    /**
     * ********************蓝牙所有状态的回调都在这里*********************************
     */
    private BluetoothListener mListener = new BluetoothListener() {
        @Override
        public void onBluetoothNotSupported() {
            if (isViewAttached()) {
                getView().setDialog("未找到蓝牙设备");
            }
        }

        @Override
        public void onBluetoothNotEnabled() {
        }

        @Override
        public void onConnecting(BluetoothDevice device) {
            if (isViewAttached()) {
                getView().onConnecting(device);
            }
        }

        @Override
        public void onConnected(BluetoothDevice device) {
            KLog.e("已经连接：" + device.getName());
            SharedPreferencesUtils.put(BaseApplication.mContext, Constants.BLUETOOTH_ADDRESS, device.getAddress());
            if (isViewAttached()) {
                getView().onConnected(device);
            }
            AudioPlayer.play(Constants.AUDIO_CONNECT);
        }

        @Override
        public void onDisconnected() {
            if (isViewAttached()) {
                getView().onDisconnected();
                AudioPlayer.play(Constants.AUDIO_DISCONNECT);
            }
        }

        @Override
        public void onConnectionFailed(BluetoothDevice device) {
            if (isViewAttached()) {
                getView().setDialog("蓝牙连接失败");
            }
        }

        @Override
        public void onDiscoveryStarted() {
            if (isViewAttached()) {
                getView().onDiscoveryStarted();
            }
        }

        @Override
        public void onDiscoveryFinished() {
            if (isViewAttached()) {
                getView().onDiscoveryFinished();
            }
        }

        @Override
        public void onNoDevicesFound() {
            if (isViewAttached()) {
                getView().setDialog("未发现蓝牙设备");
            }
        }

        @Override
        public void onDevicesFound(List<BluetoothDevice> deviceList) {
            if (isViewAttached()) {
                getView().onDevicesFound(deviceList);
            }
        }

        @Override
        public void onDataReceived(String str) {
            if (isViewAttached()) {
                getView().onDataReceived(str);
            }
            KLog.e("currentThreadName::" + Thread.currentThread().getName());
        }
    };

    @Override
    public void detachView() {
        mBluetoothManager.onDestroy();
        mBluetoothManager = null;
        mListener = null;
        super.detachView();
    }
}
