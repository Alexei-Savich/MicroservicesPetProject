package com.example.catalog.services;

import com.example.catalog.entities.Product;
import com.example.catalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> getById(String id){
        return productRepository.findById(id);
    }

    public List<Product> getBySku(String sku){
        return productRepository.findBySku(sku);
    }

}
