package com.dvimer.buildmarket.service;

import com.dvimer.buildmarket.BadRequestException;
import com.dvimer.buildmarket.controller.model.ItemsModel;
import com.dvimer.buildmarket.dao.entity.PathAmount;
import com.dvimer.buildmarket.dao.entity.StoreEntity;
import com.dvimer.buildmarket.dao.entity.StoreThreeAmount;
import com.dvimer.buildmarket.dao.repository.GoodsRepository;
import com.dvimer.buildmarket.dao.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final GoodsRepository goodsRepository;

    public List<ItemsModel> findGoodsThree(Long id, String property) {
        StoreEntity store = storeRepository.findById(id).orElseThrow(() -> new BadRequestException("Store is not found"));

        List<PathAmount> allByType = goodsRepository.findAllByType(store.getId());
        List<PathAmount> allByCategory = goodsRepository.findAllByCategory(store.getId());
        List<PathAmount> allBySize = goodsRepository.findAllBySize(store.getId());
        List<PathAmount> allByName = goodsRepository.findAllByName(store.getId());

        List<ItemsModel> names = allByName.stream()
                .map(p -> ItemsModel.builder()
                        .name(p.getSubpath())
                        .path(subName(p))
                        .value(p.getSum())
                        .prop(property)
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

    private List<ItemsModel> getItemsByType(List<ItemsModel> categories, PathAmount type) {
        return categories.stream()
                .filter(cat -> cat.getPath().contains(type.getSubpath()))
                .collect(Collectors.toList());
    }

    //todo вторая версия, еще не готово
    public List<StoreThreeAmount> findAllStoreItem(Long id, String property) {
        StoreEntity store = storeRepository.findById(id).orElseThrow(() -> new BadRequestException("Store is not found"));

        List<StoreThreeAmount> allStoreItem = goodsRepository.findAllStoreItem(store.getId());

        return allStoreItem;
    }

}
