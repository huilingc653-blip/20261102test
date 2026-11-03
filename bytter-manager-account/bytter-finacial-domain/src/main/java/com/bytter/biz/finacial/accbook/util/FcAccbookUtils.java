package com.bytter.biz.finacial.accbook.util;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import java.io.Serializable;
/**
 * 记账列表查询显示账务数据
 * 
 * @author liyh
 *
 */
public class FcAccbookUtils implements Serializable {
	/**
	 * 比较校验状态是否在待校验的列表中
	 * @param voucherStatus
	 * @param validStatus
	 */
	public static void validVoucherStatus(String voucherStatus, String[] validStatus) {
		if (EmptyUtils.isEmpty(validStatus)) {
			return;
		}
		boolean flag = false;
		for (int i = 0, length = validStatus.length; i < length; i++) {
			if (voucherStatus.equals(validStatus[i])) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE);//状态已改变，刷新页面重试
		}
	}
	
	
}
