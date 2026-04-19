package com.student.dto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashboardStatsDto {
	
		private long totalStudents;
		
		private long totalCourses;
		
		private String topPerformingCourse;
		
		private long studentsEnrolledThisMonth;

}



