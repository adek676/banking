package com.staffgenics.training.banking.security;

import com.staffgenics.training.banking.exceptions.EmployeeAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class EmployeeService implements UserDetailsService {

  private final EmployeeRepository employeeRepository;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
    this.employeeRepository = employeeRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    return findEmployeeById(login);
  }

  @Transactional
  public String createEmployee(EmployeeDto employeeDto){
    if(findEmployeeByLogin(employeeDto.getLogin()).isPresent()){
      throw new EmployeeAlreadyExistsException("Podany uzytkownik istnieje");
    };
    EmployeeEntity employeeEntity = EmployeeEntity.createInstance(employeeDto, passwordEncoder);
    EmployeeEntity savedEntity = employeeRepository.save(employeeEntity);
    return savedEntity.getLogin();
  }

  @Transactional
  public void deleteEmployee(String login){
    EmployeeEntity employeeEntity = (EmployeeEntity)loadUserByUsername(login);
//    employeeEntity.disable();
    employeeRepository.delete(employeeEntity);
  }

  private Optional<EmployeeEntity> findEmployeeByLogin(String login){
    return employeeRepository.findEmployeeByLogin(login);
  }

  private EmployeeEntity findEmployeeById(String login){
    Optional<EmployeeEntity> employeeEntity = findEmployeeByLogin(login);
    if (!employeeEntity.isPresent()) {
      throw new UsernameNotFoundException(String.format("Użytkownik o loginie '%s' nie istnieje", login));
    }
    return employeeEntity.get();
  }
}
