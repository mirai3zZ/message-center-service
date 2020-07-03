package com.yangyang.cloud.common;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Deprecated
public class EncryptUtil {
    private static final String ALGORITHMSTR = "AES/CBC/PKCS5Padding";

    public static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }

    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return aesDecryptByBytes(encryptStr.getBytes(), decryptKey);
    }

    // 解密
    public static String decrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        if (StringUtils.isNotBlank(sSrc)) {
            sSrc = sSrc.replace(' ', '+');
            try {
                byte[] raw = sKey.getBytes("ASCII");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                // 先用base64解密
                byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, encodingFormat);
                return originalString;
            } catch (Exception ex) {
                throw new Exception();
            }
        } else {
            return null;
        }
    }

    public static String decrypt(String data, String encodingFormat) throws Exception {
        if (StringUtils.isNotBlank(data)) {
            data = data.replace(' ', '+');
            String key = data.substring(0, 16);
            String sSrc = data.substring(16, data.length());
            return decrypt(sSrc, encodingFormat, key, key);

        } else {
            return null;
        }
    }

    public static String decrypt(String key, String src, String encodingFormat) throws Exception {
        if (StringUtils.isNotBlank(src)) {
            src = src.replace(' ', '+');
            return decrypt(src, encodingFormat, key, key);
        } else {
            return null;
        }

    }

    public static String getRandomKey() {
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
                "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};
        StringBuffer randomKey = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            randomKey.append(chars[ThreadLocalRandom.current().nextInt(chars.length)]);
        }
        return randomKey.toString();
    }

}