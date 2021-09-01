package com.healthy.security.core.validate.code.image;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.IoUtil;
import com.healthy.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;

/**
 * 图片验证码处理器
 */
@Component("imageValidateCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    /**
     * 发送图形验证码，将其写到响应中
     */
    @Override
    protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
        ImgUtil.writeJpg(imageCode.getImage(), request.getResponse().getOutputStream());
    }

}
