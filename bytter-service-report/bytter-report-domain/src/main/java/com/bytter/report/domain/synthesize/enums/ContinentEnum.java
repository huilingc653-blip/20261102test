package com.bytter.report.domain.synthesize.enums;
/**
 * 洲枚举类
 *
 * @author zuoy
 * @since 2020-01-16
 */
public enum ContinentEnum {
    // CPD-Ignore-On
    NORTH_AMERICA("北美洲", 1, "bi.money.map.northAmerica"),
    SOUTH_AMERICA("南美洲", 2, "bi.money.map.southAmerica"),
    EUROPE("欧洲", 3, "bi.money.map.europe"),
    ASIA_OF_REST("亚洲其他地区", 4, "bi.money.map.otherPartsOfAsia"),
    CHAIN("中国", 5, "bi.money.map.china"),
    SOUTHEAST_ASIA_AND_JAPAN_AND_KOREA("东南亚及日韩", 6, "bi.money.map.southeastAsiaAndJapanAndKorea"),
    AFRICA("非洲", 7, "bi.money.map.africa"),
    OCEANIA("大洋洲", 8, "bi.money.map.oceania"),
    antarctica_or_unknown("南极洲或未知领域", 9, "bi.money.map.antarctica.or.unknown"),
    // CPD-Ignore-Off
    ;
    private String name;
    private Integer value;
    private String langKey;
    ContinentEnum(String name, Integer value, String langKey) {
        this.name = name;
        this.value = value;
        this.langKey = langKey;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
    public String getLangKey() {
        return langKey;
    }
    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }
}
