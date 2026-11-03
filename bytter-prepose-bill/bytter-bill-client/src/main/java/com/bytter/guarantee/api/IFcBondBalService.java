package com.bytter.guarantee.api;

import com.bytter.guarantee.vo.FcBondBalVO;

import java.math.BigDecimal;

public interface IFcBondBalService {
    /**
     * 根据bondId获取保证金余额数据
     */
    public FcBondBalVO getBondBalByBondId(String bondId);

    /**
     * 更新保证金余额表中相应的信息
     * @param fcBondBal
     */
    public void updateFcBondBal(FcBondBalVO fcBondBal);
    public void deleteByBondId(String bondId);
    public void backBondAddMoney(String bondId, BigDecimal money);

}
