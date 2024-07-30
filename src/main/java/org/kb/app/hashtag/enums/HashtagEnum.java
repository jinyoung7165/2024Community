package org.kb.app.hashtag.enums;

public enum HashtagEnum {

    EVENT(1L), LOCATION(6L);
    private final Long value;
    HashtagEnum(Long value) {
        this.value = value;
    }

    public Long getValue() { return value;}
}
