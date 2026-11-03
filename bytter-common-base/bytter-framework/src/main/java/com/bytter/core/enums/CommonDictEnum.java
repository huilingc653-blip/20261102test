package com.bytter.core.enums;
import cn.hutool.core.map.MapUtil;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * 常用固定枚举。
 *
 * @author zhaohc
 * @date 2023/4/3 18:23
 */
public enum CommonDictEnum {
    /* 是、否 */
    SF_YN_Y("sf_yn", "1", "是",""),
    SF_YN_N("sf_yn", "0", "否",""),
    ;
    private final String group;
    private final String code;
    private final String value;
    // 针对部分需要国际化的枚举
    private final String enValue;
    CommonDictEnum(String group, String code, String value, String enValue) {
        this.group = group;
        this.code = code;
        this.value = value;
        this.enValue = enValue;
    }
    public String getGroup() {
        return group;
    }
    public String getCode() {
        return code;
    }
    public String getValue() {
        return value;
    }
    public String getEnValue() {
        return enValue;
    }
    /**
     * 根据分组获取当前分组枚举信息。
     *
     * @param group 分组名。
     * @return 枚举Map。
     */
    public static Map<String, String> getDictByGroup(String group) {
        return Arrays.stream(CommonDictEnum.values()).
                filter(dict -> dict.group.equals(group)).
                collect(Collectors.toMap(
                        dict -> dict.code,
                        dict -> dict.value
                ));
    }
    /**
     * 根据分组名和枚举code获取枚举值。
     *
     * @param group 分组名。
     * @param code  枚举名。
     * @return 枚举值。
     */
    public static String getDictValue(String group, String code) {
        Map<String, String> dictByGroup = getDictByGroup(group);
        if (MapUtil.isNotEmpty(dictByGroup)) {
            return Optional.ofNullable(dictByGroup.get(code)).orElse("");
        }
        return "";
    }
}
