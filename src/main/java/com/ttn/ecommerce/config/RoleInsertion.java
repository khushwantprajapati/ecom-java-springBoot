package com.ttn.ecommerce.config;

import com.ttn.ecommerce.enums.Authority;
import com.ttn.ecommerce.model.Role;
import com.ttn.ecommerce.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleInsertion {

    @Autowired
    RoleRepository roleRepository;

    @PostConstruct
    public void roleInsertion() {
        Integer count = roleRepository.countAll();
        if (count == 0) {
            Role a = new Role();
            a.setAuthority(Authority.CUSTOMER);
            roleRepository.save(a);

            Role b = new Role();
            b.setAuthority(Authority.SELLER);
            roleRepository.save(b);

            Role c = new Role();
            c.setAuthority(Authority.ADMIN);
            roleRepository.save(c);
        }
    }
}
