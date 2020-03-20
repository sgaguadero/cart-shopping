package com.njc.cartshopping.converter.entity;

import com.njc.cartshopping.dto.ProductDto;
import com.njc.cartshopping.model.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductDtoToEntity implements Converter<ProductDto, Product> {

    @Override
    public Product convert(final ProductDto productDto) {

        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .createDate(LocalDateTime.now())
                .build();
    }
}
