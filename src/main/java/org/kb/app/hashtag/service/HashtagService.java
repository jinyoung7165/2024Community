package org.kb.app.hashtag.service;



import org.kb.app.hashtag.entity.HashtagPost;
import org.kb.app.post.dto.request.PostRequestDto;
import org.kb.app.post.entity.Post;

import java.util.List;

public interface HashtagService {

    List<HashtagPost> getEventStyleHashtagPosts(PostRequestDto postRequestDto);

    HashtagPost getLocationHashtagPost(String hashtagName);

    void deleteAllByPosts(List<Post> posts);
}
