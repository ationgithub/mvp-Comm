package com.company.project.mvp.presenter;

import com.company.project.common.Constants;
import com.company.project.mvp.contract.ShuizhunxianContract;
import com.company.project.mvp.model.entity.db.CezhanData;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;
import com.company.project.mvp.model.entity.db.StaffData;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;
import com.company.project.mvp.presenter.base.BasePresenter;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import com.company.project.mvp.contract.ShuizhunxianContract;
import com.company.project.mvp.model.entity.db.CezhanData;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;
import com.company.project.mvp.model.entity.db.StaffData;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author：leguang on 2016/10/14 0014 13:17
 * Email：langmanleguang@qq.com
 */
public class EditShuizhunxianPresenter extends BasePresenter<ShuizhunxianContract.View> implements ShuizhunxianContract.Presenter {
    private static final String TAG = EditShuizhunxianPresenter.class.getSimpleName();

    public EditShuizhunxianPresenter(ShuizhunxianContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        requestStaffData();
    }

    @Override
    public void requestStaffData() {
        mRxManager.add(Observable.create(new Observable.OnSubscribe<List<String>>() {
                    @Override
                    public void call(Subscriber<? super List<String>> subscriber) {
                        List<StaffData> mStaffData = null;
                        List<String> listStaffName = null;
                        try {
                            mStaffData = DataSupport.findAll(StaffData.class);
                            if (mStaffData.size() > 0) {
                                listStaffName = new ArrayList<String>();
                                for (StaffData staffData : mStaffData) {
                                    listStaffName.add(staffData.getUserName());
                                }
                            }
                            subscriber.onNext(listStaffName);
                        } catch (Exception ex) {
                            subscriber.onError(ex);
                            getView().showError(ex);
                        }
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscriber<List<String>>() {
                            @Override
                            public void _onNext(List<String> staffDatas) {
                                getView().responseStaffData(staffDatas);
                            }
                        })
        );
    }

    @Override
    public void save(final YusheshuizhunxianData mYusheshuizhunxianData, final String dateTime) {
        if (mYusheshuizhunxianData == null) {
            return;
        }
        mRxManager.add(Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        try {
                            ShuizhunxianData mShuizhunxianData;
                            if (mYusheshuizhunxianData.getStatus().equals(Constants.status_daibianji)) {

                                //****************以下是生成测量这一次水准线的数据信息***************************
                                mYusheshuizhunxianData.setXiugaishijian(dateTime);
                                mShuizhunxianData = new ShuizhunxianData();
                                mShuizhunxianData.setChuangjianshijian(dateTime);
                                mShuizhunxianData.save();

                                //****************以下是生成测站的数据信息***************************
                                String[] arrayJidianAndCedian = mYusheshuizhunxianData.getXianluxinxi().split(",");
                                int intNumber = 0;
                                List<CezhanData> listCezhan = new ArrayList<>();
                                //生成往测数据
                                for (int i = 0; i < arrayJidianAndCedian.length - 1; i++) {
                                    intNumber++;
                                    CezhanData mCezhanData = new CezhanData();
                                    mCezhanData.setNumber(intNumber);
                                    mCezhanData.setShuizhunxianID(mShuizhunxianData.getId());
                                    mCezhanData.setMeasureDirection(Constants.wangce);
                                    //+++++新加的代码，往测时，当路线观测类型为"往:aBFFB 返:aFBBF","往:aBFFB 返:aBFFB","aBFFB"时,测站的观测顺序
                                    if(mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE1)||mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE2)||mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE3)){
                                        if (intNumber % 2 == 0) {
                                            //设置测站的观测类型
                                            KLog.e(TAG,"--------wangce-----555555--------------");
                                            mCezhanData.setObserveType(Constants.FBBF);
                                        } else {
                                            KLog.e(TAG,"----------wangce-----666666--------------");
                                            mCezhanData.setObserveType(Constants.BFFB);
                                        }
                                    }else if(mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE4)){
                                        mCezhanData.setObserveType(Constants.BBFF);
                                    }

                                    mCezhanData.setQianshi(arrayJidianAndCedian[i + 1]);
                                    mCezhanData.setHoushi(arrayJidianAndCedian[i]);
                                    listCezhan.add(mCezhanData);
                                }

                                if(mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE1)||mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE2)){
                                    //生成返测数据
                                    for (int i = arrayJidianAndCedian.length - 1; i > 0; i--) {
                                        intNumber++;
                                        CezhanData mCezhanData = new CezhanData();
                                        mCezhanData.setNumber(intNumber);
                                        mCezhanData.setShuizhunxianID(mShuizhunxianData.getId());
                                        mCezhanData.setMeasureDirection(Constants.fance);
                                        //----新加的代码，返测时,当路线观测类型为"往:aBFFB 返:aFBBF",测站的观测顺序
                                        if(mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE1)){
                                            if (intNumber % 2 == 0) {
                                                KLog.e(TAG,"----------fance---11111111--------------");
                                                mCezhanData.setObserveType(Constants.BFFB);
                                            } else {
                                                KLog.e(TAG,"--------fance-----22222222--------------");
                                                mCezhanData.setObserveType(Constants.FBBF);
                                            }
                                        }else if(mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE2)){
                                            if (intNumber % 2 == 0) {
                                                KLog.e(TAG,"----------fance------33333333--------------");
                                                mCezhanData.setObserveType(Constants.FBBF);
                                            } else {
                                                KLog.e(TAG,"--------fance-----44444444--------------");
                                                mCezhanData.setObserveType(Constants.BFFB);
                                            }
                                        }

