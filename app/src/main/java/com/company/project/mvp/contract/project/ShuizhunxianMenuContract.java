package com.company.project.mvp.contract.project;


import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.GongdianData;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;

import java.util.List;

import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public interface ShuizhunxianMenuContract {
    interface View extends BaseContract.View {
        void responseShuizhunxian(List<YusheshuizhunxianData> mYusheshuizhunxianData, int pagination);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestShuizhunxian(int pagination);
    }
}
