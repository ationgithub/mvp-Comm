package com.company.project.mvp.contract.project;


import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.JidianData;

import java.util.List;

import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.JidianData;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public interface JidianContract {
    interface View extends BaseContract.View {
        void refresh(List<JidianData> mStaffData, int pagination);

    }

    interface Presenter extends BaseContract.Presenter {
        void queryData(int pagination);
    }
}
