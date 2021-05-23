package com.dvimer.buildmarket;

import com.github.javafaker.Faker;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Утилитный класс для создания файлов для тестов, чтобы руками рандомные номера не писать
 */
public class CreateTestFileUtil {
    private static final String NEW_FILE_NAME = "goods.sql";
    public static final String BASE_FILE_PATH = "src/test/resources/";
    private static final Random RANDOM = new Random();
    private static Faker faker = new Faker();

    public static void main(String[] args) {
        try (PrintWriter file = new PrintWriter(BASE_FILE_PATH + NEW_FILE_NAME)) {

            for (int i = 1; i <= 20; i++) {
                for (int j = 0; j < 20; j++) {
                    String type = faker.pokemon().name();
                    for (int k = 0; k < 20; k++) {
                        String category = faker.cat().name();
                        for (int l = 0; l < 20; l++) {
                            String size = faker.color().name();
                            for (int m = 0; m < 5000; m++) {
                                file.println(generateSql(i, type, category, size));
                            }
                        }
                    }

                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //INSERT INTO goods(store_id, path, purchase_price, delivery_price, installation_price) VALUES (1, 'Двери.Пожарные.500x600.Дверь_пожарная', 10,11,12);
    private static String generateSql(Integer storeId, String type, String category, String size) {
        return new StringBuilder()
                .append("INSERT INTO goods(store_id, path, purchase_price, delivery_price, installation_price) VALUES ")
                .append("('").append(storeId)
                .append("', '")
                .append(generatePath(type, category, size))
                .append("','")
                .append(RANDOM.nextInt(99))
                .append("','")
                .append(RANDOM.nextInt(99))
                .append("','")
                .append(RANDOM.nextInt(99))
                .append("')").append(";").toString();
    }

    private static String generatePath(String type, String category, String size) {
        String prePath = type + "." + category + "." + size + "." + faker.food().ingredient();
        return prePath.replaceAll(" ", "_").replaceAll("'", "");
    }
}