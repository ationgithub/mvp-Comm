package com.company.project.mvp.presenter;

import com.company.project.mvp.contract.MainContract;
import com.company.project.mvp.presenter.base.BasePresenter;



/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();


    public MainPresenter(MainContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
    }

//    @Override
//    public void requestGongdianData() {
//        mRxManager.add(Observable.create(new Observable.OnSubscribe<List<GongdianData>>() {
//            @Override
//            public void call(Subscriber<? super List<GongdianData>> subscriber) {
//                try {
//                    List<GongdianData> mGongdianData = DataSupport.findAll(GongdianData.class);
//
//                    KLog.e("mGongdianData::" + mGongdianData.size());
//                    subscriber.onNext(mGongdianData);
//                } catch (Exception ex) {
//                    subscriber.onError(ex);
//                }
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<GongdianData>>() {
//
//                               @Override
//                               public void onStart() {
//                                   super.onStart();
//                                   KLog.e("onStart");
//                                   getView().showLoading();
//                               }
//
//                               @Override
//                               public void onError(Throwable e) {
//
//                               }
//
//                               @Override
//                               public void onNext(List<GongdianData> gongdianDatas) {
//                                   getView().responseGongdianData(gongdianDatas);
//                               }
//
//                               @Override
//                               public void onCompleted() {
//
//                               }
//                           }
//                ));
//    }

//    @Override
//    public void requestShuizhunxianData(final int pagination, final String strGongdianParam, final String strStatusParam, final String strTypeParam) {
//        //这一部分是用于过滤状态参数和时间参数的
//        mRxManager.add(Observable.create(new Observable.OnSubscribe<List<YusheshuizhunxianData>>() {
//            @Override
//            public void call(Subscriber<? super List<YusheshuizhunxianData>> subscriber) {
//                List<YusheshuizhunxianData> mShuizhunxianData = null;
//                try {
//                    if (strStatusParam.equals("全部")) {
//
//                        if (strTypeParam.equals("全部")) {
//                            mShuizhunxianData = DataSupport.order("id").limit(Constants.PAGE_SIZE)
//                                    .offset(pagination * Constants.PAGE_SIZE)
//                                    .find(YusheshuizhunxianData.class);
//
//                        } else if (strTypeParam.equals("近一周")) {
//                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            Calendar mCalendar = Calendar.getInstance();
//                            String strEndTime = df.format(mCalendar.getTime());
//                            mCalendar.add(Calendar.DAY_OF_MONTH, -7);
//                            String strStartTime = df.format(mCalendar.getTime());
//
//                            mShuizhunxianData = DataSupport.where("xiugaishijian Between ? and ?", strStartTime, strEndTime)
//                                    .order("id").limit(Constants.PAGE_SIZE)
//                                    .offset(pagination * Constants.PAGE_SIZE)
//                                    .find(YusheshuizhunxianData.class);
//
//                        } else if (strTypeParam.equals("近一月")) {
//                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            Calendar mCalendar = Calendar.getInstance();
//                            String strEndTime = df.format(mCalendar.getTime());
//                            mCalendar.add(Calendar.MONTH, -1);
//                            String strStartTime = df.format(mCalendar.getTime());
//
//                            mShuizhunxianData = DataSupport.where("xiugaishijian Between ? and ?", strStartTime, strEndTime)
//                                    .order("id").limit(Constants.PAGE_SIZE)
//                                    .offset(pagination * Constants.PAGE_SIZE)
//                                    .find(YusheshuizhunxianData.class);
//                        }
//                    } else {
//                        if (strTypeParam.equals("全部")) {
//                            mShuizhunxianData = DataSupport.where("status = ?", strStatusParam)
//                                    .order("id").limit(Constants.PAGE_SIZE)
//                                    .offset(pagination * Constants.PAGE_SIZE)
//                                    .find(YusheshuizhunxianData.class);
//                        } else if (strTypeParam.equals("近一周")) {
//                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            Calendar mCalendar = Calendar.getInstance();
//                            String strEndTime = df.format(mCalendar.getTime());
//                            mCalendar.add(Calendar.DAY_OF_MONTH, -7);
//                            String strStartTime = df.format(mCalendar.getTime());
//
//                            mShuizhunxianData = DataSupport.where("status = ? and xiugaishijian Between ? and ?", strStatusParam, strStartTime, strEndTime)
//                                    .order("id").limit(Constants.PAGE_SIZE)
//                                    .offset(pagination * Constants.PAGE_SIZE)
//                                    .find(YusheshuizhunxianData.class);
//
//                        } else if (strTypeParam.equals("近一月")) {
//                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            Calendar mCalendar = Calendar.getInstance();
//                            String strEndTime = df.format(mCalendar.getTime());
//                            mCalendar.add(Calendar.MONTH, -1);
//                            String strStartTime = df.format(mCalendar.getTime());
//
//                            mShuizhunxianData = DataSupport.where("status = ? and xiugaishijian Between ? and ?", strStatusParam, strStartTime, strEndTime)
//                                    .order("id").limit(Constants.PAGE_SIZE)
//                                    .offset(pagination * Constants.PAGE_SIZE)
//                                    .find(YusheshuizhunxianData.class);
//                        }
//                    }
//
//                    subscriber.onNext(mShuizhunxianData);
//                } catch (Exception ex) {
//                    subscriber.onError(ex);
//                }
//            }
//        }).subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<YusheshuizhunxianData>>() {
//                               @Override
//                               public void call(List<YusheshuizhunxianData> yusheshuizhunxianDatas) {
//                                   //这里才开始过滤工点
//                                   if (strGongdianParam.equals("全部")) {
//                                       getView().responseShuizhunxianData(yusheshuizhunxianDatas, pagination);
//                                   } else {
//                                       filterAsGongdian(pagination, strGongdianParam, yusheshuizhunxianDatas);
//                                   }
//                               }
//                           }
//                ));
//    }
//
//    public void filterAsGongdian(final int pagination, final String strGongdianParam, List<YusheshuizhunxianData> yusheshuizhunxianDatas) {
//        final List<YusheshuizhunxianData> filteredShuizhunxianData = new ArrayList<YusheshuizhunxianData>();
//
//        Observable.from(yusheshuizhunxianDatas).filter(new Func1<YusheshuizhunxianData, Boolean>() {
//            @Override
//            public Boolean call(YusheshuizhunxianData yusheshuizhunxianData) {
//                String[] arrayCedian = yusheshuizhunxianData.getXianluxinxi().split(",");
//
//                if (arrayCedian.length == 0) {
//                    return false;
//                }
//
//                CedianData mCedianData = DataSupport.where("bianhao = ?", arrayCedian[1]).findFirst(CedianData.class);
////                CedianData mCedianData = DataSupport.where("bianhao = ?", "cd18").findFirst(CedianData.class);
//
//                if (mCedianData == null) {
//                    return false;
//                }
//
//                DuanmianData mDuanmianData = DataSupport.where("dmid = ?", mCedianData.getDuanmianid()).findFirst(DuanmianData.class);
//
//                if (mDuanmianData == null) {
//                    return false;
//                }
//                GongdianData mGongdianData = DataSupport.where("gdid = ?", mDuanmianData.getGongdianid()).findFirst(GongdianData.class);
//
//                if (mGongdianData == null) {
//                    return false;
//                }
//
//                KLog.e(mGongdianData.getGdmc() + "::::::::::::" + strGongdianParam);
//                return mGongdianData.getGdmc().equals(strGongdianParam);
//            }
//        }).subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<YusheshuizhunxianData>() {
//
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                        KLog.e("onStart");
//                        getView().showLoading();
//                    }
//
//                    @Override
//                    public void onNext(YusheshuizhunxianData yusheshuizhunxianData) {
//                        filteredShuizhunxianData.add(yusheshuizhunxianData);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        KLog.e("filteredShuizhunxianData.size()::" + filteredShuizhunxianData.size());
//                        KLog.e("pagination::" + pagination);
//                        getView().responseShuizhunxianData(filteredShuizhunxianData, pagination);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        //此处不考虑错误类型，笼统的以错误来介绍
//                        KLog.e("onError::" + e);
//                        getView().showError(e);
//                    }
//                });
//    }

}
