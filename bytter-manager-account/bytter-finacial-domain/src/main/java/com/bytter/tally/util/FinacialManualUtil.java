package com.bytter.tally.util;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.tally.api.constant.FinacialManualConstant;
import com.bytter.tally.entity.TallyFcAccbook;
import com.bytter.tally.api.enums.CollectMessageEnum;
import com.bytter.biz.finacial.api.dto.FcManualFinacialColumnDTO;
import javax.persistence.Column;
import java.lang.reflect.Method;
public class FinacialManualUtil {
	/**
	 * source + symbol + addStr 的字符串拼接
	 * @param source
	 * @param addStr
	 * @param symbol
	 * @return
	 */
	public static StringBuilder StringAddSymbol(StringBuilder source, String addStr, String symbol){
		if(!EmptyUtils.isEmpty(source)){
			source.append(symbol).append(addStr);
		}else{
			source = new StringBuilder();
			source.append(addStr);
		}
		return source;
	}
	/**
	 * 拼装insert语句
	 * @param tableName
	 * @param
	 * @param values
	 * @return
	 */
	public static StringBuilder updateSql(String tableName, StringBuilder values, String businessId, String globalId){
		if(!EmptyUtils.isEmpty(tableName) && !EmptyUtils.isEmpty(values)
				&& !EmptyUtils.isEmpty(businessId) && !EmptyUtils.isEmpty(globalId)){
			return new StringBuilder("update " + tableName + " set " + values + " where " + businessId + " = :" + businessId);
		}
		return null;
	}
	/**
	 * 获取column中columnValue的值
	 * @param accBook
	 * @param column
	 * @return
	 */
	public static Object getColumnValue(TallyFcAccbook accBook, FcManualFinacialColumnDTO column){
		if(EmptyUtils.isEmpty(column)){
			return null;
		}
		String reulst = null;
		if(FinacialManualConstant.COLUMN_VALUE_TYPE_1.equals(column.getWriteBackValueType())){//对象获取值类型
			Class bookClass = accBook.getClass();
			Method[] methods = bookClass.getMethods();
			for(Method method : methods){
				Column columnAnnotation = method.getAnnotation(Column.class);
				if(columnAnnotation != null){
					if(columnAnnotation.name().equalsIgnoreCase(column.getWriteBackValueColumn())){
						try {
							Object o = method.invoke(accBook);
							if (null != o) {
								reulst = o.toString();
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
                }
			}
		}else{//常量类型
			reulst = column.getWriteBackValueColumn();
		}
		return reulst;
	}
	/**
	 * 比较校验状态是否在待校验的列表中
	 * @param voucherStatus
	 * @param validStatus
	 */
	public static void validBillStatus(String voucherStatus, String[] validStatus) {
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
			throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
		}
	}
}
