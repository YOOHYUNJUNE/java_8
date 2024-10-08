package com.kosta.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class) // 생성, 수정 날짜 추적 -> Application.java > @EnableJpaAuditing
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false, length = 1000)
	private String content;
	
	@Column(nullable = false)
	private String password;
	
	// 작성자
	@JoinColumn(name = "author_id", nullable = false)
	@ManyToOne
	private User author;
	
	
	// 이미지 (하나만 게시)
	@JoinColumn(name = "image_id", nullable = true)
	@ManyToOne// 정확히는 OneToOne
	private ImageFile image;
	
	
	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	
	
}
