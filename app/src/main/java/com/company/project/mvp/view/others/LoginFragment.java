package com.company.project.mvp.view.others;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.company.project.R;
import com.company.project.common.Constants;
import com.company.project.mvp.contract.LoginContract;
import com.company.project.mvp.presenter.LoginPresenter;
import com.company.project.mvp.view.base.BaseFragment;
import com.company.project.mvp.view.main.MainActivity;
import com.company.project.utils.DeviceUtils;
import com.company.project.utils.KeyBoardUtils;
import com.company.project.widget.processbutton.iml.ActionProcessButton;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.company.project.BaseApplication;
import com.company.project.mvp.view.base.BaseFragment;
import com.company.project.mvp.view.main.MainActivity;
import com.company.project.widget.processbutton.iml.ActionProcessButton;
import retrofit2.adapter.rxjava.HttpException;

import static com.company.project.BaseApplication.mContext;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class LoginFragment extends BaseFragment<LoginContract.Presenter> implements LoginContract.View {

    private static final String TAG = LoginFragment.class.getSimpleName();
    @BindView(R.id.et_username_login_fragment)
    TextInputLayout etUsername;
    @BindView(R.id.et_password_login_fragment)
    TextInputLayout etPassword;
    @BindView(R.id.bt_login_fragment)
    ActionProcessButton btLogin;
    @BindView(R.id.cl_login_fragment)
    CoordinatorLayout cl;
    private String username;
    private String password;
    private int fromTo;
    private String strMachineCode;

    public static LoginFragment newInstance(int fromTo) {
        Bundle args = new Bundle();
        args.putInt(Constants.FROM_TO, fromTo);

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static String[] PERMISSION= {Manifest.permission.READ_PHONE_STATE};
    private  boolean islacksOfPermission(String permission){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            return ContextCompat.checkSelfPermission(BaseApplication.mContext, permission) ==
                    PackageManager.PERMISSION_DENIED;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0x12){
            setDeviceId();
        }else{
            _mActivity.finish();
        }
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            fromTo = args.getInt(Constants.FROM_TO);
        }
    }

    @NonNull
    @Override
    protected LoginContract.Presenter createPresenter() {
        return new LoginPresenter(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fromTo == Constants.FROM_SPLASH) {
            getFragmentManager().beginTransaction()
                    .show(getPreFragment())
                    .commit();
        }
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //进场动画
        revealShow();
        initData();
    }

    private void initData() {

        etUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btLogin.setProgress(0);
                if (TextUtils.isEmpty(s)) {
                    etUsername.setError("用户名不能为空");
                    etUsername.setErrorEnabled(true);
                } else {
                    etUsername.setError("");
                    etUsername.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        etPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btLogin.setProgress(0);
                if (TextUtils.isEmpty(s)) {
                    etPassword.setError("密码不能为空");
                    etPassword.setErrorEnabled(true);
                } else {
                    etPassword.setError("");
                    etPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.bt_login_fragment)
    public void onClick() {
        KeyBoardUtils.hideKeybord(btLogin, _mActivity);
        username = etUsername.getEditText().getText().toString().trim();
        password = etPassword.getEditText().getText().toString().trim();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            btLogin.setProgress(50);
            //strMachineCode = DeviceUtils.getIMEI(getContext());
            setDeviceId();
//            mPresenter.login(username, password,strMachineCode);
            go2Main();

        } else if (TextUtils.isEmpty(username)) {
            etUsername.setErrorEnabled(true);
            etUsername.setError("");
            etUsername.setError("用户名不能为空");
        } else if (TextUtils.isEmpty(password)) {
            etUsername.setErrorEnabled(true);
            etUsername.setError("");
            etPassword.setError("密码不能为空");
        }
    }

    @Override
    public void setErrorMessage(String message) {
        btLogin.setErrorText(message);
        btLogin.setProgress(-1);
    }

    @Override
    public void setSuccessMessage() {
        //按钮提示成功信息
        btLogin.setProgress(100);
    }

    @Override
    public void go2Main() {
        btLogin.postDelayed(new Runnable() {
            @Override
            public void run() {
                _mActivity.startActivity(new Intent(_mActivity, MainActivity.class));
            }
        }, 300);

        _mActivity.finish();
    }



    @Override
    public void showContent() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(islacksOfPermission(PERMISSION[0])){
            ActivityCompat.requestPermissions(getActivity(),PERMISSION,0x12);
        }else{
            setDeviceId();
        }
    }

    private void setDeviceId(){

        strMachineCode=DeviceUtils.getIMEI(getContext());
        //app.setDeviceID(device_Id);
    }

    @Override
    public void onPause() {
        super.onPause();
        //防止屏幕旋转后重画时fab显示
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

    @Override
    public void showLoading() {

    }

    private void revealShow() {
        cl.post(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                    cl.setVisibility(View.VISIBLE);
                    return;
                }

                int cx = (cl.getLeft() + cl.getRight()) / 2;
                int cy = (cl.getTop() + cl.getBottom()) / 2;

                int w = cl.getWidth();
                int h = cl.getHeight();

                // 勾股定理 & 进一法
                int finalRadius = (int) Math.hypot(w, h);

                Animator anim = ViewAnimationUtils.createCircularReveal(cl, cx, cy, 0, finalRadius);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);

                    }
                });
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.setDuration(1000);
                anim.start();
            }
        });
    }
}


