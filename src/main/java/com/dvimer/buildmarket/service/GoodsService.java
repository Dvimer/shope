package com.dvimer.buildmarket.service;

import com.dvimer.buildmarket.controller.model.ItemsModel;
import com.dvimer.buildmarket.dao.helpers.PathAmount;
import com.dvimer.buildmarket.dao.entity.StoreEntity;
import com.dvimer.buildmarket.dao.repository.GoodsRepository;
import com.dvimer.buildmarket.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final StoreService storeService;
    private final GoodsRepository goodsRepository;

    private final String PURCHASE_PRICE = "цена закупки";
    private final String DELIVERY_PRICE = "стоимость поставки";
    private final String INSTALLATION_PRICE = "стоимость монтажа";

    public List<ItemsModel> findGoodsThree(Long id, String property) {
        StoreEntity store = storeService.findById(id);

        List<PathAmount> allByType;
        List<PathAmount> allByCategory;
        List<PathAmount> allBySize;
        List<PathAmount> allByName;

        if (Strings.isEmpty(property))
            throw new BadRequestException("Обязательно указывать название паремтра стоимости");
//todo перенести в спецификации
        if (DELIVERY_PRICE.equalsIgnoreCase(property)) {
            allByType = goodsRepository.findAllByTypeAndDeliveryPrice(store.getId());
            allByCategory = goodsRepository.findAllByCategoryAndDeliveryPrice(store.getId());
            allBySize = goodsRepository.findAllBySizeAndDeliveryPrice(store.getId());
            allByName = goodsRepository.findAllByNameAndDeliveryPrice(store.getId());
        } else if (PURCHASE_PRICE.equalsIgnoreCase(property)) {
            allByType = goodsRepository.findAllByTypeAndPurchasePrice(store.getId());
            allByCategory = goodsRepository.findAllByCategoryAndPurchasePrice(store.getId());
            allBySize = goodsRepository.findAllBySizeAndPurchasePrice(store.getId());
            allByName = goodsRepository.findAllByNameAndPurchasePrice(store.getId());
        } else if (INSTALLATION_PRICE.equalsIgnoreCase(property)) {
            allByType = goodsRepository.findAllByTypeAndInstallationPrice(store.getId());
            allByCategory = goodsRepository.findAllByCategoryAndInstallationPrice(store.getId());
            allBySize = goodsRepository.findAllBySizeAndInstallationPrice(store.getId());
            allByName = goodsRepository.findAllByNameAndInstallationPrice(store.getId());
        } else {
            throw new BadRequestException("Мы не можем искать по текущему параметру={}", property);
        }

        List<ItemsModel> names = allByName.stream()
                .map(p -> ItemsModel.builder()
                        .name(p.getSubpath())
                        .path(subName(p))
                        .value(p.getSum())
                        .prop(property)
                        .items(null)
                        .build())
                .collect(Collectors.toList());


        List<ItemsModel> sizes = allBySize.stream()
                .map(size -> ItemsModel.builder()
                        .name(subName(size))
                        .path(size.getSubpath())
                        .value(size.getSum())
                        .prop(property)
                        .items(getItemsByType(names, size))
                        .build())
                .collect(Collectors.toList());

        List<ItemsModel> categories = allByCategory
                .stream()
                .map(cat -> ItemsModel.builder()
                        .name(subName(cat))
                        .path(cat.getSubpath())
                        .value(cat.getSum())
                        .prop(property)
                        .items(getItemsByType(sizes, cat))
                        .build())
                .collect(Collectors.toList());


        return allByType.stream()
                .map(type -> ItemsModel.builder()
                        .name(type.getSubpath())
                        .path(type.getSubpath())
                        .value(type.getSum())
                        .prop(property)
                        .items(getItemsByType(categories, type))
                        .build())
                .collect(Collectors.toList());
    }

    private String subName(PathAmount p) {
        return p.getSubpath().substring(p.getSubpath().lastIndexOf(".") + 1);
    }

    private List<ItemsModel> getItemsByType(List<ItemsModel> itemsModels, PathAmount type) {
        return itemsModels.stream()
                .filter(item -> item.getPath().contains(type.getSubpath()))
                .collect(Collectors.toList());
    }

}
