package com.yangyang.cloud.keycloak;

import com.yangyang.cloud.common.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Slf4j
public class SecurityContextUtil {


    private static User defaultUser = null;

    static {
        defaultUser = new User();
        defaultUser.setName("luanxt");
        defaultUser.setId("63963d03-57a1-4fc4-a4fc-8a650f094528");
        defaultUser.setEmail("jindengke@inspur.com");
        defaultUser.setDisplayName("luanxt");
    }

    public static User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof KeycloakAuthenticationToken) {
            KeycloakAuthenticationToken tokenObj = (KeycloakAuthenticationToken) authentication;
            if (!tokenObj.isAuthenticated()) {
                throw new RuntimeException("Authenticated:false");
            }
            AccessToken token = tokenObj.getAccount().getKeycloakSecurityContext().getToken();
            String sub = token.getSubject();//用户内码
            String loginName = token.getPreferredUsername();
            String email = token.getEmail();
            User loginUser = new User();
            loginUser.setName(loginName);
            loginUser.setId(sub);
            loginUser.setEmail(email);
            loginUser.setRoles(token.getRealmAccess().getRoles());
            loginUser.setDisplayName(token.getFamilyName() + token.getGivenName());
            return loginUser;
        }
        log.info("SecurityContextUtil.getLoginUser() can not get user information!!!");
        return null;
    }

    public static void setLoginUser(User user) {
        defaultUser = user;
    }

    public static HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof KeycloakAuthenticationToken) {
            KeycloakAuthenticationToken tokenObj = (KeycloakAuthenticationToken) authentication;
            if (!tokenObj.isAuthenticated()) {
                throw new RuntimeException("Authenticated:false");
            }
            String tokenString = tokenObj.getAccount().getKeycloakSecurityContext().getTokenString();
            httpHeaders.add("Authorization", "Bearer " + tokenString);
        }
        return httpHeaders;
    }
    public static HttpHeaders getHeadersWithoutAuth() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }
}
