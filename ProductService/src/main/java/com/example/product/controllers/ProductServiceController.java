package com.example.product.controllers;

import com.example.product.entities.EndProductResponse;
import com.example.product.services.ProductServiceService;
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
public class ProductServiceController {

    private final ProductServiceService service;

    @Autowired
    public ProductServiceController(ProductServiceService service) {
        this.service = service;
    }

    @GetMapping(path = "/catalog/{id}")
    public ResponseEntity<EndProductResponse> getDataById(@PathVariable(name = "id") String id){
        log.info("Request to get item with id: {}", id);
        Optional<EndProductResponse> optional = service.getDataById(id);
        if(optional.isEmpty()){
            log.info("Request to get item with id: {} found nothing!!", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(optional.get().getNumOfItems() == -1){
            log.info("One of the services unavailable!");
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
        log.info("Request success to get item with id: {}!", id);
        return new ResponseEntity<>(optional.get(), HttpStatus.OK);
    }

    @GetMapping(path = "/catalog/sku/{sku}")
    public ResponseEntity<List<EndProductResponse>> getDataBySku(@PathVariable(name = "sku") String sku){
        log.info("Request to get item by sku: {}", sku);
        List<EndProductResponse> dataBySku = service.getDataBySku(sku);
        if(dataBySku.isEmpty()){
            log.info("Request to get item by sku: {} found nothing!", sku);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(dataBySku.get(0).getNumOfItems() == -1){
            log.info("One of the services unavailable!");
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
        log.info("Request success to get item by sku: {}!", sku);
        return new ResponseEntity<>(dataBySku, HttpStatus.OK);
    }

}
