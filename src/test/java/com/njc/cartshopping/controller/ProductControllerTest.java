package com.njc.cartshopping.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.njc.cartshopping.converter.entity.OrderDtoToEntity;
import com.njc.cartshopping.dto.ProductDto;
import com.njc.cartshopping.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.njc.cartshopping.controller.ProductController.PRODUCT_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@RunWith(SpringRunner.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderDtoToEntity orderDtoToEntity;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void findProduct_whenProductNameIsProvided_ThenReturnAValidProductDto() throws Exception {

        //Given
        final var productId = 1L;
        final var productName = "anyProduct";
        final var price = 10;
        final var expectedProduct = ProductDto.builder().id(productId).name(productName).price(price).build();

        given(productService.find(productName)).willReturn(expectedProduct);

        //When
        final var response = this.mockMvc
                .perform(get(PRODUCT_PATH+"/"+productName))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Then
        final var jsonResponse = this.objectMapper.readValue(response, ProductDto.class);
        assertThat(jsonResponse.getId()).isEqualTo(expectedProduct.getId());
    }

    @Test
    public void findProduct_whenProductNameIsProvidedButNotFOund_ThenReturnNotContent() throws Exception {

        //Given
        final var productName = "unknownProductName";

        given(productService.find(productName)).willReturn(null);

        //When Then
        this.mockMvc.perform(get(PRODUCT_PATH+"/"+productName))
                .andExpect(status().isNoContent());
    }

    @Test
    public void findAll_whenThereAreNotProducts_ThenReturnNotContent() throws Exception {

        //Given
        given(productService.findAll()).willReturn(Collections.emptyList());

        //When Then
        this.mockMvc.perform(get(PRODUCT_PATH))
                .andExpect(status().isNoContent());
    }

    @Test
    public void findAll_ThenReturnTwoProducts() throws Exception {

        //Given
        final var productIdOne = 1L;
        final var productIdTwo = 2L;
        final var productNameOne = "productNameOne";
        final var productNameTwo = "productNameTwo";
        final var priceTen = 10;
        final var priceTwenty = 20;
        final var expectedProductOne = ProductDto.builder()
                .id(productIdOne)
                .name(productNameOne)
                .price(priceTen)
                .build();
        final var expectedProductTwo = ProductDto.builder()
                .id(productIdTwo)
                .name(productNameTwo)
                .price(priceTwenty)
                .build();

        given(productService.findAll()).willReturn(List.of(expectedProductOne,expectedProductTwo));

        //When
        final var response = this.mockMvc
                .perform(get(PRODUCT_PATH))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        final var actual = objectMapper.readValue(response.getResponse().getContentAsString(),
                new TypeReference<List<ProductDto>>() {});

        assertThat(actual).containsExactly(expectedProductOne,expectedProductTwo);
    }

    @Test
    public void createNewProduct_whenProductDtoIsPassedOk_ThenReturnHttp201() throws Exception {

        //Given
        final var productId = 1L;
        final var productName = "anyProduct";
        final var price = 10;
        final var product = ProductDto.builder().name(productName).price(price).build();
        final var expectedProduct = ProductDto.builder().id(productId).name(productName).price(price).build();

        given(productService.saveProduct(product)).willReturn(expectedProduct);

        //When
        final var response = this.mockMvc
                .perform(post(PRODUCT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        //Then
        final var jsonResponse = this.objectMapper.readValue(response, ProductDto.class);
        assertThat(jsonResponse.getId()).isEqualTo(expectedProduct.getId());
    }

    @Test
    public void updateProduct_whenProductDtoIsPassedOk_ThenReturnHttp201() throws Exception {

        //Given
        final var productId = 1L;
        final var productName = "anyProduct";
        final var price = 10;
        final var productDto = ProductDto.builder().id(productId).name(productName).price(price).build();

        //When
        this.mockMvc
                .perform(put(PRODUCT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk());

        //Then
        then(productService).should(times(1)).updateProduct(productDto);
    }
}
