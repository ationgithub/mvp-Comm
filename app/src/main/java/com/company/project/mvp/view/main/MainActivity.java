package com.company.project.mvp.view.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.project.BaseApplication;
import com.company.project.R;
import com.company.project.common.ActivityManager;
import com.company.project.common.Constants;
import com.company.project.event.EventData;
import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.view.base.BaseActivity;
import com.company.project.mvp.view.main.setting.SettingFragment;
import com.company.project.mvp.view.others.LaunchActivity;
import com.company.project.utils.SharedPreferencesUtils;
import com.company.project.utils.UpdataUtil;

import org.greenrobot.eventbus.EventBus;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;

import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView tv_username;
    private TextView tv_phone_number;
    private LinearLayout llNavHeader;
    private ActionBarDrawerToggle toggle;
    private TextView tv_header;

    String baseUrl = "http://192.168.1.116:8080/jeecg/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUpdate();

        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_main_activity, MainFragment.newInstance());
        }
        initView();
        initData();
        revealShow();
    }

    @NonNull
    @Override
    protected BaseContract.Presenter createPresenter() {
        return null;
    }

    private void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        llNavHeader = (LinearLayout) navigationView.getHeaderView(0);
        tv_header = (TextView) llNavHeader.findViewById(R.id.iv_header_nav_header_main);
        tv_username = (TextView) llNavHeader.findViewById(R.id.tv_username_nav_header_main);
        tv_phone_number = (TextView) llNavHeader.findViewById(R.id.tv_phone_number_nav_header_main);
    }

    public StringBuffer getArray1(String portName) {
        try {
            java.net.URL url = new java.net.URL(portName);
            Log.e("newCode", url.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "text/html");
//            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());

            InputStreamReader reader = new InputStreamReader(httpURLConnection.getInputStream());
            char[] c = new char[1024];
            StringBuffer result = new StringBuffer();
            int length = -1;
            while ((length = reader.read(c)) != -1) {
                result.append(c, 0, length);
            }
            reader.close();
            Log.e("newCode", result.toString());
            return result;
        } catch (Exception e) {
            return new StringBuffer();
        }
    }

    int verCode, newCode;
    public void getUpdate() {
        new Thread() {
            @Override
            public void run() {
                Log.e("newCode", "1111111");
                StringBuffer result = getArray1(baseUrl+"app-version/VersionCode.xml");
                if (result.toString() != "") {
                    newCode = UpdataUtil.parser1(result.toString());
                    Log.e("newCode",newCode+"");
                    try {
                        verCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (newCode > verCode) {
                        doNewVersionUpdate();
                    }
                }
            }
        }.start();
    }


    private void doNewVersionUpdate() {
        Toast.makeText(this,"ddddd",Toast.LENGTH_SHORT).show();
//        Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新").setMessage("发现有新版本")
//                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        pBar = new ProgressDialog(MainActivity.this);
//                        pBar.setTitle("正在下载...");
//                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                        pBar.setButton("取消", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //                                    fos.close();
////                                    is.close();
//                                File file = new File(Environment.getExternalStorageDirectory(), "app-release.apk");
//                                file.delete();
//                                dialog.dismiss();
//                            }
//                        });
//                        downFile();
//                    }
//                }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                    }
//                }).create();
//        dialog.show();

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("软件更新")
                .setMessage("发现有新版本")
                .setCancelable(false)
                .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog pBar = new ProgressDialog(MainActivity.this);
                        pBar.setTitle("正在下载...");
                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pBar.setButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //                                    fos.close();
//                                    is.close();
                                File file = new File(Environment.getExternalStorageDirectory(), "app-release.apk");
                                file.delete();
                                dialog.dismiss();
                            }
                        });
                        downFile();
                    }
                }).show();

    }
    public void downFile() {
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(baseUrl + "app-version/app-release.apk", pd);
//					File file = new File(Environment.getExternalStorageDirectory(), "dyhMallAPP.apk");
                    sleep(1000);
                    installApk(file);
                    pd.dismiss(); // 结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();
    }
    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            java.net.URL url = new java.net.URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "text/html");
            // 获取到文件的大小
            // pd.setMax(conn.getContentLength());
            pd.setMax(100);
            InputStream is = httpURLConnection.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "app-release.apk");
            if (file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量

                float a = (float) total;
                float b = (float) httpURLConnection.getContentLength();
                pd.setProgress((int) (a / b * 100));
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    private void initData() {
        if (BaseApplication.mUserInfoBean != null) {
            tv_username.setText("姓名：" + BaseApplication.mUserInfoBean.getUserFullName());
            tv_phone_number.setText("电话" + BaseApplication.mUserInfoBean.getUserPhoneNum());
            String strFamilyName = BaseApplication.mUserInfoBean.getUserFullName().substring(0, 1);
            tv_header.setText(strFamilyName);
        }
        navigationView.setNavigationItemSelectedListener(this);
//        llNavHeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawer.closeDrawer(GravityCompat.START);
//                drawer.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, 250);
//            }
//        });
    }

    public void initToolBar(Toolbar toolbar) {
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        //延迟启动Fragment，先让侧滑控件关闭，避免看上去卡顿一下。
        drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                int id = item.getItemId();
                if (id == R.id.search) {
                    go2Project();
                } else if (id == R.id.update) {
//                    start(UploadFragment.newInstance());
                } else if (id == R.id.download) {
                    start(DownloadFragment.newInstance());
                } else if (id == R.id.setting) {
                    SettingFragment fragment = findFragment(SettingFragment.class);
                    if (fragment == null) {

                        popTo(MainFragment.class, false, new Runnable() {
                            @Override
                            public void run() {
                                start(SettingFragment.newInstance());
                            }
                        });
                    } else {
                        // 如果已经在栈内,则以SingleTask模式start
                        start(fragment, SupportFragment.SINGLETASK);
                    }

                } else if (id == R.id.relogin) {
                    go2Login();
                } else if (id == R.id.about) {

                } else if (id == R.id.version) {
//                    VersionFragment fragment = findFragment(VersionFragment.class);
//                    if (fragment == null) {
//                        popTo(MainFragment.class, false, new Runnable() {
//                            @Override
//                            public void run() {
//                                start(VersionFragment.newInstance());
//                            }
//                        });
//                    } else {
//                        // 如果已经在栈内,则以SingleTask模式start
//                        start(fragment, SupportFragment.SINGLETASK);
//                    }
                }
            }
        }, 300);
        return true;
    }

    private void go2Login() {
        //用户名和密码设为空，这样在点击重新登录后，即使没有登录，下次启动由于闪屏页的验证是需要用户名和密码，所以跳转到登录页面。
        SharedPreferencesUtils.put(BaseApplication.mContext, Constants.USERNAME, "");
        SharedPreferencesUtils.put(BaseApplication.mContext, Constants.PASSWORD, "");

        Intent mIntent = new Intent(this, LaunchActivity.class);
        //目的是为了通知LaunchActivity此时应该启动loginfragment，而不是闪屏页。
        mIntent.putExtra(Constants.FROM_TO, Constants.FROM_MAIN);
        startActivity(mIntent);
        finish();
    }

    private void go2Project() {
//        startActivity(new Intent(this, ProjectActivity.class));
        startActivity(new Intent(this, com.company.project.sectionedrecyclerviewsample.MainActivity.class));
    }

    @Override
    public boolean swipeBackPriority() {
        return false;
    }

    public void closeDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void openDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void revealShow() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawer.setVisibility(View.VISIBLE);
            return;
        }
        drawer.post(new Runnable() {
            @Override
            public void run() {

                int cx = (drawer.getLeft() + drawer.getRight()) / 2;
                int cy = (drawer.getTop() + drawer.getBottom()) / 2;

                int w = drawer.getWidth();
                int h = drawer.getHeight();

                // 勾股定理 & 进一法
                int finalRadius = (int) Math.hypot(w, h);

                Animator anim = ViewAnimationUtils.createCircularReveal(drawer, cx, cy, 0, finalRadius);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        drawer.setVisibility(View.VISIBLE);
                        EventBus.getDefault().postSticky(new EventData(Constants.EVENT_FINISH_LAUNCH));

                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        drawer.setVisibility(View.VISIBLE);
                    }
                });
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.setDuration(1000);
                anim.start();
            }
        });
    }
}
