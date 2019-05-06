package com.company.project.mvp.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.company.project.R;
import com.company.project.mvp.model.entity.db.RTData;
import com.socks.library.KLog;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class ResultDataRVAdapter extends BaseQuickAdapter<RTData, BaseViewHolder> {
    private static final String TAG = ResultDataRVAdapter.class.getSimpleName();

    public ResultDataRVAdapter() {
        super(R.layout.item_rv_measured_result_data_fragment, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, RTData mResultData) {
        holder.setText(R.id.tv_number_item_rv_measured_result_data_fragment, mResultData.getCedianid())
                .setText(R.id.tv_state_item_rv_measured_result_data_fragment, mResultData.getCedianzhuangtai())
                .setText(R.id.tv_date_item_rv_measured_result_data_fragment, mResultData.getGuanceshijian())
                .setText(R.id.tv_gaizhengshu_item_rv_measured_result_data_fragment, mResultData.getXiuzhengliang())
                .setText(R.id.tv_gaizhenghougaocha_item_rv_measured_result_data_fragment, mResultData.getYuanshibiaogao())
                .setText(R.id.tv_gaizhenghougaocheng_item_rv_measured_result_data_fragment, mResultData.getBiaogao());
        KLog.e(TAG,"==============ResultDataRVAdapter=============");
    }
}
