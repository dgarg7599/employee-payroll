package com.bridgelabz.employeepayroll.controller;

import com.bridgelabz.employeepayroll.dto.EmployeeDTO;
import com.bridgelabz.employeepayroll.dto.ResponseDTO;
import com.bridgelabz.employeepayroll.model.Employee;
import com.bridgelabz.employeepayroll.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Create Employees
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("Creating Employee with name : {}", employeeDTO.getName());
        Employee employee = employeeService.createEmployee(employeeDTO);
        ResponseDTO responseDTO = new ResponseDTO("Employee created successfully", employee);
        log.info("Employee Created Successfully with ID: {}", employee.getEmployeeId());
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.CREATED);
    }

    // Get all Employees
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllEmployees() {
        log.info("Fetching all Employees...");
        List<Employee> employees = employeeService.getAllEmployees();
        ResponseDTO responseDTO = new ResponseDTO("List of all Employees", employees);
        log.info("Fetched {} Employees.", employees.size());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Get Employee By ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getEmployee(@PathVariable Long id) {
        log.info("Fetching Employee with ID: {}", id);
        Employee employee = employeeService.getEmployeeById(id);
        ResponseDTO responseDTO = new ResponseDTO("Employee found", employee);
        log.info("Employee found: {}", employee.getEmployeeId());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // update employee
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("Updating Employee with ID: {}", id);
        Employee employee = employeeService.updateEmployee(id, employeeDTO);
        ResponseDTO responseDTO = new ResponseDTO("Employee updated successfully", employee);
        log.info("Employee Updated Successfully with ID: {}", employee.getEmployeeId());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteEmployee(@PathVariable Long id) {
        log.info("Deleting Employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        ResponseDTO responseDTO = new ResponseDTO("Employee deleted successfully", id);
        log.info("Employee Deleted Successfully with ID: {}", id);
        return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
    }

    // get employee by department
    @GetMapping("/department/{dept}")
    public ResponseEntity<ResponseDTO> getEmployeesByDepartment(@PathVariable String dept) {
        log.info("Fetching Employees by Department: {}", dept);
        List<Employee> employees = employeeService.getEmployeesByDepartment(dept);
        ResponseDTO responseDTO = new ResponseDTO("List of Employees", employees);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
