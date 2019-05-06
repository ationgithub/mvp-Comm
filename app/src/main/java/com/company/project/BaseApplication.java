package com.company.project;

import android.content.Context;

import com.github.moduth.blockcanary.BlockCanary;
import com.company.project.common.AppContext;
import com.company.project.common.Constants;
import com.company.project.mvp.model.entity.bean.UserInfoBean;
import com.company.project.mvp.model.entity.db.GongdianData;
import com.company.project.mvp.model.entity.db.JidianData;
import com.company.project.mvp.model.entity.db.ResultData;
import com.company.project.mvp.model.entity.db.ShuizhunxianData;
import com.company.project.mvp.model.entity.db.StaffData;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;
import com.socks.library.KLog;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.litepal.LitePalApplication;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class BaseApplication extends LitePalApplication {
    private static final String TAG = BaseApplication.class.getSimpleName();
    public static Context mContext;
    public static UserInfoBean mUserInfoBean;
    public static boolean isShowDialog = true;
    public RefWatcher mRefWatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        LitePalApplication.initialize(this);
        //日志的开关和全局标签初始化
        KLog.init(true, "SHTW沉降观测");
        mContext = this;
        // 程序异常交由AppExceptionHandler来处理
//        Thread.setDefaultUncaughtExceptionHandler(AppExceptionHandler.getInstance(this));
        //创建LeakCanary对象，观察内存泄漏
        mRefWatcher = LeakCanary.install(this);
        // 在主进程初始化调用哈
        BlockCanary.install(this, new AppContext()).start();

    }
}
