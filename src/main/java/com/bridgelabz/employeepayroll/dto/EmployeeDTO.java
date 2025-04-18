package com.bridgelabz.employeepayroll.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public @ToString class EmployeeDTO {

    @NotBlank(message = "Name must not be empty")
    @Pattern(regexp = "^[A-Z][a-zA-Z\\s]{2,}$", message = "Name must start with capital letter and be at least 3 characters")
    private String name;

    @Min(value = 1000, message = "Salary should be more than 1000")
    private double salary;

    @NotEmpty(message = "Department must not be empty")
    private List<String> department;

    @Size(max = 255, message = "Note should be less than 255 characters")
    private String note;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be a future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Pattern(regexp = "male|female|other", message = "Gender should be male, female or other")
    private String gender;

    @NotEmpty(message = "Profile Picture is required")
    private String profilePic;

}
