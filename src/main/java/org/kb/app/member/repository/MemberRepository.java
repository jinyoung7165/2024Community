package org.kb.app.member.repository;


import org.kb.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBysocialId(String socialId);

    @Query("SELECT m.refreshToken FROM Member m WHERE m.socialId=:socialId")
    String getRefreshTokenBySocialId(@Param("socialId") String socialId);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.refreshToken=:token WHERE m.socialId=:socialId")
    void updateRefreshToken(@Param("socialId") String socialId, @Param("token") String token);


}