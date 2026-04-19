package com.student.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseDto {
	
	private Long id;
	@NotBlank(message = "Course name is required.")
	@Size(max = 150,message = "Max of 150 characters allowed")
	private String courseName;
	@NotBlank(message = "Course code is required.")
	private String courseCode;
	@NotBlank(message = "Course duration is required.")
	private String duration;
	
	@NotNull(message = "Course fee is required.")
	private BigDecimal fee;
	@Size(max = 500, message = "Max of 500 characters allowed")
	private String description;
	
	
	private boolean active;
	

}
