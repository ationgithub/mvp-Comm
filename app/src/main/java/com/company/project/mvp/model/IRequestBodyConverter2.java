package com.company.project.mvp.model;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import com.company.project.utils.AESCryptUtils;
import com.company.project.utils.OtherCryptUtils;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * RequestBody 对请求体进行加密处理
 */
public class IRequestBodyConverter2<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    static final Charset UTF_8 = Charset.forName("UTF-8");

    final Gson gson;
    final TypeAdapter<T> adapter;

    IRequestBodyConverter2(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
        Log.e("IRequestBodyConverter2","#IRequestBodyConverter初始化#");
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        String json = value.toString();
        Log.e("IRequestBodyConverter2","#加密前#" + json);
        try {
//            json = AESCryptUtils.encrypt("",json);
            json = OtherCryptUtils.Encrypt(json,"b392eleaab77c099","dl6059cl48633");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("IRequestBodyConverter2","#加密后#" + json);
        return RequestBody.create(MEDIA_TYPE, json);
    }
}


