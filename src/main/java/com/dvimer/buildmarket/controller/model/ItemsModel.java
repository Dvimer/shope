package com.dvimer.buildmarket.controller.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ItemsModel {
    private String name;
    private String path;
    private String prop;
    private Long value;
    private List<ItemsModel> items;
}
