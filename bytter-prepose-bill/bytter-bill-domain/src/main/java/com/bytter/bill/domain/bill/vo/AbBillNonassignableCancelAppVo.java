package com.bytter.bill.domain.bill.vo;
import com.bytter.core.bpm.IBusinessKey;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillNonassignableCancelAppVo extends AbAccrecordsVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 流水id
     */
    private String accIds;
    /**
     * 主表id
     */
    private String infoIds;
    /**
     * 从表id
     */
    private String viceIds;
    @Override
    public String getBusinessKey() {
        return id;
    }
}
