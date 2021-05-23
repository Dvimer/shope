package com.dvimer.buildmarket.service;

import com.dvimer.buildmarket.controller.model.ItemsModel;
import com.dvimer.buildmarket.dao.entity.StoreEntity;
import com.dvimer.buildmarket.dao.helpers.PathAmount;
import com.dvimer.buildmarket.dao.repository.GoodsRepository;
import com.dvimer.buildmarket.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.Collectors;

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
        StoreEntity store = storeService.findById(id);

        List<PathAmount> allByType;
        List<PathAmount> allByCategory;
        List<PathAmount> allBySize;
        List<PathAmount> allByName;

        if (Strings.isEmpty(property))
            throw new BadRequestException("Обязательно указывать название паремтра стоимости");
//todo перенести в спецификации
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
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

        stopWatch.stop();
        log.info("Данные из базы получены за ={}", stopWatch.getTotalTimeSeconds());

        StopWatch stopWatchName = new StopWatch();
        stopWatchName.start();
        List<ItemsModel> names = allByName.stream()
                .map(p -> getItem(property, p.getSum(), subName(p), p.getSubpath(), null))
                .collect(Collectors.toList());
        stopWatchName.stop();
        log.info("Данные имен смаплены за ={},количество={}", stopWatchName.getTotalTimeSeconds(), names.size());

        StopWatch stopWatchSize = new StopWatch();
        stopWatchSize.start();
        List<ItemsModel> sizes = allBySize.stream()
                .map(size -> getItem(property, size.getSum(), subName(size), size.getSubpath(), getItemsByType(names, size)))
                .collect(Collectors.toList());
        stopWatchSize.stop();
        log.info("Данные размеров смаплены за ={},количество={}", stopWatchSize.getTotalTimeSeconds(), sizes.size());

        StopWatch stopWatchCat = new StopWatch();
        stopWatchCat.start();
        List<ItemsModel> categories = allByCategory
                .stream()
                .map(cat -> getItem(property, cat.getSum(), subName(cat), cat.getSubpath(), getItemsByType(sizes, cat)))
                .collect(Collectors.toList());
        stopWatchCat.stop();
        log.info("Данные категорий смаплены за ={},количество={}", stopWatchCat.getTotalTimeSeconds(), categories.size());

        StopWatch stopWatchType = new StopWatch();
        stopWatchType.start();
        List<ItemsModel> types = allByType.stream()
                .map(type -> getItem(property, type.getSum(), type.getSubpath(), type.getSubpath(), getItemsByType(categories, type)))
                .collect(Collectors.toList());
        stopWatchType.stop();
        log.info("Данные типов смаплены за ={},количество={}", stopWatchType.getTotalTimeSeconds(), types.size());


        Long totalSum = allByType.stream().map(PathAmount::getSum).reduce(Long::sum).get();
        return getItem(property, totalSum, store.getName(), null, types);
    }

    private ItemsModel getItem(String property, Long sum, String s, String subpath, List<ItemsModel> itemsByType) {
        return ItemsModel.builder()
                .name(s)
                .path(subpath)
                .value(sum)
                .prop(property)
                .items(itemsByType)
                .build();
    }

    private String subName(PathAmount p) {
        return p.getSubpath().substring(p.getSubpath().lastIndexOf(".") + 1);
    }
//// TODO: 23.05.2021 самое узкое место данный мапинг на 8000 имен и 3000 категории работает 40 секунд 
    private List<ItemsModel> getItemsByType(List<ItemsModel> itemsModels, PathAmount type) {
        return itemsModels.stream()
                .filter(item -> item.getPath().contains(type.getSubpath()))
                .collect(Collectors.toList());
    }
}
