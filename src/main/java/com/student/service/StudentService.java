package com.student.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.student.dto.StudentDto;

public interface StudentService {
	
	boolean existsByEmailIgnoreCase(String email);
		
	StudentDto createStudent(StudentDto studentDto);
	
	
	Page<StudentDto> getStudents(int page, int size);
	
	
	StudentDto getStudentById(Long id);

	StudentDto updateStudent(Long id,StudentDto studentDto);

	boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
	
	
	List<StudentDto> getAllStudents();
	
	

}
