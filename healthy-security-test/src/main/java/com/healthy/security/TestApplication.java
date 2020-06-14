package com.healthy.security;

import com.healthy.security.core.support.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * SpringBootApplication中的scanBasePackages指定扫描包
 */
@SpringBootApplication
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestApplication {

    // private final TokenStore redisTokenStore;

    private final ProviderSignInUtils providerSignInUtils;

    /* private final AppSignInUtils appSignInUtils; */

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @ResponseBody
    @GetMapping("/user/me")
    public Authentication me(@AuthenticationPrincipal Authentication authentication) {
        return authentication;
    }

    @ResponseBody
    @GetMapping("/user/me2")
    public Object me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return principal;
    }

    /*@ResponseBody
    @PostMapping("/logouts")
    public ResponseEntity logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();

        OAuth2AccessToken oAuth2AccessToken = redisTokenStore.readAccessToken(oAuth2AuthenticationDetails.getTokenValue());

        OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();

        // 删除访问令牌
        redisTokenStore.removeAccessToken(oAuth2AccessToken);
        // 删除刷新令牌
        redisTokenStore.removeRefreshToken(oAuth2RefreshToken);
        // 通过刷新令牌删除访问令牌
        redisTokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);

        return ResponseEntity.ok(new SimpleResponse("exit success."));
    }*/

    @ResponseBody
    @PostMapping("/user/regist")
    public void regist(HttpServletRequest request) {
        providerSignInUtils.doPostSignUp("admin", new ServletWebRequest(request));
        // appSignInUtils.doPostSignUp("admin", new ServletWebRequest(request));
    }
}
