package com.healthy.security.app.authentication;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.healthy.security.core.properties.SecurityConstants;
import com.healthy.security.core.support.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录成功处理器
 */
@Slf4j
@Component("healthyAuthenticationSuccessHandler")
public class HealthyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(SecurityConstants.APPLICATION_JSON_UTF8_VALUE);
        log.info("认证成功: [{}]", authentication.getName());
        try {
            UsernamePasswordAuthenticationToken authRequest = this.authenticationConverter.convert(request);

            if (authRequest == null) {
                throw new BadCredentialsException("Empty basic authentication token");
            }

            String clientId = authRequest.getName();
            String clientSecret = authRequest.getCredentials().toString();

            if (log.isDebugEnabled()) {
                log.debug("Basic Authentication Authorization header found for user '" + clientId + "'");
            }

            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

            if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
                throw new UnapprovedClientAuthenticationException("client secret is incorrect");
            }

            TokenRequest tokenRequest = new TokenRequest(MapUtil.newHashMap(), clientId, clientDetails.getScope(), "custom");

            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

            OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

            response.getWriter().write(JSONUtil.toJsonStr(token));
        } catch (ClientRegistrationException e) {
            log.error(e.getMessage(), e);
            response.getWriter().write(JSONUtil.toJsonStr(new SimpleResponse(e.getMessage())));
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(JSONUtil.toJsonStr(new SimpleResponse(e.getMessage())));
        }
    }
}