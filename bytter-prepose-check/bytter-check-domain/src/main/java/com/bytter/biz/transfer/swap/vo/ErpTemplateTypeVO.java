package com.bytter.biz.transfer.swap.vo;
import com.bytter.biz.transfer.swap.entity.ErpTemplateTypeDtl;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * Created by zhouj on 2018/5/22.
 */
public class ErpTemplateTypeVO {
    private String id;
    @Length(max = 50, message = "typeCode长度不能超过50")
    @NotNull(message = "typeCode不能为空")
    private String typeCode;
    @Length(max = 100, message = "typeName长度不能超过100")
    @NotNull(message = "typeName不能为空")
    private String typeName;
    @Length(max = 50, message = "matchingField长度不能超过50")
    @NotNull(message = "matchingField不能为空")
    private String matchingField;
    private String rmk;
    private String matchingName;
    private List<ErpTemplateTypeDtl> typeDtlList;
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
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getMatchingField() {
        return matchingField;
    }
    public void setMatchingField(String matchingField) {
        this.matchingField = matchingField;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public List<ErpTemplateTypeDtl> getTypeDtlList() {
        return typeDtlList;
    }
    public void setTypeDtlList(List<ErpTemplateTypeDtl> typeDtlList) {
        this.typeDtlList = typeDtlList;
    }
    public String getMatchingName() {
        return matchingName;
    }
    public void setMatchingName(String matchingName) {
        this.matchingName = matchingName;
    }
}
