package com.bytter.biz.creditRating.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScCoefficients implements Serializable {
    private String id;
    private BigDecimal value;
}
