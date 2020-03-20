package com.njc.cartshopping.converter.entity;

import com.njc.cartshopping.dto.OrderDto;
import com.njc.cartshopping.model.Product;
import com.njc.cartshopping.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OrderDtoToEntityTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderDtoToEntity orderDtoToEntity;

    @Test(expected = IllegalStateException.class)
    public void convert_whenOrderWithProductsAreNotTheSame_thenThrowAnException() {

        //Given
        final var productId = 1L;
        final var orderDto = OrderDto.builder()
                .email("a@a.com")
                .products(List.of(productId))
                .build();
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        //When Then
        orderDtoToEntity.convert(orderDto);
    }

    @Test
    public void convert_whenOrderWithProductsAreTheSameAndFoundOnDB_thenReturnProperlyDto() {

        //Given
        final var productIdOne = 1L;
        final var productIdTwo = 2L;
        final var nameOne = "nameOne";
        final var nameOTwo = "nameTwo";
        final var priceOne = 50;
        final var priceTwo = 25;

        final var productOne = Product.builder()
                .id(productIdOne)
                .name(nameOne)
                .price(priceOne)
                .createDate(LocalDateTime.now())
                .build();
        final var productTwo = Product.builder()
                .id(productIdTwo)
                .name(nameOTwo)
                .price(priceTwo)
                .createDate(LocalDateTime.now())
                .build();

        final var email = "a@a.com";
        final var orderDto = OrderDto.builder()
                .email(email)
                .products(List.of(productIdOne,productIdTwo))
                .build();

        given(productRepository.findById(productIdOne)).willReturn(Optional.of(productOne));
        given(productRepository.findById(productIdTwo)).willReturn(Optional.of(productTwo));

        //When
        final var order = orderDtoToEntity.convert(orderDto);

        //Then
        assertThat(order).hasFieldOrPropertyWithValue("email",email);
        assertThat(order.getTotalPrice()).isEqualTo(priceOne+priceTwo);
        assertThat(order.getProducts()).containsExactly(productOne,productTwo);
    }
}
