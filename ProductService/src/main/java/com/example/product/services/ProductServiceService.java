package com.example.product.services;

import com.example.product.entities.EndProductResponse;
import com.example.product.entities.InventoryItemData;
import com.example.product.entities.Product;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceService {

    private final RestTemplate restTemplate;

    @Autowired
    public ProductServiceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "returnErrorData",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "4000")
            }
    )
    public Optional<EndProductResponse> getDataById(String id) {
        log.info("Sending request to CatalogApplication");
        ResponseEntity<Product> productResponseEntity = restTemplate.getForEntity("http://catalog-server/products/{id}", Product.class, id);
        log.info("Got response code {} from http://catalog-server/products/{}", productResponseEntity.getStatusCode(), id);
        log.info("Sending request to InventoryService");
        ResponseEntity<InventoryItemData> inventoryItemDataResponseEntity = restTemplate.getForEntity("http://inventory-data-service/inventory/{id}", InventoryItemData.class, id);
        log.info("Got response code {} from http://inventory-data-service/inventory/{}", inventoryItemDataResponseEntity.getStatusCode(), id);
        if (productResponseEntity.getStatusCode() == HttpStatus.OK && inventoryItemDataResponseEntity.getStatusCode() == HttpStatus.OK) {
            if (inventoryItemDataResponseEntity.getBody().getNumOfItems() > 0) {
                return Optional.of(new EndProductResponse
                        (
                                productResponseEntity.getBody().getUniqueId(),
                                productResponseEntity.getBody().getSku(),
                                inventoryItemDataResponseEntity.getBody().getNumOfItems()
                        )
                );
            }
        }
        return Optional.empty();
    }

    public Optional<EndProductResponse> returnErrorData(String id) {
        log.info("InventoryService is unavailable!");
        return Optional.of(new EndProductResponse("-1", "-1", -1));
    }

    @HystrixCommand(fallbackMethod = "returnErrorDataBySku",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "4000")
            }
    )
    public List<EndProductResponse> getDataBySku(String sku) {
        log.info("Sending request to CatalogApplication");
        ResponseEntity<List> productResponseEntity = restTemplate.getForEntity("http://catalog-server/products/sku/{sku}", List.class, sku);
        log.info("Got response code {} from http://catalog-server/products/sku/{}", productResponseEntity.getStatusCode(), sku);
        List<EndProductResponse> res = new ArrayList<>();
        if (productResponseEntity.getStatusCode() == HttpStatus.OK) {
            for (Object o : productResponseEntity.getBody()) {
                LinkedHashMap<String, String> hashMap = (LinkedHashMap<String, String>) o;
                Product p = new Product(hashMap.get("uniqueId"), hashMap.get("sku"));
                log.info("Sending request to InventoryService");
                ResponseEntity<InventoryItemData> responseEntity = restTemplate.getForEntity("http://inventory-data-service/inventory/{id}", InventoryItemData.class, p.getUniqueId());
                log.info("Got response code {} from http://inventory-data-service/inventory/{}", responseEntity.getStatusCode(), p.getUniqueId());
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    if (responseEntity.getBody().getNumOfItems() > 0) {
                        res.add(new EndProductResponse
                                (
                                        p.getUniqueId(),
                                        p.getSku(),
                                        responseEntity.getBody().getNumOfItems()
                                )
                        );
                    }
                }
            }
        }
        return res;
    }

    public List<EndProductResponse> returnErrorDataBySku(String sku) {
        log.info("InventoryService is unavailable!");
        List<EndProductResponse> res = new ArrayList<>();
        res.add(new EndProductResponse("-1", "-1", -1));
        return res;
    }

}
