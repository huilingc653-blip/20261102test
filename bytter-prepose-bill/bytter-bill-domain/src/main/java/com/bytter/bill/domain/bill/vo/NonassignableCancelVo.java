package com.bytter.bill.domain.bill.vo;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
/**
 * @author zouxs
 * @version 1.0
 * @Title: VO
 * @Description: 不可转让撤销Vo
 * @date 2023-03-01
 */
public class NonassignableCancelVo implements Serializable, IBusinessKey {
    private static final long serialVersionUID = 268419642335987546L;
    @Override
    public String getBusinessKey() {
        return null;
    }
}
