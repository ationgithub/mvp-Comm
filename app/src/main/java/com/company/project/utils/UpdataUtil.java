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


    public static int parserCode(String content) {
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



    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
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
            File file = new File(Environment.getExternalStorageDirectory(), "app-wy.apk");
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

    public static StringBuffer getXml(String portName) {
        try {
            URL url = new URL(portName);
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

}
