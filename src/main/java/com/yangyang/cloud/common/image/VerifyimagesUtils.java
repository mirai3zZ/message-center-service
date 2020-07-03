package com.yangyang.cloud.common.image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author Administrator
 * @ClassName VerifyImagesUtils
 **/
public class VerifyimagesUtils {
    /**
     * 图片的宽度
     */
    private int w = 70;
    /**
     * 图片的高度
     */
    private int h = 35;
    /**
     * 定义有那些字体
     */
    private String[] fontNames = { "宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312" };
    /**
     * 定义有那些验证码的随机字符
     */
    private String codes = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
    /**
     * 用于gettext 方法 获得生成的验证码文本
     */
    private String text;
    /**
     * 随机数对象
     */
    private Random r = new Random();
    /**
     * <p>Discription:[生成随机颜色]</p>
     * Created on 2018年3月1日 下午5:56:19
     * @return Color 返回颜色类
     * 
     */
    private Color randomColor() {
        int red = r.nextInt(150);
        int green = r.nextInt(150);
        int blue = r.nextInt(150);
        return new Color(red, green, blue);
    }
    /**
     * <p>Discription:[生成随机字体]</p>
     * Created on 2018年3月1日 下午5:56:42
     * @return Font 返回字体类
     * 
     */
    private Font randomFont() {
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];
        int style = r.nextInt(4);
        int size = r.nextInt(5) + 24;
        return new Font(fontName, style, size);
    }
    /**
     * <p>Discription:[画干扰线]</p>
     * Created on 2018年3月1日 下午5:57:00
     * @param image 图片
     * 
     */
    private void drawLine(BufferedImage image) {
        // 干扰线的个数
        int num = 3;
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            // (x1,y1)为干扰线的起始点
            int x1 = r.nextInt(w);
            int y1 = r.nextInt(h);
            // (x2,y2)为干扰线的结束点
            int x2 = r.nextInt(w);
            int y2 = r.nextInt(h);
            // 设置干扰线的宽度
            g2.setStroke(new BasicStroke(1.5F));
            // 干扰线的颜色
            g2.setColor(Color.blue);
            // 将当前这条干扰线画出来
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * <p>Discription:[得到codes的长度内的随机数 并使用charAt 取得随机数位置上的codes中的字符]</p>
     * Created on 2018年3月1日 下午5:58:25
     * @return char 返回随机字符
     * 
     */
    private char randomChar() {
        int index = r.nextInt(codes.length());
        return codes.charAt(index);
    }

    /**
     * <p>Discription:[创建一张验证码的图片]</p>
     * Created on 2018年3月1日 下午6:00:42
     * @return BufferedImage 返回一张图片
     * 
     */
    public BufferedImage createImage() {
        // BufferedImage的构造(宽度，高度和图片类型)
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        // 下面两行代码是为了解决图片背景色为黑色的问题，我设置成了白色
        g2.setColor(Color.white);
        g2.fillRect(0, 0, w, h);
        // 可变字符串存储图片里的文本
        StringBuilder sb = new StringBuilder();
        // 向图中画四个字符
        for (int i = 0; i < 4; i++) {
            String s = randomChar() + "";
            sb.append(s);
            float x = i * 1.0F * w / 4;
            g2.setFont(randomFont());
            g2.setColor(randomColor());
            g2.drawString(s, x, h - 5);
        }
        this.text = sb.toString();
        drawLine(image);
        // 返回图片
        return image;
    }

    /**
     * <p>Discription:[得到验证码的文本]</p>
     * Created on 2018年3月1日 下午6:01:00
     * @return String 返回验证码文本
     * 
     */
    public String getText() {
        return text;
    }
}

