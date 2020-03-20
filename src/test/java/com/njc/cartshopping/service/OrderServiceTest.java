package com.njc.cartshopping.service;


import com.njc.cartshopping.dto.OrderDto;
import com.njc.cartshopping.model.Order;
import com.njc.cartshopping.model.Product;
import com.njc.cartshopping.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private OrderService orderService;

    @Test(expected = IllegalStateException.class)
    public void makeOrder_whenOrderIdIsHigherThan0_thenThrowIllegalStateException() {
        //Given
        final var orderDto = OrderDto.builder().orderId(1L).build();

        //When Then
        orderService.makeOrder(orderDto);
    }

    @Test
    public void makeOrder_OrderDtoIsProvided_thenSaveTheOrderOnTheSystem() {

        //Given
        final var email = "b@b.com";
        final var productId = 2L;
        final var product = Product.builder()
                .id(productId)
                .name("anyName")
                .price(10)
                .build();

        final var orderDto = OrderDto.builder()
                .email(email)
                .products(Collections.singletonList(productId))
                .build();
        final var order = Order.builder()
                .email(email)
                .products(Collections.singletonList(product))
                .build();

        given(conversionService.convert(orderDto, Order.class)).willReturn(order);

        //When
        orderService.makeOrder(orderDto);

        //Then
        then(orderRepository).should(times(1)).save(order);
    }
}
