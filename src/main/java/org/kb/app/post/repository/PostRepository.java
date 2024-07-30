package org.kb.app.post.repository;


import org.kb.app.member.entity.Member;
import org.kb.app.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByMember(Member member, Pageable pageable);
    List<Post> findAllByMember(Member member, Sort sort);
    List<Post> findAllByMember(Member member);

    Optional<Post> findByIdAndMember(Long id, Member member);

    @Modifying(clearAutomatically=true, flushAutomatically=true) //bulk update 후 영속성 컨텍스트 비움
    @Query("UPDATE Post p SET p.isDeleted=true WHERE p.member=:member")
    void deleteAllByMember(@Param("member") Member member);


}
