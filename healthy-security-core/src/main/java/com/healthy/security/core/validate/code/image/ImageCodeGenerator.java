package com.healthy.security.core.validate.code.image;

import cn.hutool.captcha.CircleCaptcha;
import com.healthy.security.core.properties.ImageCodeProperties;
import com.healthy.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 默认的图片验证码生成器
 */
public class ImageCodeGenerator implements ValidateCodeGenerator {

    private ImageCodeProperties imageCodeProperties;

    @Override
    public ImageCode generate(ServletWebRequest request) {
        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width", imageCodeProperties.getWidth());
        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height", imageCodeProperties.getWidth());
        CircleCaptcha captcha = new CircleCaptcha(width, height, imageCodeProperties.getLength());
        return new ImageCode(captcha.getImage(), captcha.getCode(), imageCodeProperties.getExpireIn());
    }

    public void setImageCodeProperties(ImageCodeProperties imageCodeProperties) {
        this.imageCodeProperties = imageCodeProperties;
    }
}
