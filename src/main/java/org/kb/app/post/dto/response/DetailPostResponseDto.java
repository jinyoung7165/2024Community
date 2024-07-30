package org.kb.app.post.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kb.app.common.utils.CustomObjectMapper;
import org.kb.app.hashtag.dto.response.HashtagResponseDto;
import org.kb.app.post.entity.Post;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DetailPostResponseDto extends PostPagingResponseDto{

    private Boolean isOwner = false;

    private String title;

    private String description;
    public static DetailPostResponseDto of(Post post) { //Entity->Dto
        DetailPostResponseDto detailPostResponseDto = CustomObjectMapper.to(post, DetailPostResponseDto.class);
        detailPostResponseDto.setHashtags(post.getHashtagPosts().stream()
                .map(hashtagPost -> new HashtagResponseDto(hashtagPost).getName())
                .collect(Collectors.toList()));
        return detailPostResponseDto;
    }
}
