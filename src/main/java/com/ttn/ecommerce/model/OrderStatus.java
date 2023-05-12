package com.ttn.ecommerce.model;

import com.ttn.ecommerce.enums.Status;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode
public class OrderStatus {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status fromStatus;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status toStatus;
    @Column(nullable = false)
    private String transitionNotesComments;
    @Column(nullable = false)
    private LocalDateTime transitionDate;

    @Id
    @OneToOne
    @JoinColumn(name = "orderProductId")
    private OrderProduct orderProduct;
}
