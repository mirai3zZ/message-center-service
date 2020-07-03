package com.yangyang.cloud.common;

import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Desc:   工具类
 */
public class CommonUtils {

    /**
     * @return java.lang.String
     * @desc 生成uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 功能描述: 生成当前时间+三位随机数的uuid
     *
     * @author: chenxinyi
     * @date: 2018/9/29 21:14
     */
    public static BigInteger getIntUUID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timeStr = sdf.format(new Date());
        int randomInt = ThreadLocalRandom.current().nextInt(10, 100);
        return new BigInteger(timeStr + randomInt);
    }

    /**
     * description: 正则表达式判断字符串是否为邮箱格式
     *
     * @param email 传入的字符串
     * @return true ? false ?
     * @author renhaixiang
     * @date 2018/9/30 10:24
     **/
    public static boolean checkEmail(String email) {
        String ruleEmail = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(ruleEmail);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }

    /**
     * description: 正则表达式判断字符串是否为手机号格式
     * 支持中国电信获得199号段，中国移动得到198号段，中国联通得到166号段。
     *
     * @param mobile 传入的字符串
     * @return true ? false ?
     * @author renhaixiang
     * @date 2018/9/30 10:24
     **/
    public static boolean checkMobile(String mobile) {
        String ruleMobile = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(ruleMobile);
        //正则表达式的匹配器
        Matcher m = p.matcher(mobile);
        //进行正则匹配
        return m.matches();
    }

    /**
     * description: 邮箱@之前长度的一半变为*号
     *
     * @param email 邮箱
     * @return String
     * @author chenxinyi
     * @date 2018/9/30 10:24
     **/
    @Deprecated
    public static String hideEmails(String email) {
        if (!CommonUtils.checkEmail(email)) {
            return email;
        }
        //截取@前面的字符串
        String local = email.substring(0, email.lastIndexOf("@"));
        int num = 4;
        if (local.length() <= num) {
            return email.replaceAll(email.substring(email.lastIndexOf("@") - 1, email.lastIndexOf("@")), "*");
        } else {
            return email.replaceAll(email.substring(email.lastIndexOf("@") - 4, email.lastIndexOf("@")), "****");
        }
    }

    /**
     * 功能描述:  字符串压缩
     *
     * @param paramString
     * @return byte[]
     * @author chenxinyi
     * @date 2018/10/1 15:15
     */
    public static byte[] compress(String paramString) {
        if (paramString == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        byte[] arrayOfByte;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
            zipOutputStream.putNextEntry(new ZipEntry("0"));
            zipOutputStream.write(paramString.getBytes());
            zipOutputStream.closeEntry();
            arrayOfByte = byteArrayOutputStream.toByteArray();
        } catch (IOException localIOException5) {
            arrayOfByte = null;
        } finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
        return arrayOfByte;
    }

    /**
     * 功能描述:  字符串解压
     *
     * @param paramArrayOfByte
     * @return String
     * @author chenxinyi
     * @date 2018/10/1 15:15
     */
    @SuppressWarnings("unused")
    public static String decompress(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ZipInputStream zipInputStream = null;
        String str;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
            zipInputStream = new ZipInputStream(byteArrayInputStream);
            ZipEntry localZipEntry = zipInputStream.getNextEntry();
            byte[] arrayOfByte = new byte[1024];
            int i = -1;
            while ((i = zipInputStream.read(arrayOfByte)) != -1) {
                byteArrayOutputStream.write(arrayOfByte, 0, i);
            }
            str = byteArrayOutputStream.toString();
        } catch (IOException localIOException7) {
            str = null;
        } finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
        return str;
    }

    /**
     * 使用* 隐藏敏感信息
     * 保持字符串长度
     *
     * @param info
     * @param left     左边保留长度
     * @param right    右边保留长度
     * @param baseLeft 长度异常时选择保留左/右边 true为左
     * @return String
     */
    public static String hideSensitiveInfo(String info, int left, int right, boolean baseLeft) {
        if (StringUtils.isEmpty(info)) {
            return "";
        }
        StringBuilder hideInfo = new StringBuilder();
        int hiddenCharCount = info.length() - left - right;
        if (hiddenCharCount > 0) {
            String prefix = info.substring(0, left);
            hideInfo.append(prefix);
            String suffix = info.substring(info.length() - right);
            for (int i = 0; i < hiddenCharCount; i++) {
                hideInfo.append("*");
            }
            hideInfo.append(suffix);
        } else {
            if (baseLeft) {
                baseLeft(info, left, hideInfo);
            } else {
                baseRight(info, right, hideInfo);
            }
        }
        return hideInfo.toString();
    }

    /**
     * 固定隐藏长度 4
     *
     * @param info
     * @param left
     * @param right
     * @param baseLeft
     * @return
     */
    public static String hideSensitiveInfoFixHide(String info, int left, int right, boolean baseLeft) {
        String hideStr = hideSensitiveInfo(info, left, right, baseLeft);
        if (!StringUtils.isEmpty(hideStr)) {
            int defaultSize = 4;
            String prefix = info.substring(0, left);
            String suffix = info.substring(info.length() - right);
            String hideChars = hideStr.substring(left, hideStr.length() - right);
            if (hideChars.length() != defaultSize) {
                hideStr = prefix + "****" + suffix;
            }
        }
        return hideStr;
    }

    /**
     * 按照比例
     *
     * @param info
     * @param sublen
     * @param basedOnLeft
     * @return String
     */
    public static String hideSensitiveInfo(String info, int sublen, boolean basedOnLeft) {
        if (StringUtils.isEmpty(info)) {
            return "";
        }
        if (sublen <= 1) {
            sublen = 3;
        }
        int subLength = info.length() / sublen;
        if (subLength > 0 && info.length() > (subLength * 2)) {
            String prefix = info.substring(0, subLength);
            String suffix = info.substring(info.length() - subLength);
            return prefix + "****" + suffix;
        } else {
            if (basedOnLeft) {
                String prefix = subLength > 0 ? info.substring(0, subLength) : info.substring(0, 1);
                return prefix + "****";
            } else {
                String suffix = subLength > 0 ? info.substring(info.length() - subLength) : info.substring(info.length() - 1);
                return "****" + suffix;
            }
        }
    }

    private static void baseLeft(String info, int left, StringBuilder hideInfo) {
        if (info.length() > left && left > 0) {
            hideInfo.append(info.substring(0, left) + "****");
        } else {
            hideInfo.append(info.substring(0, 1) + "****");
        }
    }

    private static void baseRight(String info, int right, StringBuilder hideInfo) {
        if (info.length() > right && right > 0) {
            hideInfo.append("****" + info.substring(info.length() - right, right));
        } else {
            hideInfo.append("****" + info.substring(info.length() - 1));
        }
    }

    /**
     * 敏感信息判断
     * 手机号 邮箱
     *
     * @param info
     * @return
     */
    public static String sensitiveInfoHide(String info) {
        if (!StringUtils.isEmpty(info) && checkEmail(info)) {
            int right = info.length() - info.lastIndexOf("@");
            return hideSensitiveInfo(info, 3, right, false);
        } else if (!StringUtils.isEmpty(info) && checkMobile(info)) {
            return hideSensitiveInfo(info, 3, 4, true);
        } else {
            return info;
        }
    }

    /**
     * 获取项目的路径
     *
     * @return
     */
    public static String getRootPath() {
        String rootPath = "";
        try {
            File file = new File(CommonUtils.class.getResource("/").getFile());
            rootPath = file.getParentFile().getParentFile() + "\\";
            rootPath = java.net.URLDecoder.decode(rootPath, "utf-8");
            System.out.println(rootPath);
            return rootPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootPath;
    }

    public static String getIpV4() {
        Enumeration allNetInterfaces = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
                        .nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        if (ip.getHostAddress().equals("127.0.0.1")) {
                            continue;
                        }
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (java.net.SocketException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }
}
