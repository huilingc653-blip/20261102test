package com.bytter.bill.domain.billbank.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
public class BtValueSetter implements java.io.Serializable {
	/* 通过映射字段将一条sql查询出来的结果映射成javabean */
	public static Object mapping(String fields, Object[] records,
			Class beanclass) {
		HashMap map = new HashMap();
		StringTokenizer field = new StringTokenizer(fields, ",");
		Object retObj = null;
		int i = 0;
		while (field.hasMoreTokens()) {
			map.put(field.nextToken(), records[i]);
			i = i + 1;
		}
		try {
			retObj = beanclass.newInstance();
			BeanUtil.copyProperties(map, retObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retObj;
	}
	/* 根据字段从sql查询的Object[]数组映射对象列表 */
	public static List mapping(String fields, List records, Class beanclass) {
		List lst = new ArrayList();
		Iterator it = records.iterator();
		while (it.hasNext()) {
			Object[] oneline = (Object[]) it.next();
			lst.add(mapping(fields, oneline, beanclass));
		}
		return lst;
	}
	public static void main(String[] args) throws Exception {
	}
}
