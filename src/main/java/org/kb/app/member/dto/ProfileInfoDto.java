package org.kb.app.member.dto;

import lombok.*;
import org.kb.app.auth.enums.Gender;
import org.kb.app.auth.enums.Social;
import org.kb.app.common.utils.CustomObjectMapper;
import org.kb.app.member.entity.Member;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileInfoDto {

    private Long id;

    private String nickname;

    private Social social;

    private String email;

    private Gender gender;

    private Short birthyear;


    public static ProfileInfoDto of(Member member) { //Entity->Dto
        ProfileInfoDto profileResponse = CustomObjectMapper.to(member, ProfileInfoDto.class);
        return profileResponse;
    }
}
