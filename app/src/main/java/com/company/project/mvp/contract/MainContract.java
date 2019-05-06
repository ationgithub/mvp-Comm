package com.company.project.mvp.contract;


import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.GongdianData;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;

import java.util.List;

import com.company.project.mvp.model.entity.db.GongdianData;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;

public interface MainContract {
    interface View extends BaseContract.View {
        void responseGongdianData(List<GongdianData> mGongdianData);

        void responseShuizhunxianData(List<YusheshuizhunxianData> mShuizhunxianData, int pagination);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestGongdianData();

        void requestShuizhunxianData(int pagination, String strGongdianParam, String strMeasureStatusParam, String strTimeTypeParam);
    }
}
