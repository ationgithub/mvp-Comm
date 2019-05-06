package com.company.project.mvp.view.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.company.project.BaseApplication;
import com.company.project.R;
import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.view.base.BaseFragment;
import com.company.project.utils.DensityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.company.project.BaseApplication;
import com.company.project.mvp.view.base.BaseFragment;

/**
 * Created by Administrator on 2018/4/25.
 */

public class AddDuanMianFragment extends BaseFragment {


    @BindView(R.id.toolbar_toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_dalicheng)
    EditText ed_dalicheng;
    @BindView(R.id.ed_xiaolicheng)
    EditText ed_xiaolicheng;
    @BindView(R.id.et_duanmian_width)
    EditText et_duanmian_width;
    @BindView(R.id.et_mark)
    EditText et_mark;
    @BindView(R.id.spinner_method_add)
    MaterialSpinner spinner_method_add;
    @BindView(R.id.spinner_level_add)
    MaterialSpinner spinner_level_add;
    @BindView(R.id.et_gdnumber)
    EditText et_gdnumber;
    @BindView(R.id.et_shuiping_line)
    EditText et_shuiping_line;
    @BindView(R.id.et_xie_line)
    EditText et_xie_line;
    @BindView(R.id.et_db_number)
    EditText et_db_number;
    @BindView(R.id.et_gd_change_value)
    EditText et_gd_change_value;
    @BindView(R.id.et_gd_change_speed_value)
    EditText et_gd_change_speed_value;
    @BindView(R.id.et_line_change_value)
    EditText et_line_change_value;
    @BindView(R.id.et_line_change_speed_value)
    EditText et_line_change_speed_value;
    @BindView(R.id.et_xie_change_value)
    EditText et_xie_change_value;
    @BindView(R.id.et_xie_change_speed_value)
    EditText et_xie_change_speed_value;
    @BindView(R.id.et_db_change_value)
    EditText et_db_change_value;
    @BindView(R.id.et_db_change_speed_value)
    EditText et_db_change_speed_value;
    @BindView(R.id.cb_change)
    CheckBox cb_change;
    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    @BindView(R.id.ll_add_duanmian_fragment)
    LinearLayout ll;
    private String[]  arrayDigMethod;
    private String[]  arrayDigLevel;
    private ImageView ivSave;
    private ViewGroup viewGroup;

    public static AddDuanMianFragment newInstance() {
        return new AddDuanMianFragment();
    }

    @NonNull
    @Override
    protected BaseContract.Presenter createPresenter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_duanmian, container, false);
        ivSave = (ImageView) inflater.inflate(R.layout.menu_item_sync, null);
        ButterKnife.bind(this, view);
        getFragmentManager().beginTransaction()
                .show(getPreFragment())
                .commit();
        return attachToSwipeBack(view);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initStateBar(toolbar);
        revealShow();
        initToolbar();
        initData();
        viewGroup = (ViewGroup) _mActivity.findViewById(android.R.id.content).getRootView();
    }

    private void initData() {

        Resources res = getResources();
        arrayDigLevel = res.getStringArray(R.array.dig_level);
        arrayDigMethod = res.getStringArray(R.array.dig_method);
        spinner_method_add.setItems(arrayDigMethod);
        spinner_level_add.setItems(arrayDigLevel);

    }

    private void initToolbar() {
        toolbar.setTitle("创建断面");
        initToolbarBackNavigation(toolbar);

    }

    private void revealClose() {
        ll.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    ll.setVisibility(View.INVISIBLE);
                    return;
                }

                int intTemp = DensityUtils.dp2px(BaseApplication.mContext, 26);
                int cx = ll.getRight() - intTemp;
                int cy = intTemp;

                int w = ll.getWidth();
                int h = ll.getHeight();

                // 勾股定理 & 进一法
                int finalRadius = (int) Math.hypot(w, h);

                Animator anim = ViewAnimationUtils.createCircularReveal(ll, cx, cy, finalRadius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ll.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);

                    }
                });
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.setDuration(618);
                anim.start();
            }
        });
    }

    private void revealShow() {
        ll.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    ll.setVisibility(View.VISIBLE);
                    return;
                }

                int intTemp = DensityUtils.dp2px(BaseApplication.mContext, 26);
                int cx = ll.getRight() - intTemp;
                int cy = intTemp;
                int w = ll.getWidth();
                int h = ll.getHeight();
                int finalRadius = (int) Math.hypot(w, h);

                Animator anim = ViewAnimationUtils.createCircularReveal(ll, cx, cy, 0, finalRadius);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.setDuration(618);
                anim.start();
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        revealClose();
        return super.onBackPressedSupport();
    }
}
