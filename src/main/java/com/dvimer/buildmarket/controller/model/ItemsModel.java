package com.dvimer.buildmarket.controller.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ItemsModel {
    private String name;
    private String prop;
    private Long value;
    private Map<String, ItemsModel> items;
}
