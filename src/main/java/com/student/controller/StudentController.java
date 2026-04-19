package com.student.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.student.dto.CourseDto;
import com.student.dto.StudentDto;
import com.student.service.StudentService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/students")
public class StudentController {

	private static final Logger log = LoggerFactory.getLogger(StudentController.class);

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping("/new")
	public String showCreateStudent(Model model) {
		log.info("Get/new - showing create student page");

		model.addAttribute("studentDto", new StudentDto());

		return "add-student";
	}

	@GetMapping("/list")
	public String listStudent(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,
			Model model, @RequestParam(value = "message", required = false) String message) {
		log.info("Get/list - showing student list page");

		Page<StudentDto> students = studentService.getStudents(page, size);
		model.addAttribute("students", students);
		model.addAttribute("currentPage", page);
		
		model.addAttribute("totalPages", students.getTotalPages());
		model.addAttribute("message", message);

		return "students";
	}

	@PostMapping("/save")
	public String createStudent(@Valid @ModelAttribute("studentDto") StudentDto studentDto, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttributes) {
		log.info("Post/save - create student request received");

		if (bindingResult.hasErrors()) {
			return "add-student";
		}
		if (studentService.existsByEmailIgnoreCase(studentDto.getEmail())) {
			log.error("Post /save - email must be unique.");

			bindingResult.rejectValue("email", null, "email must be unique");
			return "add-student";

		}

		studentService.createStudent(studentDto);
		redirectAttributes.addAttribute("message", "Student is added successfully");
		return "redirect:/students/list";
	}

	@GetMapping("/{id}")
	public String getStudentByID(@PathVariable Long id, Model model) {
		StudentDto student = studentService.getStudentById(id);
		model.addAttribute("student", student);
		return "view-student";
	}

	@GetMapping("/{id}/edit")
	public String editStudent(@PathVariable Long id, Model model) {
		StudentDto student = studentService.getStudentById(id);
		model.addAttribute("studentDto", student);
		return "edit-student";
	}

	@PostMapping("/{id}/update")
	public String updateStudent(@PathVariable Long id,
	                             @Valid @ModelAttribute("studentDto") StudentDto studentDto,
	                             BindingResult bindingResult,
	                             Model model,
	                             RedirectAttributes redirectAttributes) {

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("studentDto", studentDto);
	        return "edit-student";
	    }

	    if (studentService.existsByEmailIgnoreCaseAndIdNot(studentDto.getEmail(), id)) {
	        bindingResult.rejectValue("email", null, "email must be unique");
	        return "edit-student";
	    }

	    studentService.updateStudent(id, studentDto);

	    redirectAttributes.addFlashAttribute("message", "Student updated successfully");
	    return "redirect:/students/list";
	}
}
