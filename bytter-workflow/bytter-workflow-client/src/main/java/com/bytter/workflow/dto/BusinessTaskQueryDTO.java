package com.bytter.workflow.dto;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.IParameterInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
@Data
public class BusinessTaskQueryDTO implements Serializable {
    private String cateKey;
    private String userId;
    //
    private Date cycleTime;
    private FLowStatus fLowStatus;
    /**
     * 原始基于接口传递参数转换
     * @param cateKey 分类key
     * @param pagePara 分页
     * @return
     */
    public static BusinessTaskQueryDTO change(String cateKey, IParameterInput pagePara) {
        IParameter parameter = pagePara.getParameter();
        String flowStatus = parameter.get("flowStatus");
        String cycleTime = parameter.get("cycleTime");
        BusinessTaskQueryDTO queryDTO = new BusinessTaskQueryDTO();
        queryDTO.setCateKey(cateKey);
        queryDTO.setUserId(pagePara.getCurrentUserId());
        queryDTO.setFLowStatus(FLowStatus.of(flowStatus));
        return queryDTO;
    }
    @AllArgsConstructor
    @Getter
    public enum FLowStatus{
        TODO("待办"),
        TRACK("跟踪"),
        PASS("已完成"),
        ;
        private final String desc;
        public static FLowStatus of(String status) {
            return Arrays.stream(FLowStatus.values()).filter(item -> item.name().equalsIgnoreCase(status))
                    .findFirst().orElse(FLowStatus.TODO);
        }
    }
}
