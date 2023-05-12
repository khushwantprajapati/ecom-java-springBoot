package com.ttn.ecommerce;

import com.ttn.ecommerce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EcommerceApplicationTests {
    @Autowired
    RoleRepository roleRepository;


}
