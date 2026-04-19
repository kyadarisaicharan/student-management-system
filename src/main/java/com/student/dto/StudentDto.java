package com.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class StudentDto {
	private Long id;
	
	@NotBlank(message = "First name is required")
	private String firstName;
	@NotBlank(message = "Last name is required")
	private String lastName;
	@NotBlank(message = "Email is required")
	private String email;
	@Size(max = 10, message ="Phone Number must be within 10 Numbers")
	private String phoneNumber;
	@Size(max = 500, message ="Address must be within 500 characters")
	private String address;
	
	private boolean active;
	
	

}
