package com.company.project.mvp.view.others;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.company.project.BaseApplication;
import com.company.project.R;
import com.company.project.common.Constants;
import com.company.project.common.ToastUtils;
import com.company.project.mvp.contract.RegisterContract;
import com.company.project.mvp.model.entity.bean.RegisterBean;
import com.company.project.mvp.presenter.RegisterPresenter;
import com.company.project.mvp.view.base.BaseActivity;
import com.company.project.utils.AESCryptUtils;
import com.company.project.utils.DeviceUtils;
import com.company.project.utils.KeyBoardUtils;
import com.company.project.utils.SharedPreferencesUtils;
import com.company.project.widget.processbutton.iml.ActionProcessButton;
import com.socks.library.KLog;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.company.project.mvp.view.base.BaseActivity;
import com.company.project.widget.processbutton.iml.ActionProcessButton;
import retrofit2.adapter.rxjava.HttpException;

public class RegisterActivity extends BaseActivity<RegisterContract.Presenter> implements RegisterContract.View {


    @BindView(R.id.bt_register_activity)
    ActionProcessButton bt;

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }

    @NonNull
    @Override
    protected RegisterContract.Presenter createPresenter() {
        return new RegisterPresenter(this);
    }


    @OnClick(R.id.bt_register_activity)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_register_activity:
                KeyBoardUtils.hideKeybord(bt, this);
                String strMachineCode = DeviceUtils.getIMEI(getApplicationContext());
                String strphoneBrand = DeviceUtils.getPhoneBrand();
                String strphoneSysVersion = DeviceUtils.getOSVersion();
                String strphoneModel = DeviceUtils.getPhoneType();

                KLog.e(strMachineCode);
                KLog.e(strphoneBrand);
                KLog.e(strphoneSysVersion);
                KLog.e(strphoneModel);
                Log.e("strMachineCode",strMachineCode);
                Log.e("strphoneBrand",strphoneBrand);
                Log.e("strphoneSysVersion",strphoneSysVersion);
                Log.e("strphoneModel",strphoneModel);

                if (!TextUtils.isEmpty(strMachineCode)) {
                    mPresenter.register(strMachineCode, strphoneBrand, strphoneSysVersion, strphoneModel);
                } else {
                    ToastUtils.showSuccessToast(getApplicationContext(), "机器码获取失败");
                }
                break;
        }
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
    public void showContent() {

    }

    @Override
    public void showError(Throwable t) {
        if (t instanceof ConnectException) {
            setErrorMessage("网络异常");
        } else if (t instanceof HttpException) {
            setErrorMessage("服务器异常");
        } else if (t instanceof SocketTimeoutException) {
            setErrorMessage("连接超时");
        } else if (t instanceof JSONException) {
            setErrorMessage("解析异常");
        } else {
            setErrorMessage("数据异常");
        }
    }

    public void setErrorMessage(String message) {
        bt.setErrorText(message);
        bt.setProgress(-1);
    }

    @Override
    public void showLoading() {
        bt.setProgress(50);
    }

    @Override
    public void registerSuccessfully(RegisterBean mRegisterBean) {


        bt.setProgress(100);
        bt.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 618);
        Log.e("REGISTER_CODE",mRegisterBean.getRegCode());
        ToastUtils.showSuccessToast(getApplicationContext(), "恭喜！注册成功请登录");
        //保存注册码到本地
        String encryptRegisterCode = mRegisterBean.getRegCode();
        SharedPreferencesUtils.put(getApplicationContext(), Constants.REGISTER_CODE, encryptRegisterCode);
        SharedPreferencesUtils.put(getApplicationContext(), Constants.ISFIRSTENTRY, false);
        Intent mIntent = new Intent(this, LaunchActivity.class);
        //目的是为了通知LaunchActivity此时应该启动loginfragment，而不是闪屏页。
        mIntent.putExtra(Constants.FROM_TO, Constants.FROM_REGISTER);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void registerFailed(String message) {
        setErrorMessage(message);
    }
}
