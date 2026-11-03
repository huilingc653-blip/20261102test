package com.bytter.guarantee.domain.vo;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaContractUseVO
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/4/29 10:03
 */
public class GuaContractUseVO {
    /**
     * 担保合同id
     */
    private String contractId;
    /**
     * 担保合同id
     */
    private List<GuaContractUseDtlVO> useDtls;
    /** 状态 */
    private Integer status;
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
    public List<GuaContractUseDtlVO> getUseDtls() {
        return useDtls;
    }
    public void setUseDtls(List<GuaContractUseDtlVO> useDtls) {
        this.useDtls = useDtls;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
