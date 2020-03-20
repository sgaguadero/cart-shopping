package com.njc.cartshopping.converter.dto;

import com.njc.cartshopping.dto.ProductDto;
import com.njc.cartshopping.model.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductToDtoConverter implements Converter<Product, ProductDto> {

    @Override
    public ProductDto convert(final Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
