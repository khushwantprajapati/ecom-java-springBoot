package com.ttn.ecommerce.config;

import com.ttn.ecommerce.model.Role;
import com.ttn.ecommerce.model.Users;
import com.ttn.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> role) {
        return role.stream().map(roles -> new SimpleGrantedAuthority(roles.getAuthority())).collect(Collectors.toList());
    }
}