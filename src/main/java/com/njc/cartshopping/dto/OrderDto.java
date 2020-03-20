package com.njc.cartshopping.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@JsonDeserialize(builder = OrderDto.OrderDtoBuilder.class)
@ApiModel(description="All details about Orders ")
public class OrderDto {

    @ApiModelProperty(notes = "Order Id created by the system")
    private final long orderId;

    @ApiModelProperty(notes = "Total order Price")
    private final double totalPrice;

    @ApiModelProperty(notes = "Order date, created when order is placed")
    private final LocalDateTime orderDate;

    @Email
    @NotNull
    private String email;

    @ApiModelProperty(notes = "List of products included on the order")
    @Builder.Default
    private List<Long> products;

    @JsonPOJOBuilder(withPrefix = "")
    public static class OrderDtoBuilder {
    }
}
