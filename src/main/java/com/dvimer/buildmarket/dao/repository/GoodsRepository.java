package com.dvimer.buildmarket.dao.repository;

import com.dvimer.buildmarket.dao.entity.GoodsEntity;
import com.dvimer.buildmarket.dao.helpers.PathAmount;
import com.dvimer.buildmarket.dao.helpers.StoreThreeAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsRepository extends JpaRepository<GoodsEntity, Long> {
    //todo тут лучше сделать через Specification, но для скорости копипастнул
    @Query(value = "SELECT ltree2text(subpath(path, 0, 1)) AS subpath, SUM(delivery_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByTypeAndDeliveryPrice(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 2)) AS subpath, SUM(delivery_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByCategoryAndDeliveryPrice(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 3)) AS subpath, SUM(delivery_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllBySizeAndDeliveryPrice(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 4)) AS subpath, SUM(delivery_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByNameAndDeliveryPrice(@Param("store_id") Long storeId);


    @Query(value = "SELECT ltree2text(subpath(path, 0, 1)) AS subpath, SUM(installation_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByTypeAndInstallationPrice(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 2)) AS subpath, SUM(installation_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByCategoryAndInstallationPrice(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 3)) AS subpath, SUM(installation_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllBySizeAndInstallationPrice(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 4)) AS subpath, SUM(installation_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByNameAndInstallationPrice(@Param("store_id") Long storeId);


    @Query(value = "SELECT ltree2text(subpath(path, 0, 1)) AS subpath, SUM(purchase_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByTypeAndPurchasePrice(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 2)) AS subpath, SUM(purchase_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByCategoryAndPurchasePrice(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 3)) AS subpath, SUM(purchase_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllBySizeAndPurchasePrice(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 4)) AS subpath, SUM(purchase_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByNameAndPurchasePrice(@Param("store_id") Long storeId);


    //// TODO: 23.05.2021 не используется(есть идея с 1 запросом в базу и обработкой в коде)
    @Query(value = "SELECT ltree2text(subpath(path, 0, 1)) AS type, " +
            "ltree2text(subpath(path, 1, 1)) AS category, " +
            "ltree2text(subpath(path, 2, 1)) AS size, " +
            "ltree2text(subpath(path, 3, 1)) AS name, " +
            "SUM(delivery_price) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY type, category, size, name; ", nativeQuery = true)
    List<StoreThreeAmount> findAllStoreItem(@Param("store_id") Long storeId);
}
