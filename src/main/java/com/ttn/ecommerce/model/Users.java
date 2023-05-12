package com.ttn.ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(unique = true)
    private String email;

    private String firstName;
    private String middleName;
    private String lastName;

    private String password;

    private Boolean isDeleted = false;
    private Boolean isActive = false;
    private Boolean isExpired = false;
    private Boolean isLocked = false;

    private Integer invalidAttemptCount;

    private LocalDateTime passwordUpdateDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> role = new ArrayList<>();

    @OneToMany
    private List<Address> addresses;

}
