package com.bytter.poi.util;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 校验工具类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class PoiValidationUtil {
    private final static Validator VALIDATOR;
    static {
        ValidatorFactory factory = Validation.byDefaultProvider().configure()
                .traversableResolver(new JPAIgnoreTraversableResolver()).buildValidatorFactory();
        VALIDATOR = factory.getValidator();
    }
    /**
     * 校验对象
     *
     * @param obj
     *            对象
     * @return 校验结果
     */
    public static String validation(Object obj) {
        Set<ConstraintViolation<Object>> set = VALIDATOR.validate(obj);
        if (set.size() > 0) {
            return getValidateErrMsg(set);
        }
        return null;
    }
    /**
     * 校验错误信息
     *
     * @param set
     * @return
     */
    private static String getValidateErrMsg(Set<ConstraintViolation<Object>> set) {
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<Object> constraintViolation : set) {
            builder.append(constraintViolation.getMessage()).append(",");
        }
        return builder.substring(0, builder.length() - 1).toString();
    }
    /**
     * 判断对象的所有成员变量是否为空
     *
     * @param obj
     *            判断对象
     * @param isExcludeZero
     *            :true:数值类型的值为0,则当做为空;false:数值类型的值为0,则不为空
     * @return 是否为空
     * @throws SecurityException
     * @throws IllegalArgumentException
     */
    public static boolean isNullObject(Object obj, boolean isExcludeZero)
            throws SecurityException, IllegalArgumentException {
        if (isNullOrEmpty(obj)) {
            // 对象本身就为null
            return true;
        }
        List<Field> fieldList = PoiReflectorUtil.fields(obj.getClass());
        boolean isNull = true;
        for (int i = 0; i < fieldList.size(); i++) {
            Field f = fieldList.get(i);
            Object propertyValue = null;
            try {
                propertyValue = PoiReflectorUtil.getFieldValue(obj, f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!isNullOrEmpty(propertyValue)) {
                // 字段不为空
                if (propertyValue instanceof Integer) {
                    // 是数字
                    if (!((Integer) propertyValue == 0 && isExcludeZero)) {
                        isNull = false;
                        break;
                    }
                } else if (propertyValue instanceof Double) {
                    // 是数字
                    if (!((Double) propertyValue == 0 && isExcludeZero)) {
                        isNull = false;
                        break;
                    }
                } else if (propertyValue instanceof Float) {
                    // 是数字
                    if (!((Float) propertyValue == 0 && isExcludeZero)) {
                        isNull = false;
                        break;
                    }
                } else if (propertyValue instanceof Short) {
                    // 是数字
                    if (!((Short) propertyValue == 0 && isExcludeZero)) {
                        isNull = false;
                        break;
                    }
                } else if (propertyValue instanceof BigDecimal) {
                    // 是数字
                    if (!(((BigDecimal) propertyValue).compareTo(BigDecimal.ZERO) == 0 && isExcludeZero)) {
                        isNull = false;
                        break;
                    }
                } else {
                    isNull = false;
                    break;
                }
            }
        }
        return isNull;
    }
    private static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }
}
