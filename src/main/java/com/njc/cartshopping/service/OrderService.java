package com.njc.cartshopping.service;

import com.njc.cartshopping.dto.OrderDto;
import com.njc.cartshopping.model.Order;
import com.njc.cartshopping.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ConversionService conversionService;

    @Autowired
    private OrderService(final OrderRepository orderRepository, final ConversionService conversionService) {
        this.orderRepository = orderRepository;
        this.conversionService = conversionService;
    }

    public OrderDto makeOrder(final OrderDto orderDto) {

        if (orderDto.getOrderId() > 0){
           throw  new IllegalStateException("The order Id must be null");
        }

        final Order order = conversionService.convert(orderDto, Order.class);
        return conversionService.convert(orderRepository.save(order), OrderDto.class);
    }

    public List<OrderDto> findOrder(final LocalDateTime date) {

        return orderRepository.findByCreateDateAfter(date).stream()
                .map(order -> conversionService.convert(order, OrderDto.class))
                .collect(Collectors.toList());
    }
}
