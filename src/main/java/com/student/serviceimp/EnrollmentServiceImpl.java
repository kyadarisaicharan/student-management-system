package com.student.serviceimp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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

import com.student.dto.CourseDto;
import com.student.dto.EnrollmentDto;
import com.student.dto.EnrollmentSummaryDto;
import com.student.model.Courses;
import com.student.model.Enrollment;
import com.student.model.Students;
import com.student.respositity.CourseRepository;
import com.student.respositity.EnrollmentRepository;
import com.student.respositity.StudentRepository;
import com.student.service.EnrollmentService;
@Service
public class EnrollmentServiceImpl implements EnrollmentService {
	
	private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

	
	private final EnrollmentRepository enrollmentRepository;
	
	private final StudentRepository studentRepository;
	
	private final CourseRepository courseRepository;
	
	private final ModelMapper mapper;
	
	EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository ,StudentRepository studentRepository ,CourseRepository  courseRepository,ModelMapper mapper){
		this.enrollmentRepository =enrollmentRepository;
		this.studentRepository =studentRepository;
		this.courseRepository = courseRepository;
		this.mapper=mapper;
	}
	

	@Override
	public void enrollStudentToCourses(EnrollmentDto enrollmentDto) {
		log.info("request from enrollStudentToCourses");
		
		Students student = studentRepository.findById(enrollmentDto.getStudentId())
				.orElseThrow(() -> new RuntimeException("Student not found"));
		
		for (Long courseId : enrollmentDto.getCourseIds()) {
			Courses course = courseRepository.findById(courseId)
					.orElseThrow(() -> new RuntimeException("course not found"));

			if (enrollmentRepository.existsByStudentIdAndCourseId(enrollmentDto.getStudentId(), courseId)) {
				continue;
			}
			Enrollment enrollment = new Enrollment();
			enrollment.setStudent(student);
			enrollment.setCourse(course);

			student.getEnrollments().add(enrollment);
			course.getEnrollments().add(enrollment);

			enrollmentRepository.save(enrollment);

		}

	}

	@Override
	public Page<EnrollmentSummaryDto> getEnrolledStudents(int page, int size) {
		log.info("list of student from: {}", page);
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Direction.DESC, "id"));

		return studentRepository.findEnrolledStudents(pageRequest).map(student -> {
			EnrollmentSummaryDto dto = new EnrollmentSummaryDto();
			dto.setStudentId(student.getId());
			dto.setStudentName(student.getFirstName()+" "+student.getLastName());
			dto.setEmail(student.getEmail());
			
			dto.setCourseCount(student.getEnrollments().size());
			BigDecimal totalFee = student.getEnrollments().stream()
					.map(enrollment -> enrollment.getCourse().getFee())
					.filter(fee -> fee != null)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			dto.setTotalFee(totalFee);
			
			return dto;
		});
		}


	@Override
	public EnrollmentSummaryDto findEnrolledStudentCourseDetails(Long studentId) {
	
		return studentRepository.findEnrolledStudentCourseDetails(studentId)
				.map(student ->{
					EnrollmentSummaryDto dto = new EnrollmentSummaryDto();
					dto.setStudentId(student.getId());
					dto.setStudentName(student.getFirstName()+" "+student.getLastName());
					dto.setEmail(student.getEmail());
					
					dto.setCourseCount(student.getEnrollments().size());
					BigDecimal totalFee = student.getEnrollments().stream()
							.map(enrollment -> enrollment.getCourse().getFee())
							.filter(fee -> fee != null)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
					dto.setTotalFee(totalFee);
					
					List<CourseDto> courseList = student.getEnrollments().stream()
							.map(enrollment -> enrollment.getCourse())
							.map(course -> mapper.map(course, CourseDto.class))
							.collect(Collectors.toList());
							
					dto.setCourseList(courseList);
					return dto;
					
				})
				.orElseThrow(() -> new RuntimeException("Student not Found"));
	}


	@Transactional(readOnly = true)
	@Override
	public List<EnrollmentSummaryDto> getRecentlyEnrolledStudents() {

	    log.info("list of recently enrolled students");

	    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Direction.DESC, "id"));

	    return studentRepository.findEnrolledStudents(pageRequest)
	            .map(student -> {

	                EnrollmentSummaryDto dto = new EnrollmentSummaryDto();

	                dto.setStudentId(student.getId());
	                dto.setStudentName(student.getFirstName() + " " + student.getLastName());
	                dto.setEmail(student.getEmail());

	                // Safe null check for enrollments
	                int courseCount = (student.getEnrollments() != null)
	                        ? student.getEnrollments().size()
	                        : 0;
	                dto.setCourseCount(courseCount);

	                // Calculate total fee safely
	                BigDecimal totalFee = (student.getEnrollments() != null)
	                        ? student.getEnrollments().stream()
	                            .map(enrollment -> enrollment.getCourse())
	                            .filter(Objects::nonNull)
	                            .map(course -> course.getFee())
	                            .filter(Objects::nonNull)
	                            .reduce(BigDecimal.ZERO, BigDecimal::add)
	                        : BigDecimal.ZERO;

	                dto.setTotalFee(totalFee);

	                return dto;
	            })
	            .getContent();   // ✅ fixed semicolon
	}
	
	

}
