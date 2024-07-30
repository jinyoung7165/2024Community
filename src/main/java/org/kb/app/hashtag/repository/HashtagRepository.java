package org.kb.app.hashtag.repository;


import org.kb.app.hashtag.entity.Hashtag;
import org.kb.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String hashtagName);

    Optional<Hashtag> findByNameAndParentId(String hashtagName, Long parentId);

    @Query("SELECT h FROM Hashtag h where h.parent.id=:parent and h.name in :names")
    List<Hashtag> findHashtagsWithParentIdAndNames(@Param("parent") Long parent, @Param("names")  List<String> names);

    @Modifying(clearAutomatically=true, flushAutomatically=true) //bulk update 후 영속성 컨텍스트 비움
    @Query("DELETE FROM HashtagPost hp WHERE hp.post in :posts")
    void deleteAllByPosts(@Param("posts") List<Post> posts);

}
