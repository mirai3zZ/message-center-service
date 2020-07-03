package com.yangyang.cloud.common;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yuanye
 * @date 2018/10/10
 * Desc:common method
 */
@Component
public class CommonUtil {

    private static final int DEFAULT_RANDOM_CODE = 6;

    /**
     * default random code
     *
     * @return string
     */
    public static String defaultRandomCode() {
        return generateRandomNumber(DEFAULT_RANDOM_CODE);
    }

    /**
     * generate random number that was appointed
     *
     * @param size
     * @return string
     */
    public static String generateRandomNumber(int size) {
        StringBuffer randomNumber = new StringBuffer();
        for (int i = 0; i < size; i++) {
            randomNumber.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return randomNumber.toString();
    }

    /**
     *description:判断邮箱格式是否正确
     * @param email
     * @return
     */
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
     * description:判断手机号格式是否正确
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        String ruleMobile = "^[1][3,4,5,7,8][0-9]{9}$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(ruleMobile);
        //正则表达式的匹配器
        Matcher m = p.matcher(mobile);
        //进行正则匹配
        return m.matches();
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
        StringBuilder hideInfo = new StringBuilder();
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
        if(StringUtils.isEmpty(info)){
            return info;
        }
        if (checkEmail(info)) {
            int right = info.length() - info.lastIndexOf("@");
            return hideSensitiveInfo(info, 3, right, false);
        } else if (checkMobile(info)) {
            return hideSensitiveInfo(info, 3, 4, true);
        } else {
            return info;
        }
    }

}
