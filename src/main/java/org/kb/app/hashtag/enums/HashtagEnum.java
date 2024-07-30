package org.kb.app.hashtag.enums;

public enum HashtagEnum {

    EVENT(1L), STYLE(9L), LOCATION(10L);
    private final Long value;
    HashtagEnum(Long value) {
        this.value = value;
    }

    public Long getValue() { return value;}
}
