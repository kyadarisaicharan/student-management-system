package com.student.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="courses")
@Getter
@Setter 
public class Courses {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String courseName;
	@Column(nullable = false, unique = true)
	private String courseCode;
	
	private String duration;
	@Column(name="active",nullable = false)
	private boolean active=true;
	@Column(precision = 12, scale = 2,nullable = false)
	private BigDecimal fee;
	@Column(length=1000)
	private String description;
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Enrollment> enrollments = new HashSet<>();
	
	@PrePersist
	public void onCreate() {
		createdAt= LocalDateTime.now();
	}
	
	

}
