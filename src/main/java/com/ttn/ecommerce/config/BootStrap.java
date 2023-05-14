package com.ttn.ecommerce.config;

import com.ttn.ecommerce.enums.Authority;
import com.ttn.ecommerce.model.Role;
import com.ttn.ecommerce.model.Users;
import com.ttn.ecommerce.repository.RoleRepository;
import com.ttn.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BootStrap implements ApplicationRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Value("${admin.email}")
    String email;

    @Value("${admin.fname}")
    String fname;
    @Value("${admin.lname}")
    String lname;
    @Value("${admin.password}")
    String password;

    @Override
    public void run(ApplicationArguments args) {

        if (!roleRepository.existsByAuthority(Authority.CUSTOMER)) {
            Role a = new Role();
            a.setAuthority(Authority.CUSTOMER);
            roleRepository.save(a);
        }
        if (!roleRepository.existsByAuthority(Authority.SELLER)) {
            Role b = new Role();
            b.setAuthority(Authority.SELLER);
            roleRepository.save(b);
        }
        if (!roleRepository.existsByAuthority(Authority.ADMIN)) {
            Role c = new Role();
            c.setAuthority(Authority.ADMIN);
            roleRepository.save(c);
        }


        if (!userRepository.existsByEmail(email)) {
            // User with given email already exists, do nothing
            Users user = new Users();
            user.setFirstName(fname);
            user.setLastName(lname);
            user.setEmail(email);
            user.setIsActive(true);
            user.setPassword(passwordEncoder.encode(password));
            Role role = roleRepository.findByAuthority(Authority.ADMIN);
            user.setRole(List.of(role));
            userRepository.save(user);
        }
    }
}
