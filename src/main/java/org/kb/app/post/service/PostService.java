package org.kb.app.post.service;

import org.kb.app.common.dto.SimpleSuccessResponse;
import org.kb.app.member.entity.Member;
import org.kb.app.post.dto.request.PostRequestDto;
import org.kb.app.post.dto.response.DetailPostResponseDto;
import org.kb.app.post.dto.response.PostPagingResponseDto;
import org.kb.app.post.dto.response.PostsResponseDto;
import org.kb.app.post.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    Post getPostById(Long id);

    SimpleSuccessResponse uploadPost(Member member, PostRequestDto postRequestDto);

    DetailPostResponseDto getPost(Member member, Long id);

    PostsResponseDto<List<PostPagingResponseDto>> getMemberPostsWithPaging(Member member, int start, int limit);

    SimpleSuccessResponse removeMyPost(Member member, Long id);

    SimpleSuccessResponse updatePost(Member member, PostRequestDto postRequestDto);

    void removeMyAllPosts(Member member);

    PostsResponseDto<List<PostPagingResponseDto>> getMemberPosts(Member member);
}
