package com.student.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.student.dto.EnrollmentDto;
import com.student.dto.EnrollmentSummaryDto;

public interface EnrollmentService {
	
	void enrollStudentToCourses(EnrollmentDto enrollmentDto);
	
	Page<EnrollmentSummaryDto> getEnrolledStudents(int page, int size);
	
	EnrollmentSummaryDto findEnrolledStudentCourseDetails(Long studentId);
	
	List<EnrollmentSummaryDto> getRecentlyEnrolledStudents();




}
