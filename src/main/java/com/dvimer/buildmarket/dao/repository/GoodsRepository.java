package com.dvimer.buildmarket.dao.repository;

import com.dvimer.buildmarket.dao.entity.GoodsEntity;
import com.dvimer.buildmarket.dao.entity.PathAmount;
import com.dvimer.buildmarket.dao.entity.StoreThreeAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsRepository extends JpaRepository<GoodsEntity, Long> {

    @Query(value = "SELECT ltree2text(subpath(path, 0, 1)) AS subpath, SUM(amount) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByType(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 2)) AS subpath, SUM(amount) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByCategory(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 3)) AS subpath, SUM(amount) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllBySize(@Param("store_id") Long storeId);

    @Query(value = "SELECT ltree2text(subpath(path, 0, 4)) AS subpath, SUM(amount) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY subpath;", nativeQuery = true)
    List<PathAmount> findAllByName(@Param("store_id") Long storeId);


    @Query(value = "SELECT ltree2text(subpath(path, 0, 1)) AS type, " +
            "ltree2text(subpath(path, 1, 1)) AS category, " +
            "ltree2text(subpath(path, 2, 1)) AS size, " +
            "ltree2text(subpath(path, 3, 1)) AS name, " +
            "SUM(amount) " +
            "FROM goods " +
            "WHERE store_id = :store_id " +
            "GROUP BY type, category, size, name; ", nativeQuery = true)
    List<StoreThreeAmount> findAllStoreItem(@Param("store_id") Long storeId);
}
