package org.kb.app.hashtag.service.impl;


import lombok.RequiredArgsConstructor;
import org.kb.app.hashtag.entity.Hashtag;
import org.kb.app.hashtag.entity.HashtagPost;
import org.kb.app.hashtag.enums.HashtagEnum;
import org.kb.app.hashtag.repository.HashtagRepository;
import org.kb.app.hashtag.service.HashtagService;
import org.kb.app.post.dto.request.PostRequestDto;
import org.kb.app.post.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    public List<HashtagPost> getEventStyleHashtagPosts(PostRequestDto postRequestDto) {
        List<Hashtag> events = hashtagRepository.findHashtagsWithParentIdAndNames(
                HashtagEnum.EVENT.getValue(),
                postRequestDto.getEvents().stream().collect(Collectors.toList()));

        return HashtagPost.createHashtagPosts(events);
    }

    @Override
    @Transactional
    public HashtagPost getLocationHashtagPost(String hashtagName) {
        Hashtag hashtag = hashtagRepository.findByNameAndParentId(hashtagName, HashtagEnum.LOCATION.getValue())
                .orElseGet(() -> {
                    Hashtag newLocation = Hashtag.createHashtagWithParent(
                            hashtagName,
                            hashtagRepository.findById(HashtagEnum.LOCATION.getValue()).get());
                    hashtagRepository.save(newLocation);
                    return newLocation;
                }); //존재하지 않을 때 장소 추가

        return HashtagPost.createHashtagPost(hashtag);
    }

    @Override
    @Transactional
    public void deleteAllByPosts(List<Post> posts) {
        hashtagRepository.deleteAllByPosts(posts);
    }

}
