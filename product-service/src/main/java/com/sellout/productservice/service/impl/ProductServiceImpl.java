package com.sellout.productservice.service.impl;

import com.sellout.productservice.dto.ProductRequest;
import com.sellout.productservice.dto.ProductResponse;
import com.sellout.productservice.entity.Product;
import com.sellout.productservice.repository.ProductRepository;
import com.sellout.productservice.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setStartingPrice(request.getStartingPrice());
        product.setCurrentPrice(request.getStartingPrice());
        product.setStartTime(request.getStartTime());
        product.setEndTime(request.getEndTime());
        product.setActive(true);
        product.setSellerId(request.getSellerId());

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCurrentPrice(product.getCurrentPrice());
        response.setStartTime(product.getStartTime());
        response.setEndTime(product.getEndTime());
        response.setActive(product.isActive());
        response.setSellerId(product.getSellerId());
        return response;
    }
}
