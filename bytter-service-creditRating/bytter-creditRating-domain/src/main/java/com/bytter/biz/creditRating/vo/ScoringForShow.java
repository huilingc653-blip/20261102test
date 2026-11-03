package com.bytter.biz.creditRating.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoringForShow implements Serializable {
    private String newScoringIds;
    private String oldScoringIds;
    private String modelId;
    private List<ScCoefficients> scCoefficientsArray;
}
