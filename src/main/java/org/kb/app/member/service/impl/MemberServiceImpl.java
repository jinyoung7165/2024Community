package org.kb.app.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.kb.app.auth.dto.CustomUserDetails;
import org.kb.app.member.dto.ProfileUpdateDto;
import org.kb.app.member.entity.Member;
import org.kb.app.member.repository.MemberRepository;
import org.kb.app.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findMember(CustomUserDetails member) {
        return memberRepository.findBysocialId(member.getSocialId())
                .orElseThrow(() -> new NoSuchElementException("User Not Found"));
    }

    @Override
    public Member findMember(String socialId) {
        return memberRepository.findBysocialId(socialId)
                .orElseThrow(() -> new NoSuchElementException("User Not Found"));
    }

    @Override
    public Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User Not Found"));
    }

    @Override
    @Transactional
    public ProfileUpdateDto updateProfile(CustomUserDetails member, String nickname) {
        Member findMember = findMember(member);
        findMember.setNickname(nickname);
        return new ProfileUpdateDto(findMember.getNickname());
    }
}
