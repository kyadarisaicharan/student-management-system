package com.student.serviceimp;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student.controller.StudentController;
import com.student.dto.CourseDto;
import com.student.dto.StudentDto;
import com.student.model.Courses;
import com.student.model.Students;
import com.student.respositity.StudentRepository;
import com.student.service.StudentService;
@Service
@Transactional
public class StudentServiceImpl implements StudentService{
	
	private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

	
	private final StudentRepository studentRepository;
	
	private final ModelMapper mapper;
	
	public StudentServiceImpl(StudentRepository studentRepository, ModelMapper mapper) {
		this.studentRepository =studentRepository;
		this.mapper=mapper;
	}

	@Override
	public boolean existsByEmailIgnoreCase(String email) {
		log.info("email from create student");
		return studentRepository.existsByEmailIgnoreCase(email);
	}

	@Override
	public StudentDto createStudent(StudentDto studentDto) {
		log.info("saving student data");
		Students students = mapper.map(studentDto, Students.class);
		Students saved = studentRepository.save(students);
		return  mapper.map(saved, StudentDto.class);
	}

	@Override
	public Page<StudentDto> getStudents(int page, int size) {
		log.info("list of student from: {}", page);
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

		return studentRepository.findByActiveTrue(pageRequest).map(student -> mapper.map(student, StudentDto.class));
	
	}

	@Override
	@Transactional(readOnly = true)
	public StudentDto getStudentById(Long id) {
		Students student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("No student found"));

		return mapper.map(student, StudentDto.class);
	}

	@Override
	public boolean existsByEmailIgnoreCaseAndIdNot(String email , Long id) {
		log.info("email from update student");
		return studentRepository.existsByEmailIgnoreCaseAndIdNot(email, id);
	}

	@Override
	public StudentDto updateStudent(Long id, StudentDto studentDto) {
		 Students student = studentRepository.findById(id)
			        .orElseThrow(() -> new RuntimeException("No student found"));

			    mapper.map(studentDto, student);

			    Students updated = studentRepository.save(student);
			    return mapper.map(updated, StudentDto.class);
	}
	
	
	@Override
	public List<StudentDto> getAllStudents() {
		return studentRepository.findByActiveTrue().stream()
				.map(student -> mapper.map(student, StudentDto.class))
				.collect(Collectors.toList());
	}

	

}
