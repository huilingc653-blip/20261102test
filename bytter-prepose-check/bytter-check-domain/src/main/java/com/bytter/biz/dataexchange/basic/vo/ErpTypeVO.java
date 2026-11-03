package com.bytter.biz.dataexchange.basic.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import org.apache.commons.lang.StringUtils;
/**
 * Created by zhangxb on 2017/12/13.
 */
public class ErpTypeVO {
    private String id;
    /**系统代码*/
    @BtCompositeSearch(fieldName = "typeCode")
    private String typeCode;
    /**系统名称*/
    @BtCompositeSearch(fieldName = "typeName")
    private String typeName;
    /**备注*/
    private String rmk;
	/** 显示名称 **/
	private String showName;
	
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTypeCode() {
        return typeCode;
    }
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
        setShowName(typeCode, null);
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
        setShowName(null, typeName);
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
	public String getShowName() {
		return showName;
	}
	
	private void setShowName(String typeCode,String typeName) {
		String showName=getShowName();
		if(StringUtils.isEmpty(showName)){
			showName="";
		}
		if(StringUtils.isNotEmpty(typeCode)){
			setShowName("("+typeCode+")"+showName);
		}
		if(StringUtils.isNotEmpty(typeName)){
			setShowName(showName+typeName);
		}
	}
	
	public void setShowName(String showName) {
		this.showName = showName;
	}
}
