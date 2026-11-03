package com.bytter.biz.vo;

import com.bytter.biz.budget.vo.CorpVO;
import com.bytter.biz.budget.vo.FbsCorpBudgetVO;
import com.bytter.system.api.dto.CorpDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FbsCorpBudgetListVO implements Serializable {
    /**
     * 单位列表
     */
    private List<CorpDTO> corpList;
    /**
     * 单位映射
     */
    private Map<String, FbsCorpBudgetVO> corpBudgetMap;
    /**
     *查询
     */
    private String seach;

    /**
     * forwardName  原先跳转页面路径
     */
    private String forwardName;

    /**
     * 单位映射1
     */
    private  Map<String, CorpVO> corpMap;


    private   String netId;
}
