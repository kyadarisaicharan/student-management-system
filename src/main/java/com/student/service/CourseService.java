package com.student.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.student.dto.CourseDto;

public interface CourseService  {
	
	CourseDto createCourse(CourseDto courseDto);
	
	
	boolean existsByCourseCode(String code);
	
	
	boolean existsByCourseCodeAndIdNot(String code, Long id);
	
	
	Page<CourseDto> getCourses(int page, int size);
	
	CourseDto getCourseById(Long id);
	
	
	CourseDto updateCourse(Long id,CourseDto courseDto);
	
	List<CourseDto> getAllCourses();


}
