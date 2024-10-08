package com.kosta.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.entity.ImageFile;

// 이미지 업로드, 수정 관련 클래스
@Component
public class FileUtils {

	// application.yml의 location 정보 가져오기
	@Value("${spring.upload.location}")
	private String uploadPath;
	
	public ImageFile fileUpload(MultipartFile file) {
		
		try {		
			String originalFileName = file.getOriginalFilename(); // 원본 파일명 가져오기
			Long fileSize = file.getSize(); // 파일 크기 가져오기			
			String savedFileName = UUID.randomUUID() + "_" + originalFileName; // 새로운 파일명 가져오기
			
			// 파일 저장
			InputStream inputStream = file.getInputStream();	
			Path path = Paths.get(uploadPath).resolve(savedFileName); // 저장 경로 설정
			
			// 이미 있는 경우 덮어쓰기
			Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
			
			// 새 ImageFile 반환
			return ImageFile.builder()
					.originalName(originalFileName)
					.savedName(savedFileName)
					.fileSize(fileSize)
					.build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}
