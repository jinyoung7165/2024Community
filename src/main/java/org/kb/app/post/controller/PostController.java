package org.kb.app.post.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kb.app.auth.dto.CustomUserDetails;
import org.kb.app.common.dto.ApiResponse;
import org.kb.app.common.dto.SimpleSuccessResponse;
import org.kb.app.member.service.MemberService;
import org.kb.app.post.dto.request.PostPagingRequestDto;
import org.kb.app.post.dto.request.PostRequestDto;
import org.kb.app.post.dto.response.DetailPostResponseDto;
import org.kb.app.post.dto.response.PostPagingResponseDto;
import org.kb.app.post.dto.response.PostsResponseDto;
import org.kb.app.post.service.PostService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "Post를 조회하고 생성합니다")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;


    @Tag(name = "Post")
    @GetMapping("/{id}")
    @Operation(summary = "특정 id의 post 조회", description = "Post ID를 입력받아 세부 정보를 조회하는 API")
    public ResponseEntity<DetailPostResponseDto> getPost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @PathVariable("id") Long id) {
        return ApiResponse.success(postService.getPost(memberService.findMember(userDetails), id));
    }

    @Tag(name = "Post")
    @PostMapping() //img까지 첨부 - formData의 경우 @RequsetBody적지 말고, Dto에 반드시 Setter열어놔야함
    @Operation(summary = "post 생성", description = "Post를 생성하는 API")
    public ResponseEntity<SimpleSuccessResponse> uploadPost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody @Valid PostRequestDto postRequestDto) throws Exception {
        return ApiResponse.created(postService.uploadPost(
                memberService.findMember(userDetails),
                postRequestDto));
    }

    @Tag(name = "Post")
    @GetMapping()
    @Operation(summary = "start부터 limit까지 특정 user의 post 조회", description = "start부터 limit까지 특정 user의 post 조회 API")
    public ResponseEntity<PostsResponseDto<List<PostPagingResponseDto>>> getMemberPostsWithPaging(PostPagingRequestDto pagingRequestDto) {
        return ApiResponse.success(postService.getMemberPostsWithPaging(
                memberService.findMember(pagingRequestDto.getUserId()),
                pagingRequestDto.getStart(), pagingRequestDto.getLimit()));
    }

    @Tag(name = "Post")
    @GetMapping("/all")
    @Operation(summary = "특정 user의 post 전체 조회", description = "특정 user의 post 조회 API")
    public ResponseEntity<PostsResponseDto<List<PostPagingResponseDto>>> getMemberPosts(@Param("userId") Long userId) {
        return ApiResponse.success(postService.getMemberPosts(memberService.findMember(userId)));
    }

    @Tag(name = "Post")
    @GetMapping("/my")
    @Operation(summary = "내 post 전체 조회", description = "특정 user의 post 조회 API")
    public ResponseEntity<PostsResponseDto<List<PostPagingResponseDto>>> getMyPosts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.success(postService.getMemberPosts(memberService.findMember(userDetails)));
    }

    @Tag(name = "Post")
    @DeleteMapping("/{id}")
    @Operation(summary = "글 삭제", description = "내가 쓴 글 삭제")
    public ResponseEntity<SimpleSuccessResponse> deletePost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable("id") Long id) {
        return ApiResponse.success(postService.removeMyPost(
                memberService.findMember(userDetails),
                id
        ));
    }

    @Tag(name = "Post")
    @PutMapping(value = "/{id}")
    @Operation(summary = "글 수정", description = "내가 쓴 글 수정")
    public ResponseEntity<SimpleSuccessResponse> updatePost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody @Valid PostRequestDto postRequestDto) throws Exception {
        return ApiResponse.success(postService.updatePost(
                memberService.findMember(userDetails),
                postRequestDto));
    }
}
