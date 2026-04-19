package com.student.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.student.dto.CourseDto;
import com.student.service.CourseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // ✅ Show Create Page
    @GetMapping("/new")
    public String showCreateCourse(Model model) {
        log.info("GET /courses/new - showing course page.");
        model.addAttribute("courseDto", new CourseDto());
        return "add-course";
    }

    // ✅ List Page (FIXED: removed wrong @RequestParam for message)
    @GetMapping("/list")
    public String listCourse(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "3") int size,
                             Model model) {

        log.info("GET /courses/list - showing course list page.");

        Page<CourseDto> courses = courseService.getCourses(page, size);
        model.addAttribute("courses", courses);

        return "courses";
    }

    // ✅ Create Course (FLASH MESSAGE FIXED)
    @PostMapping
    public String createCourse(@Valid @ModelAttribute("courseDto") CourseDto courseDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("POST /courses - create course request received.");

        if (bindingResult.hasErrors()) {
            log.error("Validation errors while creating course.");
            return "add-course";
        }

        if (courseService.existsByCourseCode(courseDto.getCourseCode())) {
            log.error("Course code must be unique.");
            bindingResult.rejectValue("courseCode", null, "Code must be unique");
            return "add-course";
        }

        courseService.createCourse(courseDto);

        // ✅ FLASH MESSAGE (correct way)
        redirectAttributes.addFlashAttribute("message", "Course created successfully");

        log.info("Course created successfully.");

        return "redirect:/courses/list";
    }

    // ✅ View Course
    @GetMapping("/{id}")
    public String getCourseByID(@PathVariable Long id, Model model) {
        CourseDto course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "view-course";
    }

    // ✅ Edit Course
    @GetMapping("/{id}/edit")
    public String editCourse(@PathVariable Long id, Model model) {
        CourseDto course = courseService.getCourseById(id);
        model.addAttribute("courseDto", course);
        return "edit-course";
    }

    // ✅ Update Course (FIXED: flash message)
    @PostMapping("/{id}/update")
    public String updateCourse(@PathVariable Long id,
                               @Valid @ModelAttribute("courseDto") CourseDto courseDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("POST /courses/{}/update - update request.", id);

        if (bindingResult.hasErrors()) {
            log.error("Validation errors while updating course.");
            return "edit-course";
        }

        if (courseService.existsByCourseCodeAndIdNot(courseDto.getCourseCode(), id)) {
            log.error("Course code must be unique.");
            bindingResult.rejectValue("courseCode", null, "Code must be unique");
            return "edit-course";
        }

        courseService.updateCourse(id, courseDto);

        // ✅ FIXED (was addAttribute before)
        redirectAttributes.addFlashAttribute("message", "Course updated successfully");

        log.info("Course updated successfully.");

        return "redirect:/courses/list";
    }
}