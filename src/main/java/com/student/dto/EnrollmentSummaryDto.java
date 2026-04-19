package com.student.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EnrollmentSummaryDto {
	
	private Long studentId;
	
	private String studentName;
	
	private String email;
	
	private int courseCount;
	
	private BigDecimal totalFee;
	
	private List<CourseDto> courseList = new ArrayList<>();

}
