package com.sellout.productservice.service;

import com.sellout.productservice.dto.ProductRequest;
import com.sellout.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
}
