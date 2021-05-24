package com.dvimer.buildmarket.dao.repository;

import com.dvimer.buildmarket.dao.entity.GoodsEntity;
import com.dvimer.buildmarket.dao.helpers.StoreThreeAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface GoodsRepository extends JpaRepository<GoodsEntity, Long> {
    //todo тут лучше сделать через Specification, но для скорости копипастнул
    @Query(value = "SELECT ltree2text(subpath(path, 0, 1)) AS type, " +
            "ltree2text(subpath(path, 1, 1)) AS category, " +
            "ltree2text(subpath(path, 2, 1)) AS size, " +
            "ltree2text(subpath(path, 3, 1)) AS name, " +
            "SUM(installation_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY type, category, size, name; ", nativeQuery = true)
    Set<StoreThreeAmount> findAllStoreItemInstallation(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 1)) AS type, " +
            "ltree2text(subpath(path, 1, 1)) AS category, " +
            "ltree2text(subpath(path, 2, 1)) AS size, " +
            "ltree2text(subpath(path, 3, 1)) AS name, " +
            "SUM(purchase_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY type, category, size, name; ", nativeQuery = true)
    Set<StoreThreeAmount> findAllStoreItemPurchase(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 1)) AS type, " +
            "ltree2text(subpath(path, 1, 1)) AS category, " +
            "ltree2text(subpath(path, 2, 1)) AS size, " +
            "ltree2text(subpath(path, 3, 1)) AS name, " +
            "SUM(delivery_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY type, category, size, name; ", nativeQuery = true)
    Set<StoreThreeAmount> findAllStoreItemDelivery(@Param("store_id") Long storeId);
}
