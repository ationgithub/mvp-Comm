package com.company.project.mvp.view.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
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
import com.company.project.common.Constants;
import com.company.project.event.EventData;
import com.company.project.mvp.contract.base.BaseContract;
import com.company.project.mvp.model.HttpHelper;
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

import static com.company.project.utils.UpdataUtil.getFileFromServer;
import static com.litesuits.common.utils.HandlerUtil.runOnUiThread;

public class MainActivity1 extends BaseActivity{

    String baseUrl = "http://192.168.1.116:8080/jeecg/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        getUpdate();
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_main_activity, MainFragment.newInstance());
        }
    }

    //先定义
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    //然后通过一个函数来申请
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    protected BaseContract.Presenter createPresenter() {
        return null;
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
            Log.e("getArray1", result.toString());
            return result;
        } catch (Exception e) {
            return new StringBuffer();
        }
    }

    //开始
    int verCode, newCode;
    public void getUpdate() {
        new Thread() {
            @Override
            public void run() {
                Log.e("getUpdate", "1111111");
                StringBuffer result = UpdataUtil.getXml(HttpHelper.BASE_URL+"appVersion/VersionCode.xml");
                if (result.toString() != "") {
                    newCode = UpdataUtil.parserCode(result.toString());
                    Log.e("newCode",newCode+"");
                    try {
                        verCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                        Log.e("verCode",verCode+"");
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (newCode > verCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                doNewVersionUpdate();
                            }
                        });
                    }
                }
            }
        }.start();
    }
    private void doNewVersionUpdate() {
        new AlertDialog.Builder(this)
                .setTitle("软件更新")
                .setMessage("发现有新版本")
                .setCancelable(false)
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog pBar = new ProgressDialog(MainActivity1.this);
                        pBar.setTitle("正在下载...");
                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pBar.setButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //                                    fos.close();
//                                    is.close();
                                File file = new File(Environment.getExternalStorageDirectory(), "app-wy.apk");
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
                    Log.e("downFile", HttpHelper.BASE_URL + "appVersion/app-wy.apk");
                    File file = getFileFromServer(HttpHelper.BASE_URL + "appVersion/app-wy.apk", pd);
//					File file = new File(Environment.getExternalStorageDirectory(), "dyhMallAPP.apk");
                    sleep(1000);
                    installApk(file);
                    pd.dismiss(); // 结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();
    }
    protected void installApk( File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    this
                    , "com.company.project.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }
    // 结束



//    private void doNewVersionUpdate() {
//        Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新").setMessage("发现有新版本")
//                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ProgressDialog pBar = new ProgressDialog(MainActivity1.this);
//                        pBar.setTitle("正在下载...");
//                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                        pBar.setButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                File file = new File(Environment.getExternalStorageDirectory(), "app-release.apk");
//                                file.delete();
//                                dialog.dismiss();
//                            }
//                        });
//
//                        downFile();
//                    }
//                }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                    }
//                }).create();
//        dialog.show();

//        new AlertDialog.Builder(MainActivity1.this)
//                .setTitle("软件更新")
//                .setMessage("发现有新版本")
//                .setCancelable(false)
//                .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ProgressDialog pBar = new ProgressDialog(MainActivity1.this);
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
//                }).show();
//    }
}
