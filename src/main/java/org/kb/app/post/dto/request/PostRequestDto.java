package org.kb.app.post.dto.request;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kb.app.common.utils.CustomObjectMapper;
import org.kb.app.hashtag.entity.HashtagPost;
import org.kb.app.member.entity.Member;
import org.kb.app.post.entity.Post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequestDto {

    private Long id;

    @NotBlank
    @Size(min=1, max=38)
    private String title;

    @Size(max=100)
    private String description;

    private List<String> events = new ArrayList<>(); //상황

    private List<String> styles = new ArrayList<>(); //스타일

    private String location = null; //장소

    public Post toPost(Member member, List<HashtagPost> hashtagPosts) { //Dto->Entity
        Post post = CustomObjectMapper.to(this, Post.class);
        post.createPost(member, hashtagPosts);
        return post;
    }
}
