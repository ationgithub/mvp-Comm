package com.company.project.utils;

import android.util.Base64;
import android.util.Log;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class OtherCryptUtils {

    public static String getMD5Str(String sourceStr) {
        byte[] source = sourceStr.getBytes();
        char hexDigits[] = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        java.security.MessageDigest md = null;

        try {
            md = java.security.MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (md == null) {
            return null;
        }
        md.update(source);
        byte tmp[] = md.digest();
        char str[] = new char[16 * 2];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = tmp[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * 加密
     * @param sSrc 加密的明文
     * @param sKey 秘钥
     * @param iv 向量  16 bytes
     * @return
     * @throws Exception
     */
    public static String Encrypt(String sSrc, String sKey,String iv) throws Exception {
        if (sKey == null) {
            Log.e("OtherCrypt","Key不能为空null");
            return null;
        }
//        if (sKey.length() != 16) {
//            Log.e("OtherCrypt","Key的长度不是16位");
//            return null;
//        }
//        if (iv.length() != 16) {
//            Log.e("OtherCrypt","iv的长度不是16位");
//            return null;
//        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv1 = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv1);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

        return  Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    /**
     * 解密
     * @param sSrc 接收到的加密过后的字符串（带解密密文）
     * @param sKey 秘钥
     * @param iv 初始化向量
     * @return
     * @throws Exception
     */
    public static String Decrypt(String sSrc, String sKey,String iv) throws Exception {
        try {
            if (sKey == null) {
                Log.e("OtherCrypt","Key不能为空null");
                return null;
            }
            if (sKey.length() != 16) {
                Log.e("OtherCrypt","Key的长度不是16位");
                return null;
            }
            if (iv.length() != 16) {
                Log.e("OtherCrypt","iv的长度不是16位");
                return null;
            }
            byte[] byte1 = Base64.decode(sSrc,Base64.DEFAULT);//先用Base64解码
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
            SecretKeySpec key = new SecretKeySpec(sKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            //与加密时不同MODE:Cipher.DECRYPT_MODE
            byte[] ret = cipher.doFinal(byte1);
            return new String(ret, "utf-8");
        } catch (Exception ex) {
            Log.e("OtherCrypt",ex.toString());
            return null;
        }
    }

}
