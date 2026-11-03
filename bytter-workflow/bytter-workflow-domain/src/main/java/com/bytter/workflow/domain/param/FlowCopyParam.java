package com.bytter.workflow.domain.param;

import com.bytter.workflow.domain.entity.FlowModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工作流-参数
 *
 * @author chenlw 2021-03-15
 * @since 1.0.0
 */
@Data
public class FlowCopyParam implements Serializable {

    /**
     * 成员单位ID
     */
    protected String corpId;

    /**
     * 成员单位IDs
     */
    protected List<String> corpIds;

    /**
     * 流程定义KEY
     */
    protected String cateId;

    /**
     * 流程定义KEYs
     */
    private List<String> cateIds;

    public static FlowCopyParam buildCopyParam(CopyType copyType, List<String> copyKeys, FlowModel model) {
        FlowCopyParam flowCopyParam = new FlowCopyParam();
        if (FlowCopyParam.CopyType.CORP == copyType) {
            flowCopyParam.setCorpIds(copyKeys);
            flowCopyParam.setCateId(model.getCategoryId());
        } else {
            flowCopyParam.setCateIds(copyKeys);
            flowCopyParam.setCorpId(model.getCorpId());
        }
        return flowCopyParam;
    }

    public enum CopyType {

        /**
         * 按照单位复制
         */
        CORP,

        /**
         * 按照业务复制
         */
        BUSINESS
    }

}
