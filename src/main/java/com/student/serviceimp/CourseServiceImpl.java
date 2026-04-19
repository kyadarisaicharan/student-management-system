package com.student.serviceimp;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student.controller.CourseController;
import com.student.dto.CourseDto;
import com.student.model.Courses;
import com.student.respositity.CourseRepository;
import com.student.service.CourseService;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

	private static final Logger log = LoggerFactory.getLogger(CourseService.class);

	private final CourseRepository courseRepository;
	private final ModelMapper mapper;

	public CourseServiceImpl(CourseRepository courseRepository, ModelMapper mapper) {
		this.courseRepository = courseRepository;
		this.mapper = mapper;
	}

	@Override
	public CourseDto createCourse(CourseDto courseDto) {
		log.info("createing course with code: {}", courseDto.getCourseCode());

		Courses courses = mapper.map(courseDto, Courses.class);
		Courses savedCourse = courseRepository.save(courses);
		return mapper.map(savedCourse, CourseDto.class);
	}

	@Override
	public boolean existsByCourseCode(String code) {
		log.info("checking if code exists: {}", code);
		return courseRepository.existsByCourseCodeIgnoreCase(code);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CourseDto> getCourses(int page, int size) {
		log.info("list of course from: {}", page);
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Direction.DESC, "id"));

		return courseRepository.findByActiveTrue(pageRequest).map(course -> mapper.map(course, CourseDto.class));
	}

	@Override
	public CourseDto updateCourse(Long id, CourseDto courseDto) {

	    Courses course = courseRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("No course found with id: " + id));

	    mapper.map(courseDto, course);

	    Courses updated = courseRepository.save(course);

	    return mapper.map(updated, CourseDto.class);
	}

	@Override
	public boolean existsByCourseCodeAndIdNot(String code, Long id) {
		log.info("code from update page: {},id: {}", code, id);
		return courseRepository.existsByCourseCodeIgnoreCaseAndIdNot(code, id);
	}

	@Override
	@Transactional(readOnly = true)
	public CourseDto getCourseById(Long id) {
		Courses course = courseRepository.findById(id)
							.orElseThrow(() -> new RuntimeException("No course found"));
		
		return mapper.map(course, CourseDto.class);
	}

	@Override
	public List<CourseDto> getAllCourses() {
		return courseRepository.findByActiveTrue(Sort.by("courseName")).stream()
				.map(course -> mapper.map(course, CourseDto.class))
				.collect(Collectors.toList());
	}

	
	
	
	
	
	
	
	
	
}