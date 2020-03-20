package com.njc.cartshopping.controller;

import com.njc.cartshopping.dto.ProductDto;
import com.njc.cartshopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ProductController.PRODUCT_PATH)
public class ProductController {

    static final String PRODUCT_PATH = "/api/product";
    private static final String PRODUCT_NAME_PATH = "/{name}";

    private final ProductService productService;

    @Autowired
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(PRODUCT_NAME_PATH)
    public ResponseEntity<ProductDto> find(@PathVariable @NotNull final String name) {

        final ProductDto productDto = this.productService.find(name);

        return Optional.ofNullable(productDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {

        final List<ProductDto> productList = this.productService.findAll();

        if (CollectionUtils.isEmpty(productList)) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(productList, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody final ProductDto productDto) {
        final ProductDto productStored = productService.saveProduct(productDto);
        return new ResponseEntity<>(productStored, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateProduct(@Valid @RequestBody final ProductDto productDto) {

        productService.updateProduct(productDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
