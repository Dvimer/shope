package com.dvimer.buildmarket.service;

import com.dvimer.buildmarket.controller.model.ItemsModel;
import com.dvimer.buildmarket.dao.entity.StoreEntity;
import com.dvimer.buildmarket.dao.helpers.StoreThreeAmount;
import com.dvimer.buildmarket.dao.repository.GoodsRepository;
import com.dvimer.buildmarket.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsService {
    private final StoreService storeService;
    private final GoodsRepository goodsRepository;

    private final String PURCHASE_PRICE = "цена закупки";
    private final String DELIVERY_PRICE = "стоимость поставки";
    private final String INSTALLATION_PRICE = "стоимость монтажа";

    public ItemsModel findGoodsThree(Long id, String property) {
        log.info("Начинаем получение данных");
        StoreEntity store = storeService.findById(id);

        Set<StoreThreeAmount> goods = getStoreThreeAmounts(id, property);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ItemsModel itemsModel = ItemsModel.builder().name(store.getName()).prop(property).build();
        Map<String, ItemsModel> types = new HashMap<>();
        itemsModel.setItems(types);
        Long totalAmount = 0L;
        for (StoreThreeAmount good : goods) {
            ItemsModel type = types.get(good.getType());
            if (type == null) {
                type = ItemsModel.builder().value(0L).prop(property).name(good.getType()).build();
                types.put(good.getType(), type);
            }
            Map<String, ItemsModel> categories = type.getItems();
            if (categories == null) {
                categories = new HashMap<>();
                type.setItems(categories);
            }
            ItemsModel category = categories.get(good.getType());
            if (category == null) {
                category = ItemsModel.builder().value(0L).prop(property).name(good.getCategory()).build();
                categories.put(good.getCategory(), category);
            }
            Map<String, ItemsModel> sizes = category.getItems();
            if (sizes == null) {
                sizes = new HashMap<>();
                category.setItems(sizes);
            }
            ItemsModel size = sizes.get(good.getType());
            if (size == null) {
                size = ItemsModel.builder().value(0L).prop(property).name(good.getSize()).build();
                sizes.put(good.getCategory(), size);
            }
            Map<String, ItemsModel> names = size.getItems();
            if (names == null) {
                names = new HashMap<>();
                size.setItems(names);
            }
            ItemsModel name = names.get(good.getType());
            if (name == null) {
                name = ItemsModel.builder().prop(property).value(good.getSum()).name(good.getName()).build();
                names.put(good.getCategory(), name);
                type.setValue(type.getValue() + name.getValue());
                category.setValue(category.getValue() + name.getValue());
                size.setValue(size.getValue() + name.getValue());
                totalAmount += name.getValue();
            }

        }
        itemsModel.setValue(totalAmount);
        stopWatch.stop();
        log.info("Данные смапленны и поссчита стоимость за={}", stopWatch.getTotalTimeSeconds());
        return itemsModel;
    }

    private Set<StoreThreeAmount> getStoreThreeAmounts(Long id, String property) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            if (DELIVERY_PRICE.equalsIgnoreCase(property)) {
                return goodsRepository.findAllStoreItemDelivery(id);

            } else if (PURCHASE_PRICE.equalsIgnoreCase(property)) {
                return goodsRepository.findAllStoreItemPurchase(id);

            } else if (INSTALLATION_PRICE.equalsIgnoreCase(property)) {
                return goodsRepository.findAllStoreItemInstallation(id);
            } else {
                throw new BadRequestException("Мы не можем искать по текущему параметру={}", property);
            }
        } finally {
            stopWatch.stop();
            log.info("Данные из базы получены за={}", stopWatch.getTotalTimeSeconds());
        }
    }
}
