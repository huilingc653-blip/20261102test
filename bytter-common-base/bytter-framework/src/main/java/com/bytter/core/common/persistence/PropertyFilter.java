package com.bytter.core.common.persistence;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.enums.PropertyTypeEnum;
import com.bytter.core.utils.ConvertUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.util.Assert;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
/**
 * 与具体ORM实现无关的属性过滤条件封装类, 主要记录页面中简单的搜索过滤条件.
 */
public class PropertyFilter {
    /** 多个属性间OR关系的分隔符. */
    public static final String OR_SEPARATOR = "_OR_";
    private MatchTypeEnum matchType = null;
    private Object matchValue = null;
    private Class<?> propertyClass = null;
    private String[] propertyNames = null;
    public PropertyFilter() {
    }

    public static void main(String[] args) {
        String filterName = "IS_NULLi_interestRateType";
        String firstPart = StringUtils.substringBefore(filterName, "_");
        if (StrUtil.startWithIgnoreCase(filterName, MatchTypeEnum.IS_NULL.name())){
            firstPart = StringUtils.substringBeforeLast(filterName, "_");
        }
        System.out.println(firstPart);
    }
    /**
     * @param filterName
     *            比较属性字符串,含待比较的比较类型、属性值类型及属性列表. eg. LIKES_NAME_OR_LOGIN_NAME
     * @param value
     *            待比较的值.
     */
    public PropertyFilter(final String filterName, final String value) {
        String firstPart = StringUtils.substringBefore(filterName, "_");
        // 兼容IS_NULL，否则切割字符串会出错
        if (StrUtil.startWithIgnoreCase(filterName, MatchTypeEnum.IS_NULL.name())){
            firstPart = StringUtils.substringBeforeLast(filterName, "_");
        }
        String matchTypeCode = StringUtils.substring(firstPart, 0, firstPart.length() - 1);
        String propertyTypeCode = StringUtils.substring(firstPart, firstPart.length() - 1, firstPart.length());
        try {
            matchType = Enum.valueOf(MatchTypeEnum.class, matchTypeCode.toUpperCase());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型.", e);
        }
        try {
            propertyClass = Enum.valueOf(PropertyTypeEnum.class, propertyTypeCode.toUpperCase()).getValue();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性值类型.", e);
        }
        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        if (StrUtil.startWithIgnoreCase(filterName, MatchTypeEnum.IS_NULL.name())){
            propertyNameStr = StringUtils.substringAfterLast(filterName, "_");
        }
        Assert.isTrue(StringUtils.isNotBlank(propertyNameStr), "filter名称" + filterName + "没有按规则编写,无法得到属性名称.");
        String modified = Pattern.compile(PropertyFilter.OR_SEPARATOR, Pattern.CASE_INSENSITIVE)
                .matcher(propertyNameStr).replaceAll(PropertyFilter.OR_SEPARATOR);
        propertyNames = modified.split(PropertyFilter.OR_SEPARATOR);
        // propertyNames = StringUtils.splitByWholeSeparator(propertyNameStr, PropertyFilter.OR_SEPARATOR);
        // 针对<=日期格式的时候如果日期没有时分秒则追加 23:59:59
        String valueNew = value;
        if (MatchTypeEnum.LE.getValue().equals(matchType.getValue())
                && PropertyTypeEnum.D.getValue().toString().equals(propertyClass.toString())
                && StringUtils.isNotEmpty(value) && value.length() == 10) {
            valueNew = value + " 23:59:59";
        }
        this.matchValue = ConvertUtils.convertStringToObject(valueNew, propertyClass);
    }
    /**
     * 从HttpRequest中创建PropertyFilter列表, 默认Filter属性名前缀为filter.
     * 
     * @see #buildFromHttpRequest(HttpServletRequest, String)
     */
    public static List<PropertyFilter> buildFromHttpRequest(final HttpServletRequest request) {
        return buildFromHttpRequest(request, "filter");
    }
    /**
     * 从HttpRequest中创建PropertyFilter列表 PropertyFilter命名规则为Filter属性前缀_比较类型属性类型_属性名.
     * 
     * eg. filter_EQS_name filter_LIKES_name_OR_email
     */
    public static List<PropertyFilter> buildFromHttpRequest(final HttpServletRequest request,
            final String filterPrefix) {
        List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();
        // 从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
        Map<String, Object> filterParamMap = ServletUtils.getParametersStartingWith(request, filterPrefix + "_");
        // 分析参数Map,构造PropertyFilter列表
        for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
            String filterName = entry.getKey();
            String value = (String) entry.getValue();
            // 如果value值为空,则忽略此filter.
            if (StringUtils.isNotBlank(value)) {
                PropertyFilter filter = new PropertyFilter(filterName, value);
                filterList.add(filter);
            }
        }
        return filterList;
    }
    /**
     * 获取比较值的类型.
     */
    public Class<?> getPropertyClass() {
        return propertyClass;
    }
    /**
     * 获取比较方式.
     */
    public MatchTypeEnum getMatchType() {
        return matchType;
    }
    /**
     * 获取比较值.
     */
    public Object getMatchValue() {
        return matchValue;
    }
    /**
     * 获取比较属性名称列表.
     */
    public String[] getPropertyNames() {
        return propertyNames;
    }
    /**
     * 获取唯一的比较属性名称.
     */
    public String getPropertyName() {
        Assert.isTrue(propertyNames.length == 1, "There are not only one property in this filter.");
        return propertyNames[0];
    }
    /**
     * 是否比较多个属性.
     */
    public boolean hasMultiProperties() {
        return (propertyNames.length > 1);
    }
}
