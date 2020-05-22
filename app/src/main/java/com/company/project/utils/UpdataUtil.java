package com.company.project.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.net.URL;

public class UpdataUtil extends AppCompatActivity {


    String baseUrl = "http://192.168.1.116:8080/jeecg/rest/";
     int verCode, newCode;
    private ProgressDialog pBar;

    /** 检查更新 */
    public void getUpdate() {
        new Thread() {
            @Override
            public void run() {
                StringBuffer result = getArray1(baseUrl+"/app-version/versionCode.xml");
                Log.e("newCode", result.toString());
                if (result.toString() != "") {
                    newCode = parser1(result.toString());
                    try {
                        verCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
//                    if (newCode > verCode) {
//                        doNewVersionUpdate();
//                    }
                }
            }
        }.start();
    }

    public StringBuffer getArray1(String portName) {
        try {
            java.net.URL url = new java.net.URL(portName);
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
            return result;
        } catch (Exception e) {
            return new StringBuffer();
        }
    }

    public static int parser1(String content) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            StringReader in = new StringReader(content);
            parser.setInput(in);
            int result = parser.getEventType();
            while (result != XmlPullParser.END_DOCUMENT) {
                switch (result) {
                    case XmlPullParser.START_TAG:
                        if ("versionCode".equals(parser.getName()))
                            return Integer.parseInt(parser.nextText().toString());
                        break;
                }
                result = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


//    private FileOutputStream fos;
//    private InputStream is;
    private void doNewVersionUpdate() {
        Dialog dialog = new AlertDialog.Builder(getApplicationContext()).setTitle("软件更新").setMessage("发现有新版本")
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pBar = new ProgressDialog(getApplicationContext());
                        pBar.setTitle("正在下载...");
                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pBar.setButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //                                    fos.close();
//                                    is.close();
                                File file = new File(Environment.getExternalStorageDirectory(), "dyhMallAPP.apk");
                                file.delete();
                                dialog.dismiss();
                            }
                        });
                        downFile();
                    }
                }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
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
                    File file = getFileFromServer(baseUrl + "/update/dyhMallAPP.apk", pd);
//					File file = new File(Environment.getExternalStorageDirectory(), "dyhMallAPP.apk");
                    sleep(1000);
                    installApk(file);
                    pd.dismiss(); // 结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();

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

}
