package com.company.project.mvp.view.main.upload;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.company.project.R;
import com.company.project.common.Constants;
import com.company.project.mvp.contract.upload.DetailContract;
import com.company.project.mvp.model.entity.db.CezhanData;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;
import com.company.project.mvp.presenter.upload.DetailPresenter;
import com.company.project.mvp.view.base.BaseFragment;
import java.util.List;
import com.company.project.widget.recyclervierviewpager.RecyclerViewPager;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class DetailDataFragment extends BaseFragment<DetailContract.Presenter> implements DetailContract.View {
    private static final String TAG = DetailDataFragment.class.getSimpleName();
    RecyclerViewPager mRecyclerView;
    private ShuizhunxianData mShuizhunxianData;
    private LinearLayoutManager mLinearLayoutManager;
//    private MeasureRVPAdapter mAdapter;

    public static DetailDataFragment newInstance(ShuizhunxianData mShuizhunxianData) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.YUSHESHUIZHUNXIAN, mShuizhunxianData);

        DetailDataFragment fragment = new DetailDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mShuizhunxianData = (ShuizhunxianData) args.getSerializable(Constants.YUSHESHUIZHUNXIAN);
        }
    }

    @NonNull
    @Override
    protected DetailContract.Presenter createPresenter() {
        return new DetailPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_data, container, false);
        mRecyclerView = (RecyclerViewPager) view.findViewById(R.id.rvp_check_measure_fragment);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initRecyclerViewPager();
    }


    private void initData() {
        mPresenter.requestCezhanData(mShuizhunxianData);
    }

    protected void initRecyclerViewPager() {
        //数字越大，触发滚向下一页所需偏移就越大
        mRecyclerView.setTriggerOffset(0.1f);
        //数字越大，就越容易滚动
        mRecyclerView.setFlingFactor(0.0f);
        mRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
//        mRecyclerView.setAdapter(mAdapter = new MeasureRVPAdapter());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);
    }

    @Override
    public void responseCezhanData(List<CezhanData> listCezhan) {
//        mAdapter.setNewData(listCezhan);
    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(Throwable t) {

    }

    @Override
    public void showLoading() {

    }
}
