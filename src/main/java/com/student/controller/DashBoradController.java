package com.student.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.student.service.CourseService;
import com.student.service.DashboardService;
import com.student.service.EnrollmentService;
import com.student.service.StudentService;

@Controller
public class DashBoradController {
	
	private static final Logger log = LoggerFactory.getLogger(DashBoradController.class);

   
    private final EnrollmentService enrollmentService;
    
    private final DashboardService dashboardService;

    public DashBoradController (EnrollmentService enrollmentService,DashboardService dashboardService) {
        this.enrollmentService = enrollmentService;
        this.dashboardService =dashboardService;
    }
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("dashboardStats", dashboardService.getDashboardStats());
		model.addAttribute("students", enrollmentService.getRecentlyEnrolledStudents());
		return "dashboard";
	}

}
