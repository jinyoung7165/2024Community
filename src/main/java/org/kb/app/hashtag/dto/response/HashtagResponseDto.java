package org.kb.app.hashtag.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kb.app.hashtag.entity.HashtagPost;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HashtagResponseDto {

    private String name;

    public HashtagResponseDto(HashtagPost hashtagPost) {
        this.name = hashtagPost.getHashtag().getName();
    }
}
