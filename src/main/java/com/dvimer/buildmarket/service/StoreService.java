package com.dvimer.buildmarket.service;

import com.dvimer.buildmarket.exception.BadRequestException;
import com.dvimer.buildmarket.dao.entity.StoreEntity;
import com.dvimer.buildmarket.dao.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreEntity findById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Store is not found"));
    }
}
