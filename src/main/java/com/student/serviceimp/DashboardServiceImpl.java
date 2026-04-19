package com.student.serviceimp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.student.dto.DashboardStatsDto;
import com.student.respositity.CourseRepository;
import com.student.respositity.EnrollmentRepository;
import com.student.respositity.StudentRepository;
import com.student.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

	private final EnrollmentRepository enrollmentRepository;

	private final StudentRepository studentRepository;

	private final CourseRepository courseRepository;

	DashboardServiceImpl(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository,
			CourseRepository courseRepository) {
		this.enrollmentRepository = enrollmentRepository;
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public DashboardStatsDto getDashboardStats() {
		long totalStudets = studentRepository.count();
		long totalCourse = courseRepository.count();
		String topPerformingCourse = getTopPerformingCourse();
		
		YearMonth currentMonth = YearMonth.now();
		LocalDateTime starDate = currentMonth.atDay(1).atStartOfDay();
		LocalDateTime endDate = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);
		
		long studentEnrolledThisMonth =enrollmentRepository.countDistinctStudentByEnrollDateBetween(starDate, endDate);
		DashboardStatsDto dashboardStatsDto = new DashboardStatsDto();
		dashboardStatsDto.setTotalStudents(totalStudets);
		dashboardStatsDto.setTotalCourses(totalCourse);
		dashboardStatsDto.setTopPerformingCourse(topPerformingCourse);
		dashboardStatsDto.setStudentsEnrolledThisMonth(studentEnrolledThisMonth);

		return dashboardStatsDto;
	}

	private String getTopPerformingCourse() {

		return enrollmentRepository.findAll().stream()
				.collect(Collectors.groupingBy(e -> e.getCourse().getCourseCode(), Collectors.counting())).entrySet()
				.stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");
	}

}
