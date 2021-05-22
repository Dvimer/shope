package com.dvimer.buildmarket.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "store")
@NoArgsConstructor
@Entity
@Table(name = "goods")
public class GoodsEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private StoreEntity store;

    private String path;

    private BigDecimal amount;
}
