package com.njc.cartshopping.converter.entity;

import com.njc.cartshopping.dto.OrderDto;
import com.njc.cartshopping.model.Order;
import com.njc.cartshopping.model.Product;
import com.njc.cartshopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderDtoToEntity implements Converter<OrderDto, Order> {

    private final ProductRepository productRepository;

    @Autowired
    private OrderDtoToEntity(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Order convert(final OrderDto orderDto) {

        final List<Product> orderProducts = orderDto.getProducts().stream()
                .map(productRepository::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        //To control the order has all valid products
        if (orderProducts.size() != orderDto.getProducts().size()){
            throw new IllegalStateException("The number of products on our system are not equal with the order");
        }

        return Order.builder()
                .createDate(LocalDateTime.now())
                .email(orderDto.getEmail())
                .totalPrice(orderProducts.stream().mapToDouble(Product::getPrice).sum())
                .products(orderProducts)
                .build();
    }
}
