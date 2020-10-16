package com.ssafy.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.backend.exception.ResourceNotFoundException;
import com.ssafy.backend.model.User;
import com.ssafy.backend.repository.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with userId : " + userId)
        );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserByNum(Long num) {
        User user = userRepository.findByNum(num).orElseThrow(
            () -> new ResourceNotFoundException("User", "num", num)
        );

        return UserPrincipal.create(user);
    }
}