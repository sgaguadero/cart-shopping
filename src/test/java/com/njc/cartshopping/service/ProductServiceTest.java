package com.njc.cartshopping.service;

import com.njc.cartshopping.dto.ProductDto;
import com.njc.cartshopping.model.Product;
import com.njc.cartshopping.repository.ProductRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @SpyBean
    private ConversionService conversionService;

    @Autowired
    private ProductService productService;

    @Test
    public void find_whenNameIsProvided_theReturnValidDto() {

        //Given
        final var name = "anyNameOfProduct";
        final var product = Product.builder()
                .price(10)
                .name(name)
                .id(1L)
                .createDate(LocalDateTime.now())
                .build();
        given(productRepository.findByName(name)).willReturn(Optional.of(product));

        //When
        final var productDto = productService.find(name);

        //Then
        assertThat(productDto).hasFieldOrPropertyWithValue("name",product.getName());
        assertThat(productDto).hasFieldOrPropertyWithValue("price",product.getPrice());
    }

    @Test
    public void findName_whenNameIsNotFound_then() {

        //Given
        final var name = "anyNameOfProduct";
        given(productRepository.findByName(name)).willReturn(Optional.empty());

        //When
        final var productDto = productService.find(name);

        //Then
        assertThat(productDto).isNull();
    }

    @Test
    public void findAll_whenThereAreTwoProducts_thenReturnTwoProductsDto() {

        //Given
        final var productDtoOne = Product.builder()
                .id(1L)
                .name("anyName")
                .price(10)
                .build();

        final var productDtoTwo = Product.builder()
                .id(2L)
                .name("anyNameTwo")
                .price(20)
                .build();
        given(productRepository.findAll()).willReturn(List.of(productDtoOne, productDtoTwo));

        //When
        final var productList = productService.findAll();

        //Then
        assertThat(productList.size()).isEqualTo(2);
    }

    // FIXME: 19/03/2020
    @Ignore
    @Test(expected = NoSuchElementException.class)
    public void updateProduct_whenProductNotFound_thenReturnNotSuchElementException() {

        //Given
        final var name = "anyName";
        final var productDto = ProductDto.builder()
                .name(name)
                .build();
        given(productRepository.findByName(name)).willReturn(Optional.empty());


        //When Then
        productService.updateProduct(productDto);
    }

    @Test
    public void updateProduct_whenProductIsFound_thenCallToConverter() {

        //Given
        final var name = "anyName";
        final var productDto = ProductDto.builder()
                .name(name)
                .price(10)
                .id(1L)
                .build();

        final var product = Product.builder()
                .name(name)
                .price(10)
                .id(1L)
                .build();
        given(productRepository.findByName(name)).willReturn(Optional.of(product));

        //When
        productService.updateProduct(productDto);

        //Then
        then(productRepository).should(times(1)).save(any(Product.class));
    }

    @Test
    public void saveProduct_whenProcessIsCorrect_thenReturnProductDto() {

        //Given
        final var name = "anyname";
        final var price = 10;
        final var id = 1L;
        final var product = Product.builder()
                .price(price)
                .name(name)
                .id(id)
                .createDate(LocalDateTime.now())
                .build();

        final var productDto = ProductDto.builder()
                .name(name)
                .price(price)
                .build();

        given(productRepository.save(any(Product.class))).willReturn(product);

        //When
        final var responseDto = productService.saveProduct(productDto);

        //Then
        assertThat(responseDto).hasFieldOrPropertyWithValue("name",product.getName());
        assertThat(responseDto).hasFieldOrPropertyWithValue("price",product.getPrice());
    }
}
