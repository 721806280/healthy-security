package com.healthy.security.core.social.qq.connet;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class QQOAuth2Template extends OAuth2Template {

    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        // 添加 [text/plan] 格式的转换器
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        log.info("获取accessToke的响应：" + responseStr);
        List<String> items = StrUtil.split(responseStr, CharPool.AMP);
        String accessToken = StrUtil.subAfter(items.get(0), "=", true);
        Long expiresIn = Long.valueOf(StrUtil.subAfter(items.get(1), "=", true));
        String refreshToken = StrUtil.subAfter(items.get(2), "=", true);
        return new AccessGrant(accessToken, null, refreshToken, expiresIn);
    }
}