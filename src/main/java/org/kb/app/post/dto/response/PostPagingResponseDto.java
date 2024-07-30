package org.kb.app.post.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kb.app.common.utils.CustomObjectMapper;
import org.kb.app.hashtag.dto.response.HashtagResponseDto;
import org.kb.app.post.entity.Post;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostPagingResponseDto {

    private Long id;
    private List<String> hashtags;

    public static PostPagingResponseDto of(Post post) { //Entity->Dto
        PostPagingResponseDto postPagingResponseDto = CustomObjectMapper.to(post, PostPagingResponseDto.class);
        postPagingResponseDto.hashtags = post.getHashtagPosts().stream()
                .map(hashtagPost -> new HashtagResponseDto(hashtagPost).getName())
                .collect(Collectors.toList());
        return postPagingResponseDto;
    }
}

