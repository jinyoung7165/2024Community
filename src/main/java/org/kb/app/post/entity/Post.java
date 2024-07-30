package org.kb.app.post.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.kb.app.common.entity.BaseTimeEntity;
import org.kb.app.hashtag.entity.HashtagPost;
import org.kb.app.member.entity.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false") //삭제하지 않은 post랑만 join 가능
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    //hashtagPost의 post 필드에 의해 매핑됨. 양방향
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<HashtagPost> hashtagPosts = new ArrayList<>();

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private Boolean isDeleted = false;
    /**
     *
     * 연관관계 메소드
     * hashtagPost 세팅시 HashtagPost 객체에도 Post 세팅
     */
    public void addHashtagPost(HashtagPost hashtagPost) {
        this.hashtagPosts.add(hashtagPost);
        hashtagPost.setPost(this);
    }
    //이미 생성된 Hashtag를 em에서 가져온 후, createHashtagPost 호출해 HashtagPost return-> createPost호출. Post save시 cascade 전달

    /**
     *
     * Post 생성 시 HashtagPost, Member의 필드 세팅
     */
    public void createPost(Member member, List<HashtagPost> hashtagPosts) {//나중에 tag도 넘겨받음
        this.member = member;
        hashtagPosts.stream().forEach(this::addHashtagPost);
    }

    public void updatePost(Post post) {
        this.title = post.getTitle();
        this.description = post.getDescription();

        this.hashtagPosts.clear();
        post.getHashtagPosts().stream().forEach(this::addHashtagPost);
    }

}
