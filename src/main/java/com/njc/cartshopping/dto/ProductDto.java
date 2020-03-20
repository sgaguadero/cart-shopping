package com.njc.cartshopping.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = ProductDto.ProductDtoBuilder.class)
@ApiModel(description="All details about Products")
public class ProductDto {


    @ApiModelProperty(notes = "Product Id created by the system")
    private final long id;

    @ApiModelProperty(notes = "Name of product")
    @NotNull
    private final String name;

    @ApiModelProperty(notes = "Total product Price")
    @NotNull
    private final double price;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ProductDtoBuilder {
    }
}
