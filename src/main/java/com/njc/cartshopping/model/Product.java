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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;


@Builder(toBuilder = true)
@JsonDeserialize(builder = Product.ProductBuilder.class)
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "Products")
public class Product implements Serializable {

    private static final long serialVersionUID = -1000119078147252957L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "name", length = 200, unique = true)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_product_id")
    private Order orderId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ProductBuilder {
    }
}
