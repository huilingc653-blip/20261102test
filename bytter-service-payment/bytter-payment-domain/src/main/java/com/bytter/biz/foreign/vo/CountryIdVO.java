package com.bytter.biz.foreign.vo;

import lombok.Data;

@Data
public class CountryIdVO implements java.io.Serializable {
    private String sysCountryId;
    private String managerCountryId;
    private String custCountryId;

}
