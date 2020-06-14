package com.healthy.security.core.social.weibo.connet;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class WeiboOAuth2Template extends OAuth2Template {

    public WeiboOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(ClientHttpRequestFactorySelector.getRequestFactory());
        HttpMessageConverter<?> messageConverter = new FormHttpMessageConverter() {

            private final ObjectMapper objectMapper = new ObjectMapper();

            @Override
            public boolean canRead(Class<?> clazz, MediaType mediaType) {
                return true;
            }

            @Override
            public MultiValueMap<String, String> read(Class<? extends MultiValueMap<String, ?>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                TypeReference<Map<String, ?>> mapType = new TypeReference<Map<String, ?>>() {
                };
                LinkedHashMap<String, ?> readValue = (LinkedHashMap<String, ?>) objectMapper.readValue(inputMessage.getBody(), mapType);
                LinkedMultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
                for (Map.Entry<String, ?> currentEntry : readValue.entrySet()) {
                    result.add(currentEntry.getKey(), currentEntry.getValue().toString());
                }
                return result;
            }
        };

        restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
        return restTemplate;
    }

    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        MultiValueMap<String, String> response = getRestTemplate().postForObject(accessTokenUrl, parameters, MultiValueMap.class);
        // {access_token=[2.00UPUhhFtOjtPE9d3448d452XOqc3B], remind_in=[157679999], expires_in=[157679999], uid=[5226410992], isRealName=[false]}
        log.info("获取accessToke的响应：" + response);
        String expiresIn = response.getFirst("expires_in");
        String accessToken = response.getFirst("access_token");
        return new AccessGrant(accessToken, null, null, StrUtil.isNotBlank(expiresIn) ? Long.valueOf(expiresIn) : null);
    }
}