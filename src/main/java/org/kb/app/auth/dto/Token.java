package org.kb.app.auth.dto;

import lombok.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Token {

    private String accessToken;
    private String refreshToken;

}

