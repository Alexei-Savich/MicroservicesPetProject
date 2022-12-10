package com.example.catalog.controllers;

import com.example.catalog.entities.Product;
import com.example.catalog.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "id") String id){
        log.info("Request to get item by id: {}", id);
        Optional<Product> productOptional =  productService.getById(id);
        if(productOptional.isEmpty()){
            log.info("Request to get item by id: {} found nothing!", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Request success to get item by id: {}!", id);
        return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
    }

    @GetMapping(path = "products/sku/{sku}")
    public ResponseEntity<List<Product>> getProductBySku(@PathVariable(name = "sku") String sku){
        log.info("Request to get item by sku: {}", sku);
        List<Product> products =  productService.getBySku(sku);
        if(products.isEmpty()){
            log.info("Request to get item by sku: {} found nothing!", sku);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Request success to get item by sku: {}!", sku);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
