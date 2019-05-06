package com.company.project.mvp.contract;

import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;

import java.util.List;

import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;


/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public interface AddYusheshuizhunxianContract {
    interface View extends BaseContract.View {
        void responseJidian(String[] mJidianData);

        void responseCedian(String[] mCedianData);

        void responseStaff(List<String> mStaffData);

        void responseSave(int rowsAffected);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestJidian();

        void requestCedian();

        void requestStaff();

        void save(YusheshuizhunxianData mYusheshuizhunxianData, String dateTime);
    }
}
