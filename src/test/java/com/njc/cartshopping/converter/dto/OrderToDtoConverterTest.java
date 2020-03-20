package com.njc.cartshopping.converter.dto;

import com.njc.cartshopping.model.Order;
import com.njc.cartshopping.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OrderToDtoConverterTest {

    @InjectMocks
    private OrderToDtoConverter orderToDtoConverter;


    @Test
    public void convert_whenDtoIPassed_thenReturnAnOrder() {

        //Given
        final var productIdOne = 1L;
        final var productIdTwo = 2L;
        final var priceOne = 12;
        final var pricetwo = 16;
        final var productOne = Product.builder()
                .name("anyName1")
                .id(productIdOne)
                .price(priceOne)
                .build();
        final var productTwo = Product.builder()
                .name("anyName2")
                .id(productIdTwo)
                .price(pricetwo)
                .build();

        final var email = "a@a.com";
        final var orderId =10L;
        final var order = Order.builder()
                .email(email)
                .products(List.of(productOne,productTwo))
                .totalPrice(priceOne+pricetwo)
                .id(orderId)
                .build();

        //When
        final var orderDtoResponse = orderToDtoConverter.convert(order);

        //Then
        assertThat(orderDtoResponse.getProducts()).containsExactly(productIdOne,productIdTwo);
        assertThat(orderDtoResponse.getTotalPrice()).isEqualTo(priceOne+pricetwo);
        assertThat(orderDtoResponse.getOrderId()).isEqualTo(orderId);
        assertThat(orderDtoResponse.getEmail()).isEqualTo(email);
    }
}
