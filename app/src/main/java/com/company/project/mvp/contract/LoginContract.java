package com.company.project.mvp.contract;

import com.company.project.mvp.contract.base.BaseContract;

public interface LoginContract {
    interface View extends BaseContract.View {
        void setErrorMessage(String message);

        void setSuccessMessage();

        void go2Main();
    }

    interface Presenter extends BaseContract.Presenter {
        void login(String username, String password, String strMachineCode);
    }
}