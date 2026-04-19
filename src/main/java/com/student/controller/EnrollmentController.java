package com.student.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.student.dto.EnrollmentDto;
import com.student.dto.EnrollmentSummaryDto;
import com.student.service.CourseService;
import com.student.service.EnrollmentService;
import com.student.service.StudentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentController.class);

    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    public EnrollmentController(CourseService courseService,
                                StudentService studentSerive,
                                EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.studentService = studentSerive;
        this.enrollmentService = enrollmentService;
    }

    // ✅ FIXED MAPPING
    @GetMapping("/enroll")
    public String showEnroll(Model model) {
        log.info("GET /enrollments/enroll - showing enrollment page.");
        model.addAttribute("enrollmentDto", new EnrollmentDto());
        model.addAttribute("courseList", courseService.getAllCourses());
        model.addAttribute("studentList", studentService.getAllStudents());
        return "enroll-course";
    }

    @GetMapping("/enrollmentList")
    public String enrollmentList(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "3") int size,
                                 Model model,
                                 @RequestParam(value = "message", required = false) String message) {

        log.info("GET /enrollmentList - showing enrolled student list page");

        Page<EnrollmentSummaryDto> students = enrollmentService.getEnrolledStudents(page, size);
        model.addAttribute("students", students);
        model.addAttribute("message", message);

        return "enrolled-students";
    }

    @PostMapping("/enrollCourse")
    public String enrollCourse(@Valid @ModelAttribute("enrollmentDto") EnrollmentDto enrollmentDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        log.info("POST /enrollments/enrollCourse - enrollment request received.");

        if (bindingResult.hasErrors()) {
            model.addAttribute("courseList", courseService.getAllCourses());
            model.addAttribute("studentList", studentService.getAllStudents());
            return "enroll-course";
        }

        enrollmentService.enrollStudentToCourses(enrollmentDto);

        redirectAttributes.addAttribute("message", "Enrollment successfully.");

        log.info("POST /enrollments/enrollCourse - Enrollment successfully.");

        return "redirect:/enrollments/enrollmentList";
    }

    @GetMapping("/getStudentEnrollmentDetails/{id}")
    public String getStudentEnrollmentDetails(@PathVariable Long id, Model model,
    		@RequestParam(defaultValue = "enrollments")String source) {

        EnrollmentSummaryDto enrollmentSummaryDto =
                enrollmentService.findEnrolledStudentCourseDetails(id);

        model.addAttribute("enrollmentSummaryDto", enrollmentSummaryDto);
        model.addAttribute("source", source);

        return "enrollment-details";
    }
}