package org.kb.app.auth.dto.response;


import lombok.*;
import org.kb.app.auth.dto.Token;
import org.kb.app.common.utils.CustomObjectMapper;


@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthResponse extends Token {
    private String nickname;
    private Boolean isNewUser;
    private Long userId;

    public static OAuthResponse of(Token token, String nickname, Boolean isNewUser, Long userId) { //Entity->Dto
        OAuthResponse authResponse = CustomObjectMapper.to(token, OAuthResponse.class);
        authResponse.nickname = nickname;
        authResponse.isNewUser = isNewUser;
        authResponse.userId = userId;
        return authResponse;
    }
}


