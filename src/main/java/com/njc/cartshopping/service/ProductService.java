package com.njc.cartshopping.service;

import com.njc.cartshopping.dto.ProductDto;
import com.njc.cartshopping.model.Product;
import com.njc.cartshopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ConversionService conversionService;

    @Autowired
    private ProductService(final ProductRepository productRepository, final ConversionService conversionService) {
        this.productRepository = productRepository;
        this.conversionService = conversionService;
    }

    public ProductDto saveProduct(final ProductDto productDto) {

        final Product product = conversionService.convert(productDto, Product.class);
        return conversionService.convert(productRepository.save(product), ProductDto.class);
    }

    public ProductDto find(final String name) {

        return this.productRepository.findByName(name)
                .map(product ->  conversionService.convert(product, ProductDto.class))
                .orElse(null);
    }

    public List<ProductDto> findAll() {

        final List<Product> productList = productRepository.findAll();

        return productList.stream()
                .map(product ->  conversionService.convert(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    public void updateProduct(final ProductDto productDto) {

        productRepository.findByName(productDto.getName()).ifPresentOrElse(foundProduct -> {
            Product product = conversionService.convert(productDto, Product.class);
            productRepository.save(product);
            }, NoSuchElementException::new);
    }
}
