package com.bytter.system.api.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>类功能：</b>利率调整信息bean<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月6日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public class InstrateAdjustBean implements Serializable {
	 private String account;
	    /**
	     * key = 利率类型 value = 账户利率信息
	     */
	    private Map<String, List<AccInstrateBean>> adjustTypeInstrateInfo = new HashMap<String, List<AccInstrateBean>>();
	    public String getAccount() {
	        return account;
	    }
	    public void setAccount(String account) {
	        this.account = account;
	    }
	    public Map<String, List<AccInstrateBean>> getAdjustTypeInstrateInfo() {
	        return adjustTypeInstrateInfo;
	    }
	    public void setAdjustTypeInstrateInfo(Map<String, List<AccInstrateBean>> adjustTypeInstrateInfo) {
	        this.adjustTypeInstrateInfo = adjustTypeInstrateInfo;
	    }
	    public List<AccInstrateBean> getAccInstratesByAdjustType(String adjustType) {
	        return getAccInstratesByAdjustType(adjustType, false);
	    }
	    public List<AccInstrateBean> getAccInstratesByAdjustType(String adjustType, boolean null2create) {
	        List<AccInstrateBean> instrateInfos = adjustTypeInstrateInfo.get(adjustType);
	        if (instrateInfos == null && null2create) {
	            instrateInfos = new ArrayList<AccInstrateBean>();
	            adjustTypeInstrateInfo.put(adjustType, instrateInfos);
	        }
	        return instrateInfos;
	    }
}
