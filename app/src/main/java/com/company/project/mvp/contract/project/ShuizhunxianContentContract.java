package com.company.project.mvp.contract.project;


import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;

import java.util.List;

import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public interface ShuizhunxianContentContract {
    interface View extends BaseContract.View {
        void responseShuizhunxian(List<ShuizhunxianData> mShuizhunxianData, int pagination);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestShuizhunxian(int pagination, String yusheshuizhunxianID);
    }
}
