package com.njc.cartshopping.controller;


import com.njc.cartshopping.dto.OrderDto;
import com.njc.cartshopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(OrderController.ORDER_PATH)
public class OrderController {

    static final String ORDER_PATH = "/api/order";

    private final OrderService orderService;

    @Autowired
    private OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> makeOrder(@Valid @RequestBody final OrderDto orderDto) {

        final OrderDto orderPlaced = orderService.makeOrder(orderDto);
        return new ResponseEntity<>(orderPlaced, HttpStatus.CREATED);
    }

    //Retrieve all orders from date provided to currentDate
    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrdersByDate (@RequestParam(name = "date")
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        final LocalTime time = LocalTime.now();
        //Place the order with the current time
        final LocalDateTime orderDateTime = LocalDateTime.of(date, time);
        final List<OrderDto> orderDtoList = this.orderService.findOrder(orderDateTime);

        if (CollectionUtils.isEmpty(orderDtoList)) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        }
    }
}
