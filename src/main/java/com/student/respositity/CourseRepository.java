package com.student.respositity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.student.model.Courses;

public interface CourseRepository extends JpaRepository<Courses, Long>{
	
	
	boolean existsByCourseCodeIgnoreCase(String code);
	
	
	boolean existsByCourseCodeIgnoreCaseAndIdNot(String code,Long id);
	
	
	Page<Courses> findByActiveTrue(Pageable pageable);
	
	List<Courses> findByActiveTrue(Sort sort);


}
