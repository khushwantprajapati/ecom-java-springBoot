package com.ttn.ecommerce.model;

import com.ttn.ecommerce.enums.Authority;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Override
    public String getAuthority() {
        return authority.toString();
    }


}
