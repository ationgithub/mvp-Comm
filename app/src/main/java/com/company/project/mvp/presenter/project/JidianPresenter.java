package com.company.project.mvp.presenter.project;

import com.company.project.common.Constants;
import com.company.project.mvp.contract.project.JidianContract;
import com.company.project.mvp.model.entity.db.JidianData;
import com.company.project.mvp.presenter.base.BasePresenter;

import org.litepal.crud.DataSupport;

import java.util.List;

import com.company.project.mvp.contract.project.JidianContract;
import com.company.project.mvp.model.entity.db.JidianData;
import com.company.project.mvp.presenter.base.BasePresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author：leguang on 2016/10/14 0014 13:17
 * Email：langmanleguang@qq.com
 */
public class JidianPresenter extends BasePresenter<JidianContract.View> implements JidianContract.Presenter {

    private static final String TAG = JidianPresenter.class.getSimpleName();

    public JidianPresenter(JidianContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        queryData(0);
    }

    @Override
    public void queryData(final int pagination) {
        mRxManager.add(Observable.create(new Observable.OnSubscribe<List<JidianData>>() {
                    @Override
                    public void call(Subscriber<? super List<JidianData>> subscriber) {
                        List<JidianData> mJidianData = null;
                        try {
                            mJidianData = DataSupport.select("*").order("id")
                                    .limit(Constants.PAGE_SIZE)
                                    .offset(pagination * Constants.PAGE_SIZE)
                                    .find(JidianData.class);
                            subscriber.onNext(mJidianData);

                        } catch (Exception ex) {
                            subscriber.onError(ex);
                        }
                        if (mJidianData != null && mJidianData.size() > 0) {
                            subscriber.onCompleted();
                        }
                    }
                }).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<List<JidianData>>() {
                            @Override
                            public void _onNext(List<JidianData> mJidianData) {
                                getView().refresh(mJidianData, pagination);
                            }
                        })
        );
    }
}
