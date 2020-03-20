package com.njc.cartshopping.converter.dto;

import com.njc.cartshopping.dto.OrderDto;
import com.njc.cartshopping.model.Order;
import com.njc.cartshopping.model.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderToDtoConverter implements Converter<Order, OrderDto> {

    @Override
    public OrderDto convert(final Order order) {
        return OrderDto.builder()
                .orderId(order.getId())
                .email(order.getEmail())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getCreateDate())
                .products(order.getProducts().stream()
                        .map(Product::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
