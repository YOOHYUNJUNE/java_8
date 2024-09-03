package com.kosta.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.damain.FileDTO;
import com.kosta.damain.PostRequest;
import com.kosta.damain.PostResponse;
import com.kosta.entity.ImageFile;
import com.kosta.entity.Post;
import com.kosta.entity.User;
import com.kosta.repository.ImageFileRepository;
import com.kosta.repository.PostRepository;
import com.kosta.repository.UserRepository;
import com.kosta.util.FileUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final ImageFileRepository imageFileRepository;
	private final FileUtils fileUtils;

	
	// 게시글(이미지 포함) 추가 -> FileUtils
	@Override
	public PostResponse insertPost(PostRequest postDTO, MultipartFile file) {
		
		if (file != null) {
			// 파일 저장후, 저장된 imageFile 객체 가져오기
			ImageFile imageFile = fileUtils.fileUpload(file);
			
			if (imageFile != null) {			
				ImageFile savedImageFile = imageFileRepository.save(imageFile);
				postDTO.setImageFile(savedImageFile);
			}
			
		}
		
		// 게시글 추가
		User user = userRepository.findById(postDTO.getAuthorId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));
		Post post = postDTO.toEntity(user);
		Post savedPost = postRepository.save(post);
		PostResponse result = PostResponse.toDTO(savedPost);
		
		return result;
	}


	// 전체 게시물 보기
	@Override
	public List<PostResponse> getAllPost() {
		List<Post> postList = postRepository.findAll();
		
		if (postList.size() > 0 ) {
//			List<PostResponse> postResponseList = postList.stream().map(post -> PostResponse.toDTO(post)).toList();
			List<PostResponse> postResponseList = postList.stream().map(PostResponse::toDTO).toList();
			return postResponseList;							
		} else {
			return new ArrayList<>();
		}		
	}


	// 게시물 ID로 불러오기
	@Override
	public PostResponse getPostById(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없음"));
		PostResponse postResponse = PostResponse.toDTO(post);
		return postResponse;
	}


	// 게시물 수정
	@Override
	public PostResponse updatePost(PostRequest postDTO) {
		// 수정할 유저 확인
		User user = userRepository.findById(postDTO.getAuthorId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));
		// 원본 글 가져오기
		Post post = postRepository.findById(postDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없음"));
		
		if(!post.getAuthor().getId().equals(user.getId())) {
			throw new IllegalArgumentException("본인의 글만 수정할 수 있습니다.");
		}
		if(!post.getPassword().equals(postDTO.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		
		if(postDTO.getTitle() != null) post.setTitle(postDTO.getTitle());
		if(postDTO.getContent() != null) post.setContent(postDTO.getContent());
		
		Post updatedPost = postRepository.save(post);
		PostResponse result = PostResponse.toDTO(updatedPost);		
		return result;
	}


	// 게시글 삭제
	@Override
	public PostResponse deletePost(Long id, PostRequest postDTO) {
		// 삭제 유저 확인
		User user = userRepository.findById(postDTO.getAuthorId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));
		// 원본 글 가져오기
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없음"));
		
		if(!post.getAuthor().getId().equals(user.getId())) {
			throw new IllegalArgumentException("본인의 글만 삭제할 수 있습니다.");
		}
		if(!post.getPassword().equals(postDTO.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		
		postRepository.delete(post);
		return PostResponse.toDTO(post);
		
	}


	// 이미지 다운로드
	@Override
	public FileDTO getImageByImageId(Long id) {
		ImageFile image = imageFileRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없음"));
		return FileDTO.toDTO(image);
	}
	
	
	
	
	
	
	
	
	
	
	
}
