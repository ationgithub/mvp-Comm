package com.company.project.mvp.presenter.upload;

import com.company.project.common.Constants;
import com.company.project.mvp.contract.upload.ResultDataContract;
import com.company.project.mvp.model.entity.db.RTData;
import com.company.project.mvp.model.entity.db.ResultData;
import com.company.project.mvp.presenter.base.BasePresenter;

import org.litepal.crud.DataSupport;

import java.util.List;

import com.company.project.mvp.contract.upload.ResultDataContract;
import com.company.project.mvp.model.entity.db.RTData;
import com.company.project.mvp.presenter.base.BasePresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author：leguang on 2016/10/14 0014 13:17
 * Email：langmanleguang@qq.com
 */
public class ResultDataPresenter extends BasePresenter<ResultDataContract.View> implements ResultDataContract.Presenter {

    private static final String TAG = ResultDataPresenter.class.getSimpleName();

    public ResultDataPresenter(ResultDataContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        request(0);
    }


    @Override
    public void request(final int pagination) {
        mRxManager.add(Observable.create(new Observable.OnSubscribe<List<RTData>>() {
                    @Override
                    public void call(Subscriber<? super List<RTData>> subscriber) {
                        List<RTData> mResultData = null;
                        try {
                            mResultData = DataSupport.select("*")
                                    .order("id").limit(Constants.PAGE_SIZE)
                                    .offset(pagination * Constants.PAGE_SIZE)
                                    .find(RTData.class);
                            subscriber.onNext(mResultData);
                        } catch (Exception ex) {
                            subscriber.onError(ex);
                        }
                        //做此判断的目的是为了不让最后onCompleted()调用的showContent()遮住了showEmpty()的显示。
                        if (mResultData != null && mResultData.size() > 0) {
                            subscriber.onCompleted();
                        }
                    }
                }).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<List<RTData>>() {
                            @Override
                            public void _onNext(List<RTData> mResultData) {
                                getView().response(mResultData, pagination);
                            }
                        })
        );
    }
}
