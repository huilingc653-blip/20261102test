package com.bytter.bill.domain.billbank.util;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.springframework.beans.BeansException;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
public class BeanUtil {
	public static void copyProperties(Object orig,Object dest) {
		try {
			BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
			if (dest == null) {
				throw new IllegalArgumentException(
						"No destination bean specified");
			}
			if (orig == null) {
				throw new IllegalArgumentException("No origin bean specified");
			}
			if (orig instanceof DynaBean) {
				DynaProperty[] origDescriptors = ((DynaBean) orig)
						.getDynaClass().getDynaProperties();
				for (int i = 0; i < origDescriptors.length; ++i) {
					String name = origDescriptors[i].getName();
					if ((!beanUtils.getPropertyUtils().isReadable(orig, name))
							|| (!beanUtils.getPropertyUtils().isWriteable(dest,
									name)))
						continue;
					Object value = ((DynaBean) orig).get(name);
					beanUtils.copyProperty(dest, name, value);
				}
			} else if (orig instanceof Map) {
				Iterator entries = ((Map) orig).entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry entry = (Map.Entry) entries.next();
					String name = (String) entry.getKey();
					if (beanUtils.getPropertyUtils().isWriteable(dest, name))
						beanUtils.copyProperty(dest, name, entry.getValue());
				}
			} else {
				PropertyDescriptor[] origDescriptors = beanUtils
						.getPropertyUtils().getPropertyDescriptors(orig);
				for (int i = 0; i < origDescriptors.length; ++i) {
					String name = origDescriptors[i].getName();
					if ("class".equals(name)) {
						continue;
					}
					if ((!beanUtils.getPropertyUtils().isReadable(orig, name))
							|| (!beanUtils.getPropertyUtils().isWriteable(dest,
									name)))
						continue;
					try {
						Object value = beanUtils.getPropertyUtils()
								.getSimpleProperty(orig, name);
						if (value != null)
							beanUtils.copyProperty(dest, name, value);
					} catch (NoSuchMethodException localNoSuchMethodException) {
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * MAP集合转成bean对象集合
	 * 
	 * @param listResult
	 * @param clazz
	 * @param <B>
	 * @return
	 * @author zuoy
	 */
	public static <B> List<B> copyMapListProperties(List listResult,
			Class<B> clazz) {
		List<B> sourceList = new ArrayList();
		Map<String, Object> cacheMap = new HashMap<String, Object>();
		// 遍历MAP结果集
		for (Object obj : listResult) {
			Map map = (Map) obj;
			B source;
			if (null != clazz) {
				source = copyMapProperties(map, clazz, cacheMap);
			} else {
				Map<String, Object> newMap = new HashMap<String, Object>();
				Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<String, Object> entry = entries.next();
					newMap.put(entry.getKey().toLowerCase(), entry.getValue());
				}
				source = (B) newMap;
			}
			sourceList.add(source);
		}
		return sourceList;
	}
	/**
	 * MAP转成bean对象集合
	 * 
	 * @param map
	 * @param clazz
	 * @param <B>
	 * @return
	 * @author zuoy
	 */
	public static <B> B copyMapProperties(Map map, Class<B> clazz) {
		Map<String, Object> cacheMap = new HashMap<String, Object>();
		return copyMapProperties(map, clazz, cacheMap);
	}
	/**
	 * 递归获取当前class的所有父类中的属性
	 * 
	 * @param clazz
	 * @param fieldList
	 */
	public static void getAllFields(Class<?> clazz, List<Field> fieldList) {
		Class superClass = clazz.getSuperclass();
		if (superClass != Object.class) {
			getAllFields(superClass, fieldList);
		}
		Field[] fields = clazz.getDeclaredFields();
		fieldList.addAll(Arrays.asList(fields));
	}
	/**
	 * MAP转成bean对象集合
	 * 
	 * @param map
	 * @param clazz
	 * @param cacheMap
	 * @param <B>
	 * @return
	 * @author zuoy
	 */
	private static <B> B copyMapProperties(Map map, Class<B> clazz,
			Map<String, Object> cacheMap) {
		Assert.notNull(map, "map must not be null");
		Assert.notNull(clazz, "clazz must not be null");
		List<Field> fieldList = (List<Field>) cacheMap.get(clazz);
		if (fieldList == null) {
			fieldList = new ArrayList();
			getAllFields(clazz, fieldList);
		}
		B source = null;
		try {
			source = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		// 遍历结果MAP
		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			// 遍历映射实体属性
			for (Field field : fieldList) {
				String key = entry.getKey();
				Boolean flag = (Boolean) cacheMap.get(key + field.getName());
				if (flag == null) {
					flag = !key.replace("_", "").equalsIgnoreCase(
							field.getName());
					cacheMap.put(key + field.getName(), flag);
				}
				if (flag) {
					continue;
				}
				Object value = entry.getValue();
				Class typeClass = field.getType();
				// 这里去掉typeClass == double.class是为了代码规范，保证bean中的属性使用包装类
				if (value instanceof BigDecimal) {
					BigDecimal bigDecimal = (BigDecimal) value;
					if (typeClass == Double.class) {
						value = bigDecimal.doubleValue();
					} else if (typeClass == Integer.class) {
						value = bigDecimal.intValue();
					} else if (typeClass == Long.class) {
						value = bigDecimal.longValue();
					}
				} else if ((value instanceof Character)
						&& typeClass == String.class) {
					Character character = (Character) value;
					value = character.toString();
				}
				ReflectionUtils.makeAccessible(field);
				ReflectionUtils.setField(field, source, value);
			}
		}
		return source;
	}
	/**
	 * 作者：zuoy 批量转换List<Bean>
	 * 
	 * @param source
	 *            list集合
	 * @param t
	 *            所成对象的class
	 * @param <B>
	 * @return List<T>
	 * @throws BeansException
	 */
	public static <S, B> List<B> copyListProperties(List<S> source, Class<B> t)
			throws BeansException {
		List<B> list = new ArrayList();
		if (source == null) {
			return list;
		}
		try {
			for (Object o : source) {
				B tObj = t.newInstance();
				copyProperties(o, tObj);
				list.add(tObj);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static Map<String, Object> beanToMap(Object obj) {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}
		return map;
	}
	public static <B> B copyNewInstanceProperties(Object source, Class<B> t)
			throws BeansException {
		B tObj = null;
		try {
			tObj = t.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		copyProperties(source, tObj);
		return tObj;
	}
}
