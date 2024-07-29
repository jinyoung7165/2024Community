package org.kb.app.auth.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class NaverClient extends SocialClient<Map<String, Object>> {
    private static final ParameterizedTypeReference<Map<String, Object>> ATTR_TYPE = new ParameterizedTypeReference<>() {};
    public NaverClient(WebClient webClient) {
        super(webClient);
    }

    @Override
    protected String getBaseUri() {
        return "https://openapi.naver.com/v1/nid/me";
    }

    @Override
    protected ParameterizedTypeReference<Map<String, Object>> getAttrType() {
        return ATTR_TYPE;
    }
}
