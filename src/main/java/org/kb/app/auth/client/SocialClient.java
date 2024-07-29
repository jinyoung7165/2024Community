package org.kb.app.auth.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class SocialClient<T> implements SocialRetrieval<T> {
    protected final WebClient webClient;

    protected abstract String getBaseUri();
    protected abstract ParameterizedTypeReference<T> getAttrType();

    @Override
    public T getAttributes(String socialToken) {
        T attributes = webClient
                .get()
                .uri(getBaseUri())
                .headers(h->h.setBearerAuth(socialToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new AuthenticationServiceException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new AuthenticationServiceException("Internal Server Error")))
                .bodyToMono(getAttrType())
                .block();

        return attributes;
    }

}
