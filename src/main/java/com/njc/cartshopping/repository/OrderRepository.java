package com.njc.cartshopping.repository;

import com.njc.cartshopping.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCreateDateAfter(final LocalDateTime date);
}
