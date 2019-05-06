package com.company.project.mvp.contract.upload;


import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.OriginData;

import java.util.List;

import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.OriginData;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public interface OriginalDataContract {
    interface View extends BaseContract.View {
        void response(List<OriginData> mOriginalData, int pagination);
    }

    interface Presenter extends BaseContract.Presenter {
        void request(int pagination);
    }
}
