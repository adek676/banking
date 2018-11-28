package com.staffgenics.training.banking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

  private EmployeeService employeeService;

  @Autowired
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @RequestMapping(value = "/employee", method = RequestMethod.POST)
  public String createEmployee(@RequestBody EmployeeDto employeeDto){
    return employeeService.createEmployee(employeeDto);
  }

  @RequestMapping(value = "employee/{login}", method = RequestMethod.DELETE)
  public void deleteEmployee(@PathVariable String login){
    employeeService.deleteEmployee(login);
  }
}
