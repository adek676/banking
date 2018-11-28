package com.staffgenics.training.banking.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "employee")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@SQLDelete(sql = "update employee set disabled = true where login = ?")
public class EmployeeEntity implements UserDetails {

  @Id
  private String login;

  private String password;

  private String name;

  private boolean disabled;

  static EmployeeEntity createInstance(EmployeeDto employeeDto, PasswordEncoder passwordEncoder){
    EmployeeEntity employeeEntity = new EmployeeEntity();
    employeeEntity.setLogin(employeeDto.getLogin());
    employeeEntity.setName(employeeDto.getName());
    employeeEntity.setPassword(passwordEncoder.encode(employeeDto.getPassword()));

    return employeeEntity;
  }

  void disable(){
    this.setDisabled(true);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getUsername() {
    return login;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !disabled;
  }
}
