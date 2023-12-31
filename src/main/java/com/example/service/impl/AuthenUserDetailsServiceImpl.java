package com.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.repository.AuthenUserRepository;

@Service
public class AuthenUserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private AuthenUserRepository authenUserRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return authenUserRepo.findByUserName(username)
           .orElseThrow(() -> new UsernameNotFoundException("User name " + username + " not found"));
  }
  
}
