package org.kb.app.member.service;


import org.kb.app.auth.dto.CustomUserDetails;
import org.kb.app.member.dto.ProfileUpdateDto;
import org.kb.app.member.entity.Member;

public interface MemberService {
    Member findMember(CustomUserDetails member);
    Member findMember(String socialId);
    Member findMember(Long id);

    ProfileUpdateDto updateProfile(CustomUserDetails member, String nickname);
}
