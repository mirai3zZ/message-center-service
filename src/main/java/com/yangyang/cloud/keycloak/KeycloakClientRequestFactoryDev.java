package com.yangyang.cloud.keycloak;

import org.apache.http.client.methods.HttpUriRequest;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;

@Profile(value = "dev")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class KeycloakClientRequestFactoryDev extends KeycloakClientRequestFactory implements ClientHttpRequestFactory {

    public KeycloakClientRequestFactoryDev() {
    }

    @Override
    protected void postProcessHttpRequest(HttpUriRequest request) {
        request.setHeader("dev_mode", "true");
    }


}
