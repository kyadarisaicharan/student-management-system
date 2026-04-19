package com.student.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollmentDto {
	@NotNull(message = "Student is required")
	private Long studentId;
	@NotEmpty(message = "Select at least one course")
	private List<Long> courseIds = new ArrayList<>();
	

}
