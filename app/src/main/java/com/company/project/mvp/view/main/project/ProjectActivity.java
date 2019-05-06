package com.company.project.mvp.view.main.project;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.company.project.R;
import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.view.base.BaseActivity;
import com.company.project.mvp.view.main.MainFragment;

import com.company.project.mvp.view.base.BaseActivity;

public class ProjectActivity extends BaseActivity {
    private static final String TAG = ProjectActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_project_activity, ProjectFragment.newInstance());
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
}
