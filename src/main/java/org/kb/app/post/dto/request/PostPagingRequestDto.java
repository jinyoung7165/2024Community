package org.kb.app.post.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPagingRequestDto {
    private Long userId;
    private int start;
    private int limit;
}
