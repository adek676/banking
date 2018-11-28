package com.staffgenics.training.banking.security;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Ignore
public class SecurityTest {

  @Test
  public void test(){
    PasswordEncoder bCrypt = new BCryptPasswordEncoder();
    System.out.println(bCrypt.encode("user"));
  }
}
