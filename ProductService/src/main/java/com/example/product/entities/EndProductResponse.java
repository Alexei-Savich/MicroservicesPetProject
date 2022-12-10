package com.example.product.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EndProductResponse {

    private String uniqueId;
    private String sku;
    private int numOfItems;

}
