package org.kb.app.auth.client;

public interface SocialRetrieval<T> {
    T getAttributes(String socialToken);
}
