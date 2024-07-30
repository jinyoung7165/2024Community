package org.kb.app.hashtag.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kb.app.post.entity.Post;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hashtag_post")
public class HashtagPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore //양방향
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false) //연관관계 주인
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hashtag_id") //연관관계 주인
    private Hashtag hashtag;


    /**
     *
     * Entity 생성자 메소드
     * HashtagPost 생성자 호출 -> Hashtag 세팅
     *
     */
    public static List<HashtagPost> createHashtagPosts(List<Hashtag> hashtags) {

        List<HashtagPost> hashtagPosts = new ArrayList<>();

        for (Hashtag hashtag : hashtags) {
            HashtagPost hashtagPost = new HashtagPost();
            hashtagPost.setHashtag(hashtag);
            hashtagPosts.add(hashtagPost);
        }

        return hashtagPosts;
    }

    public static HashtagPost createHashtagPost(Hashtag hashtag) {
        HashtagPost hashtagPost = new HashtagPost();
        hashtagPost.setHashtag(hashtag);
        return hashtagPost;
    }

}
