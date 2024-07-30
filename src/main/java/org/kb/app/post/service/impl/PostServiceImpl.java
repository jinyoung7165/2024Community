package org.kb.app.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.app.common.dto.SimpleSuccessResponse;
import org.kb.app.hashtag.entity.HashtagPost;
import org.kb.app.hashtag.service.HashtagService;
import org.kb.app.member.entity.Member;
import org.kb.app.post.dto.request.PostRequestDto;
import org.kb.app.post.dto.response.DetailPostResponseDto;
import org.kb.app.post.dto.response.PostPagingResponseDto;
import org.kb.app.post.dto.response.PostsResponseDto;
import org.kb.app.post.entity.Post;
import org.kb.app.post.repository.PostRepository;
import org.kb.app.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final HashtagService hashtagService;

    @Override
    public Post getPostById(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> { throw new NoSuchElementException("해당 포스트를 찾을 수 없습니다."); });
    }

    @Override
    @Transactional
    public SimpleSuccessResponse uploadPost(Member member, PostRequestDto postRequestDto) {
        List<HashtagPost> hashtagPosts = getHashtagPosts(postRequestDto);

        Post post = postRequestDto.toPost(member, hashtagPosts);

        Long id = postRepository.save(post).getId(); //post등록

        return new SimpleSuccessResponse(id);
    }

    @Override
    public DetailPostResponseDto getPost(Member member, Long id) {
        Post post = getPostById(id);

        DetailPostResponseDto detailPostResponseDto = getDetailPostResponseDto(post);

        if (post.getMember().getId() == member.getId()) {
            detailPostResponseDto.setIsOwner(true);
        }

        return detailPostResponseDto;
    }


    @Override
    public PostsResponseDto<List<PostPagingResponseDto>> getMemberPostsWithPaging(Member member,
                                                                                  int page, int size) {
        Sort sort = sortByCreatedAt();
        Page<Post> allPosts = postRepository.findAllByMember(member, PageRequest.of(page, size, sort));
        List<PostPagingResponseDto> postPagingResponseDtoList = allPosts.getContent().stream()
                .map(post -> getPostResponseDto(post)).collect(Collectors.toList());
        return new PostsResponseDto(postPagingResponseDtoList);
    }

    @Override
    public PostsResponseDto<List<PostPagingResponseDto>> getMemberPosts(Member member) {
        Sort sort = sortByCreatedAt();
        List<Post> allPosts = postRepository.findAllByMember(member, sort);
        List<PostPagingResponseDto> postPagingResponseDtoList = allPosts.stream()
                .map(post -> getPostResponseDto(post)).collect(Collectors.toList());
        return new PostsResponseDto(postPagingResponseDtoList);
    }

    @Override
    @Transactional
    public SimpleSuccessResponse removeMyPost(Member member, Long id) {
        Post post = postRepository.findByIdAndMember(id, member)
                .orElseThrow(() -> { throw new NoSuchElementException("해당 포스트를 찾을 수 없습니다."); });
        postRepository.delete(post);
        return new SimpleSuccessResponse(id);
    }

    @Override
    @Transactional
    public void removeMyAllPosts(Member member) { //회원 탈퇴 시 모든 글 삭제
        List<Post> posts = postRepository.findAllByMember(member);
        hashtagService.deleteAllByPosts(posts);
        postRepository.deleteAllByMember(member);
    }

    @Override
    @Transactional
    public SimpleSuccessResponse updatePost(Member member, PostRequestDto postRequestDto) {

        Post findPost = postRepository.findByIdAndMember(postRequestDto.getId(), member)
                .orElseThrow(() -> { throw new NoSuchElementException("해당 포스트를 찾을 수 없습니다.");});

        List<HashtagPost> hashtagPosts = getHashtagPosts(postRequestDto);

        Post requestPost = postRequestDto.toPost(member, hashtagPosts);
        findPost.updatePost(requestPost); //변경 감지

        return new SimpleSuccessResponse(findPost.getId());
    }

    private List<HashtagPost> getHashtagPosts(PostRequestDto postRequestDto) {
        List<HashtagPost> hashtagPosts = new ArrayList<>();

        List<HashtagPost> eventStyles = hashtagService.getEventStyleHashtagPosts(postRequestDto);
        hashtagPosts.addAll(eventStyles);

        if (postRequestDto.getLocation() != null && !postRequestDto.getLocation().isBlank()) {
            HashtagPost location = hashtagService.getLocationHashtagPost(postRequestDto.getLocation());
            hashtagPosts.add(location);
        }
        return hashtagPosts;
    }

    private DetailPostResponseDto getDetailPostResponseDto(Post post) {
        DetailPostResponseDto detailPostResponseDto = DetailPostResponseDto.of(post);
        return detailPostResponseDto;
    }

    private PostPagingResponseDto getPostResponseDto(Post post) {
        PostPagingResponseDto postPagingResponseDto = PostPagingResponseDto.of(post);
        return  postPagingResponseDto;
    }


    private Sort sortByCreatedAt() {
        return Sort.by(Sort.Direction.DESC, "createdAt");
    }



}
