package com.company.project.mvp.view.others;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.company.project.R;
import com.company.project.common.Constants;
import com.company.project.common.ToastUtils;
import com.company.project.event.EventData;
import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.view.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.company.project.mvp.view.base.BaseActivity;

public class LaunchActivity extends BaseActivity {

    private static final String TAG = LaunchActivity.class.getSimpleName();
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    private int fromTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        fromTo = getIntent().getIntExtra(Constants.FROM_TO, Constants.FROM_SPLASH);
        if (savedInstanceState == null) {
            if (fromTo == Constants.FROM_SPLASH) {
                loadRootFragment(R.id.fl_launch_activity, SplashFragment.newInstance());
            } else {
                loadRootFragment(R.id.fl_launch_activity, LoginFragment.newInstance(Constants.FROM_MAIN));
            }
        }
    }

    @NonNull
    @Override
    protected BaseContract.Presenter createPresenter() {
        return null;
    }

    @Override
    public boolean swipeBackPriority() {
        return false;
    }

    @Override
    public void onBackPressedSupport() {

        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            ToastUtils.showInfoToast(getApplicationContext(), Constants.PRESS_AGAIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventData event) {
        if (event.position == Constants.EVENT_FINISH_LAUNCH) {
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
