package com.njc.cartshopping.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.njc.cartshopping.converter.entity.OrderDtoToEntity;
import com.njc.cartshopping.dto.OrderDto;
import com.njc.cartshopping.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.njc.cartshopping.controller.OrderController.ORDER_PATH;
import static java.util.Collections.EMPTY_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@RunWith(SpringRunner.class)
public class OrderControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderDtoToEntity orderDtoToEntity;

    @Test
    public void makeOrder_whenAllRequiredDataIsPassed_thenReturnOrderIdAndHttp201() throws Exception {

        //Given
        final var orderId = 1L;
        final var productId = 5L;
        final var totalPrice = 10;
        final var email = "a@a.com";
        final var orderDate = LocalDateTime.now();
        final var productList = Collections.singletonList(productId);
        final var order = OrderDto.builder().email(email).build();
        final var expectedOrder = OrderDto.builder()
                .email(email)
                .orderDate(orderDate)
                .orderId(orderId)
                .products(productList)
                .totalPrice(totalPrice)
                .build();

        given(orderService.makeOrder(order)).willReturn(expectedOrder);

        //When
        final var response = this.mockMvc
                .perform(post(ORDER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        //Then
        final var jsonResponse = this.objectMapper.readValue(response, OrderDto.class);
        assertThat(jsonResponse.getOrderDate()).isEqualTo(orderDate);
        assertThat(jsonResponse.getEmail()).isEqualTo(email);
        assertThat(jsonResponse.getOrderId()).isEqualTo(orderId);
        assertThat(jsonResponse.getProducts()).containsExactly(productId);
        assertThat(jsonResponse.getTotalPrice()).isEqualTo(totalPrice);
    }

    @Test
    public void makeOrder_whenAEmailIsIncorrect_thenReturnAnError() throws Exception {

        //Given
        final var email = "incorrectEmail.com";
        final var order = OrderDto.builder().email(email).build();

        //When Then
        this.mockMvc.perform(post(ORDER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(order)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findOrders_whenDateIsProvided_thenReturnListOfOrdersAndHttp200() throws Exception {

        //Given
        final var searchDate = LocalDate.now();

        final var orderId = 1L;
        final var productIdA = 5L;
        final var productIdB = 6L;
        final var totalPrice = 10;
        final var email = "a@a.com";
        final var orderDate = LocalDateTime.now();
        final var productList = List.of(productIdA,productIdB);

        final var expectedOrderOne = OrderDto.builder()
                .email(email)
                .orderDate(orderDate)
                .orderId(orderId)
                .products(productList)
                .totalPrice(totalPrice)
                .build();

        final var orderIdTwo = 2L;
        final var productIdC = 7L;
        final var productIdD = 8L;
        final var totalPriceTwo = 20;
        final var emailTwo = "b@b.com";
        final var orderDateTwo = LocalDateTime.now();
        final var productListOrderTwo = List.of(productIdC,productIdD);
        final var expectedOrderTwo = OrderDto.builder()
                .email(emailTwo)
                .orderDate(orderDateTwo)
                .orderId(orderIdTwo)
                .products(productListOrderTwo)
                .totalPrice(totalPriceTwo)
                .build();

        given(orderService.findOrder(any(LocalDateTime.class))).willReturn(List.of(expectedOrderOne, expectedOrderTwo));

        //When
        final var response = this.mockMvc
                .perform(get(ORDER_PATH)
                .param("date", searchDate.toString()))
                .andExpect(status().isOk())
                .andReturn();


        //Then
        final var actual = objectMapper.readValue(response.getResponse().getContentAsString(),
                new TypeReference<List<OrderDto>>() {});

        assertThat(actual).containsExactly(expectedOrderOne,expectedOrderTwo);
    }


    @Test
    public void findOrders_whenDateIsProvidedButNotOrderFound_thenReturnLNotContent() throws Exception {

        //Given
        final var searchDate = LocalDate.now();
        given(orderService.findOrder(any(LocalDateTime.class))).willReturn(EMPTY_LIST);

        //When
        this.mockMvc.perform(get(ORDER_PATH).param("date", searchDate.toString()))
                .andExpect(status().isNoContent());

        //Then
        then(orderService).should(times(1)).findOrder(any(LocalDateTime.class));
    }
}
