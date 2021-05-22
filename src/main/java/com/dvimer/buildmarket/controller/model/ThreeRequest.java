package com.dvimer.buildmarket.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ThreeRequest {
    @JsonProperty("id_tree")
    private Long id_tree;

    @JsonProperty("prop")
    private String prop;
}
