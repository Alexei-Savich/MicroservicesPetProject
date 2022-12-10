package com.example.catalog.configurations;

import com.example.catalog.entities.Product;
import com.example.catalog.repositories.ProductRepository;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Configuration
public class ProductConfig {

//    @Bean
    CommandLineRunner initProducts(ProductRepository productRepository) {
        return args -> {
            productRepository.saveAll(getProductsFromCsv());
        };
    }

    private List<Product> getProductsFromCsv() throws Exception {
        return getProductsFormCsv("static/jcpenney_com-ecommerce_sample.csv");
    }

    private List<Product> getProductsFormCsv(String path) throws IOException {
        List<Product> res = new ArrayList<>();
        RFC4180ParserBuilder builder = new RFC4180ParserBuilder();
        RFC4180Parser rfc4180Parser = builder.withSeparator(',').build();
        List<String> data = readStringFromFile(path);
        for (String line : data) {
            String[] parsed = rfc4180Parser.parseLine(line);
            if (parsed.length >= 4) {
                if (parsed[0].matches("-?[0-9a-fA-F]+")) {
                    res.add(new Product(parsed[0], parsed[1]));
                }
            }
        }
        return res;
    }

    private List<String> readStringFromFile(String path) throws IOException {
        try (InputStream stream = ProductConfig.class.getClassLoader().getResourceAsStream(path);
             Scanner scanner = new Scanner(stream)) {
            List<String> stringList = new ArrayList<>();
            while (scanner.hasNextLine()) {
                stringList.add(scanner.nextLine());
            }
            return stringList;
        }
    }


}
