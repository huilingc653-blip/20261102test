package com.bytter.guarantee.constant;
import java.io.Serializable;
/**
 * 担保工作流常量类
 * 
 * @author luxy
 * @version 1.0
 */
@SuppressWarnings("serial")
public class GuaWorkFlowConstant implements Serializable {
    /**
     * 抵质押物登记
     */
    public final static String GUA_PLEDGE_REGIST = "guaPledgeRegist";
    /**
     * 担保合同登记
     */
    public final static String GUA_CONTRACT_REGIST = "guaContractRegist";
    /**
     * 担保合同变更
     */
    public final static String GUA_CONTRACT_CHANGE_REGIST = "guaContractChangeRegist";
    /**
     * 对外担保合同登记
     */
    public final static String GUA_EXTCONTRACT_REGIST = "guaExtContractRegist";
    /**
     * 对外担保合同终止
     */
    public final static String GUA_EXTCONTRACT_END = "guaExtContractEnd";
}
