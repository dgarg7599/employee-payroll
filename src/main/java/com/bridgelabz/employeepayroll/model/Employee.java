package com.bridgelabz.employeepayroll.model;

import com.bridgelabz.employeepayroll.dto.EmployeeDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public @Data class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employeeId")
    private Long employeeId;

    @Column(name = "name")
    private String name;
    private double salary;
    private String gender;
    private String profilePic;
    private LocalDate startDate;
    private String note;

    @ElementCollection
    @CollectionTable(name="employeeDepartments", joinColumns = @JoinColumn(name="employeeID"))
    @Column(name = "department")
    private List<String> department;

    public Employee(EmployeeDTO employeeDTO){
        this.updateEmployee(employeeDTO);
    }

    public void updateEmployee(EmployeeDTO employeeDTO){
        this.name = employeeDTO.getName();
        this.salary = employeeDTO.getSalary();
        this.gender = employeeDTO.getGender();
        this.profilePic = employeeDTO.getProfilePic();
        this.startDate = employeeDTO.getStartDate();
        this.note = employeeDTO.getNote();
        this.department = employeeDTO.getDepartment();
    }
}
