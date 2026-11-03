package com.bytter.biz.fundtransfer.enums;
/**
 * 归集,下拨类型
 * 
 * @author fangyb
 * 
 */
public enum GatherModeEnum {
	/**
	 * 定时归集方式：
	 * 8-全额归集
	 * 6-取整归集
	 * 1-留底资金归集
	 * 9-固定金额归集
	 * b-比例上划(交易额)
	 * 5-留底取整归集
	 */
	
	/**
	 * 定时下拨方式：
	 * 4-固定余额下拨
	 * 3-固定额度下拨
	 * 0-根据前一日归集金额下拨
	 * a-余额不足时固定额度下拨
	 * 2-比例下拨(交易额)	
	 */
	/** 留底资金归集 **/
	GATHER_HOLD("1", "留底资金归集"),
	
	/** 取整归集 **/
	GATHER_INTEGER("6", "取整归集"),
	
	/** 全额归集 **/
	GATHER_ALL("8", "全额归集"),
	
	/** 定额归集(固定金额归集) **/
	GATHER_CONSTANT("9", "固定金额归集"),
	
	/**比例上划(交易额)**/
	GATHER_PERCENT("b","比 例上划"),
	
    /**留底取整归集**/
	GATHER_HOLD_INTEGER("5","往来款归集"),
	
    /**根据前一日归集金额下拨**/
	APPROPRIATE_TO_GATHER("0","根据前一日归集金额下拨"),
	
	/**比例下拨(交易额)**/
	APPROPRIATE_PERCENT("2","比例下拨"),
	
	/** 定额下拨(固定额度) **/
	APPROPRIATE_CONSTANT("3", "固定额度下拨"),
	
	/** 差额补齐下拨(固定余额) **/
	APPROPRIATE_MARGIN("4", "固定余额下拨"),
	
	/** 补齐下拨(填平下拨) **/
	APPROPRIATE_SUPPLY("7", "填平下拨"),
	
	/**余额不足时固定额度下拨**/
	APPROPRIATE_LACK_OF_BALANCE("a","余额不足时固定额度下拨");
	
	public final String value;
	public final String description;

	GatherModeEnum(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public static GatherModeEnum toEnum(String value) {
		if (value == null) {
			return null;
		}
		for (GatherModeEnum se : GatherModeEnum.values()) {
			if (value.equals(se.value)) {
				return se;
			}
		}
		return null;
	}
}
