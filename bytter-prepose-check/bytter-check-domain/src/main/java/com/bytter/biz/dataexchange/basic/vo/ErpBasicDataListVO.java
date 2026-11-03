package com.bytter.biz.dataexchange.basic.vo;
import java.util.List;
/**
 * Created by zhangxb on 2017/12/14.
 */
public class ErpBasicDataListVO {
    /**对应数据类型*/
    private String dataTypeCode;
    /**对应接口类型*/
    private String typeCode;
    private List<ErpBasicDataVO> erpBasicDataVOS;
    public String getDataTypeCode() {
        return dataTypeCode;
    }
    public void setDataTypeCode(String dataTypeCode) {
        this.dataTypeCode = dataTypeCode;
    }
    public String getTypeCode() {
        return typeCode;
    }
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    public List<ErpBasicDataVO> getErpBasicDataVOS() {
        return erpBasicDataVOS;
    }
    public void setErpBasicDataVOS(List<ErpBasicDataVO> erpBasicDataVOS) {
        this.erpBasicDataVOS = erpBasicDataVOS;
    }
}
