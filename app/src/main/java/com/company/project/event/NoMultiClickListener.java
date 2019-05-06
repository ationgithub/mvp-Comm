package com.company.project.event;

import android.view.View;

import java.util.Calendar;

/**
 * Created by leguang on 2016/8/15 0015.
 * 这个防止多点击事件,防抖
 */
public abstract class NoMultiClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoMultiClick(view);
        }
    }

    public abstract void onNoMultiClick(View view);
}
