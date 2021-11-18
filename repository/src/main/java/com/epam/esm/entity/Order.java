package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "create_order_time", nullable = false)
    private Instant createOrderTime;

    @Column(name = "update_order_time", nullable = false)
    private Instant updateOrderTime;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_order")
    private List<OrderItem> orderItems = new ArrayList<>();

}
