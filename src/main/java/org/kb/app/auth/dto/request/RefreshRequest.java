package org.kb.app.auth.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshRequest {

    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;

}
