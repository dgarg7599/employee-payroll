package com.bridgelabz.employeepayroll.controller;

import com.bridgelabz.employeepayroll.dto.EmployeeDTO;
import com.bridgelabz.employeepayroll.dto.ResponseDTO;
import com.bridgelabz.employeepayroll.model.Employee;
import com.bridgelabz.employeepayroll.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseDTO createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.createEmployee(employeeDTO);
        return new ResponseDTO("Employee Created Successfully", employee);
    }

    @GetMapping
    public ResponseDTO getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseDTO("List of All Employees", employees);
    }

    @GetMapping("/{id}")
    public ResponseDTO getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return new ResponseDTO("Employee Found Successfully", employee);
    }

    @PutMapping("/{id}")
    public ResponseDTO updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.updateEmployee(id, employeeDTO);
        return new ResponseDTO("Employee Updated Successfully", employee);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseDTO("Employee Deleted Successfully", null);
    }

}
