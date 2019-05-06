package com.company.project.mvp.contract.upload;

import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.CezhanData;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;

import java.util.List;

import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.CezhanData;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public interface DetailContract {
    interface View extends BaseContract.View {
        void responseCezhanData(List<CezhanData> listCezhan);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestCezhanData(ShuizhunxianData mShuizhunxianData);
    }
}
