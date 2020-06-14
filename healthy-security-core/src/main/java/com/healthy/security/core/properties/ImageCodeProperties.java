package com.healthy.security.core.properties;

/**
 * 图片验证码配置项
 */
public class ImageCodeProperties extends SmsCodeProperties {

    /**
     * 图片宽
     */
    private int width = 200;
    /**
     * 图片高
     */
    private int height = 50;
    /**
     * 字体大小
     */
    private int fontSize = 40;

    public ImageCodeProperties() {
        setLength(4);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
