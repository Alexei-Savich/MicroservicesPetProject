package com.example.inventory.configurations;

import com.example.inventory.entities.InventoryItemData;
import com.example.inventory.repositories.InventoryRepository;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Configuration
public class InventoryConfig {

    @Bean
    CommandLineRunner initInventoryData(InventoryRepository inventoryRepository) {
        return args -> {
            inventoryRepository.saveAll(getInventoryDataFromCsv());
        };
    }

    private List<InventoryItemData> getInventoryDataFromCsv() throws Exception {
        return getDataFormCsv("static/jcpenney_com-ecommerce_sample.csv");
    }

    private List<InventoryItemData> getDataFormCsv(String path) throws IOException {
        Random random = new Random();
        List<InventoryItemData> res = new ArrayList<>();
        RFC4180ParserBuilder builder = new RFC4180ParserBuilder();
        RFC4180Parser rfc4180Parser = builder.withSeparator(',').build();
        List<String> data = readStringFromFile(path);
        for (String line : data) {
            String[] parsed = rfc4180Parser.parseLine(line);
            if (parsed.length >= 4) {
                if (parsed[0].matches("-?[0-9a-fA-F]+")) {
                    int amount = 0;
                    if(random.nextInt(100) > 20){
                        amount = random.nextInt(100);
                    }
                    res.add(new InventoryItemData(parsed[0], amount));
                }
            }
        }
        return res;
    }

    private List<String> readStringFromFile(String path) throws IOException {
        try (InputStream stream = InventoryConfig.class.getClassLoader().getResourceAsStream(path);
             Scanner scanner = new Scanner(stream)) {
            List<String> stringList = new ArrayList<>();
            while (scanner.hasNextLine()) {
                stringList.add(scanner.nextLine());
            }
            return stringList;
        }
    }


}
