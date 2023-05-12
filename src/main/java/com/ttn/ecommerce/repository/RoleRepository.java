package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.enums.Authority;
import com.ttn.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select count(*) from Role")
    Integer countAll();

    Role findByAuthority(Authority role);
}
