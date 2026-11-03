package com.bytter.biz.transfer.swap.entity;
import com.bytter.biz.transfer.swap.vo.ErpTemplateTypeVO;
import com.bytter.check.enums.TemplateTypeEnum;
import com.bytter.core.base.entity.IdEntity;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
/**
 * Created by zhouj on 2018/5/18.
 */
@Entity
@Table(name = "ERP_TEMPLATE_TYPE", schema = "")
public class ErpTemplateType extends IdEntity implements java.io.Serializable{
    private String typeCode;
    private String typeName;
    private String matchingField;
    private String rmk;
    private Timestamp opDate;
    private String status = TemplateTypeEnum.TEMP_STATUS_TEMP.getCode();
	public ErpTemplateType(){
        super();
    }
    public ErpTemplateType(ErpTemplateTypeVO type) {
        super();
        this.typeCode = type.getTypeCode();
        this.typeName = type.getTypeName();
        this.matchingField = type.getMatchingField();
        this.rmk = type.getRmk();
        this.opDate = new Timestamp(System.currentTimeMillis());
    }
    public ErpTemplateType(ErpTemplateTypeVO type, long time) {
        super();
        this.setId(type.getId());
        this.typeCode = type.getTypeCode();
        this.typeName = type.getTypeName();
        this.matchingField = type.getMatchingField();
        this.rmk = type.getRmk();
        this.opDate = new Timestamp(time);
    }
    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
    	return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    @Basic
    @Column(name = "TYPE_CODE", nullable = true, length = 50)
    public String getTypeCode() {
        return typeCode;
    }
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    @Basic
    @Column(name = "TYPE_NAME", nullable = true, length = 100)
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    @Basic
    @Column(name = "MATCHING_FIELD", nullable = true, length = 50)
    public String getMatchingField() {
        return matchingField;
    }
    public void setMatchingField(String matchingField) {
        this.matchingField = matchingField;
    }
    @Basic
    @Column(name = "RMK", nullable = true, length = 500)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Basic
    @Column(name = "OP_DATE", nullable = true)
    public Timestamp getOpDate() {
        return opDate;
    }
    public void setOpDate(Timestamp opDate) {
        this.opDate = opDate;
    }
}
