package com.company.project.mvp.model;

import android.util.Log;

import com.company.project.utils.AESCryptUtils;
import com.company.project.utils.OtherCryptUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import okhttp3.ResponseBody;
import retrofit2.Converter;


public class IResponseBodyConverter2 implements Converter<ResponseBody, String> {

    @Override
    public String convert(ResponseBody value) throws IOException {
        String string = value.string();
        Log.e("IResponseBodyConverter2","#解密前@#" + string);
        try {
//            string = AESCryptUtils.decrypt("",string);
            string = OtherCryptUtils.Decrypt(string,"b392eleaab77c099","dl6059cl48633333");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("IResponseBodyConverter2","#解密后@#" + string);
        return string;
    }
}