                                        mCezhanData.setQianshi(arrayJidianAndCedian[i - 1]);
                                        mCezhanData.setHoushi(arrayJidianAndCedian[i]);
                                        listCezhan.add(mCezhanData);
                                    }
                                }
                                DataSupport.deleteAll(CezhanData.class, "shuizhunxianID = ? ", String.valueOf(mShuizhunxianData.getId()));
                                DataSupport.saveAll(listCezhan);

                            } else {
                                mShuizhunxianData = DataSupport.where("yusheshuizhunxianID = ? and chuangjianshijian = ? "
                                        , String.valueOf(mYusheshuizhunxianData.getId()), mYusheshuizhunxianData.getXiugaishijian())
                                        .findFirst(ShuizhunxianData.class);
                            }

                            if (mShuizhunxianData != null) {
                                KLog.e(TAG,"----------------------save-----------------");
                                mShuizhunxianData.setYusheshuizhunxianID(mYusheshuizhunxianData.getId());
                                mShuizhunxianData.setBiaoshi(mYusheshuizhunxianData.getBiaoshi());
                                mShuizhunxianData.setYsszxid(mYusheshuizhunxianData.getYsszxid());
                                mShuizhunxianData.setXianlubianhao(mYusheshuizhunxianData.getXianlubianhao());
                                mShuizhunxianData.setCedianshu(mYusheshuizhunxianData.getCedianshu());
                                mShuizhunxianData.setLeixing(mYusheshuizhunxianData.getLeixing());
                                mShuizhunxianData.setShezhiren(mYusheshuizhunxianData.getShezhiren());
                                mShuizhunxianData.setXianlumingcheng(mYusheshuizhunxianData.getXianlumingcheng());
                                mShuizhunxianData.setDepartId(mYusheshuizhunxianData.getDepartId());
                                mShuizhunxianData.setJidianshu(mYusheshuizhunxianData.getJidianshu());
                                mShuizhunxianData.setXianluxinxi(mYusheshuizhunxianData.getXianluxinxi());
                                mShuizhunxianData.setRouteType(mYusheshuizhunxianData.getRouteType());
                                mShuizhunxianData.setObserveType(mYusheshuizhunxianData.getObserveType());
                                KLog.e(TAG,"mShuizhunxianData=:"+mShuizhunxianData.getCedianshu());
                                KLog.e(TAG,"mShuizhunxianData.getObserveType()=:"+mShuizhunxianData.getObserveType());
                                mShuizhunxianData.setWeather(mYusheshuizhunxianData.getWeather());
                                mShuizhunxianData.setPressure(mYusheshuizhunxianData.getPressure());
                                mShuizhunxianData.setTemperature(mYusheshuizhunxianData.getTemperature());
                                mShuizhunxianData.setStaff(mYusheshuizhunxianData.getStaff());
                                mShuizhunxianData.setXiugaishijian(dateTime);
                                mShuizhunxianData.save();
                            }

                            mYusheshuizhunxianData.setStatus(Constants.status_daiceliang);
                            int rowsAffected = mYusheshuizhunxianData.update(mYusheshuizhunxianData.getId());

                            subscriber.onNext(rowsAffected);

                        } catch (Exception ex) {
                            subscriber.onError(ex);
                        }
                    }
                }).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<Integer>() {
                            @Override
                            public void _onNext(Integer rowsAffected) {
                                if (isViewAttached()) {
                                    getView().responseSave(rowsAffected);
                                }
                            }
                        })
        );
    }
}
