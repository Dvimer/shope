package com.dvimer.buildmarket.controller;

import com.dvimer.buildmarket.controller.model.ItemsModel;
import com.dvimer.buildmarket.controller.model.ThreeRequest;
import com.dvimer.buildmarket.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BuildingMarketController {

    public final GoodsService goodsService;

    @RequestMapping(value = "/calc",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    ResponseEntity<List<ItemsModel>> calc(@RequestBody ThreeRequest request) throws IOException {

        List<ItemsModel> itemsModels = goodsService.findGoodsThree(request.getId_tree(), request.getProp());
        return ResponseEntity.ok(itemsModels);
    }
}

