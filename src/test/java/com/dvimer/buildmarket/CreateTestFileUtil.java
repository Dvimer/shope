package com.dvimer.buildmarket;

import com.dvimer.buildmarket.utils.HelpData;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Утилитный класс для создания файлов для тестов, чтобы руками рандомные номера не писать
 */
public class CreateTestFileUtil implements HelpData {
    private static final Integer COUNT_GOODS_IN_EVERY_SIZE = 10;

    public static void main(String[] args) {
        try (PrintWriter file = new PrintWriter(BASE_FILE_PATH + NEW_FILE_NAME)) {
            for (int i = 1; i <= 20; i++) {
                for (int j = 0; j < 20; j++) {
                    String type = types.get(RANDOM.nextInt(types.size()));
                    for (int k = 0; k < 20; k++) {
                        String category = categories.get(RANDOM.nextInt(categories.size()));
                        for (int l = 0; l < 20; l++) {
                            String size = sizes.get(RANDOM.nextInt(sizes.size()));
                            for (int m = 0; m < COUNT_GOODS_IN_EVERY_SIZE; m++) {
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
        String prePath = type + "." + category + "." + size + "." + names.get(RANDOM.nextInt(names.size()));
        return prePath.replaceAll(" ", "_").replaceAll("'", "");
    }
}