package com.company.project.mvp.contract;


import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.bean.CheckUpdateBean;

import com.company.project.mvp.model.entity.bean.CheckUpdateBean;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public interface VersionContract {
    interface View extends BaseContract.View {
        void setErrorMessage(String message);

        void showForceUpdateDialog(CheckUpdateBean.UpdateInfoBean mUpdateInfoBean);

        void showUpdateDialog(CheckUpdateBean.UpdateInfoBean mUpdateInfoBean);
    }

    interface Presenter extends BaseContract.Presenter {

        void checkUpdate();
    }

}
