package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.EmployeeDTO;
import com.bridgelabz.employeepayroll.exceptions.EmployeePayrollException;
import com.bridgelabz.employeepayroll.model.Employee;
import com.bridgelabz.employeepayroll.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService implements IEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO);
        log.debug("employee data: {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        log.info("Fetching employee by Id: {}", id);
        return employeeRepository.findById(id).orElseThrow(()-> {log.error("No employee with id {} found", id);
        return new EmployeePayrollException("No employee with id " + id);});
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = getEmployeeById(id);
        employee.updateEmployee(employeeDTO);
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee by Id: {}", id);
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String department) {
        log.info("Fetching employees by department: {}", department);
        return employeeRepository.findAll().stream().filter(employee -> employee.getDepartment().contains(department)).collect(Collectors.toList());
    }
}
