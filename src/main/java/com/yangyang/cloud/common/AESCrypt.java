package com.yangyang.cloud.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

/**
 * AES加密解密
 */

@Slf4j
public class AESCrypt {

    /**
     * 功能描述: 加密
     * 1.构造密钥生成器
     * 2.根据ecnodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String encrypt(String content, String passWord) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(passWord.getBytes());
            keygen.init(128, secureRandom);
            //3.产生原始对称密钥
            SecretKey originalKey = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = originalKey.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byteEncode = content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byteAES = cipher.doFinal(byteEncode);
            //10.将字符串返回
            return parseByte2HexStr(byteAES);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //如果有错就返加nulll
        return null;
    }

    /**
     * 功能描述: 解密
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String decrypt(String content, String passWord) {
        log.info("AES解密内容：{}，解密标识：{}",content,passWord);
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            log.info("密钥构造器：{}",keygen);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(passWord.getBytes());
            keygen.init(128, secureRandom);
            log.info("密钥生成器：{}",secureRandom);
            //3.产生原始对称密钥
            SecretKey originalKey = keygen.generateKey();
            log.info("原始对称密钥：{}",originalKey);
            //4.获得原始对称密钥的字节数组
            byte[] raw = originalKey.getEncoded();
            log.info("原始密钥数组：{}",raw);
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            log.info("AES密钥：{}",key);
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            log.info("AES自成密码器：{}",cipher);
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte[] byteContent = parseHexStr2Byte(content);
            log.info("解密后的字节数组：{}",byteContent);
            //byte[] byteContent = new BASE64Decoder().decodeBuffer(content);
            //解密
            byte[] byteDecode = cipher.doFinal(byteContent);
            log.info("最终解密后的结果：{}",byteDecode);
            return new String(byteDecode, "utf-8");
        } catch (NoSuchAlgorithmException e) {
            log.error("算法异常：{}",e);
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            log.error("无效填充异常：{}",e);
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            log.error("无效的密钥异常：{}",e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("IO异常：{}",e);
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            log.error("非法长度：{}",e);
            e.printStackTrace();
        } catch (BadPaddingException e) {
            log.error("错误填充异常：{}",e);
            e.printStackTrace();
        }
        //如果有错就返加nulll
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1){
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
