package com.njc.cartshopping.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@JsonDeserialize(builder = Order.OrderBuilder.class)
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    private static final long serialVersionUID = -1000136078147252937L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "email", length = 200, nullable = false)
    private String email;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @OneToMany(mappedBy="orderId", targetEntity = Product.class,fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    @JsonPOJOBuilder(withPrefix = "")
    public static class OrderBuilder {
    }
}
