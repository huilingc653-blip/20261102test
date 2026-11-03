package com.bytter.biz.netbank.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description:
 * @date 2018/5/15 9:46
 */
@ExcelTarget("NisProjectVO")
public class NisProjectVO extends ExcelModel implements Serializable {
    @Dict(tableName = "NIS_PROJECT", valueFieldName = "id", showFiledName = "PROJECT_NAME", inject = "projectName")
    private String id;
    /**
     * 项目编码
     */
    @Excel(name = "*项目编码")
    @NotNull(message = "")
    @BtCompositeSearch(fieldName = "p.project_code")
    private String projectCode;
    /**
     * 项目名称
     */
    @Excel(name = "*项目名称")
    @NotNull(message = "")
    @BtCompositeSearch(fieldName = "p.project_name")
    private String projectName;
    private String projectNameShow;
    @Excel(name = "*收支标志")
    @NotNull(message = "")
    @BtCompositeSearch(fieldName = "p.project_cdsign")
    @Dict(dictGroup = "btBankAccDtl.cdSign", inject = "projectCdSignName")
    private String projectCdSign;
    private String projectCdSignName;
    @Excel(name = "*匹配要素")
    @NotNull(message = "")
    @BtCompositeSearch(fieldName = "p.match_attr")
    @Dict(dictGroup = "nisProject.matchAttr", inject = "matchAttrName")
    private String matchAttr;
    private String matchAttrName;
    @Excel(name = "匹配内容")
    @BtCompositeSearch(fieldName = "p.match_content")
    private String matchContent;
    @Excel(name = "*匹配关系")
    @NotNull(message = "")
    @BtCompositeSearch(fieldName = "p.match_relation")
    @Dict(dictGroup = "nisProject.matchRelation", inject = "matchRelationName")
    private String matchRelation;
    private String matchRelationName;
    @BtCompositeSearch(fieldName = "p.project_sort")
    private Integer projectSort;
    @Excel(name = "备注")
    private String rmk;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProjectCode() {
        return projectCode;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectNameShow() {
        return projectNameShow;
    }
    public void setProjectNameShow(String projectNameShow) {
        this.projectNameShow = projectNameShow;
    }
    public String getProjectCdSign() {
        return projectCdSign;
    }
    public void setProjectCdSign(String projectCdSign) {
        this.projectCdSign = projectCdSign;
    }
    public String getProjectCdSignName() {
        return projectCdSignName;
    }
    public void setProjectCdSignName(String projectCdSignName) {
        this.projectCdSignName = projectCdSignName;
    }
    public String getMatchAttr() {
        return matchAttr;
    }
    public void setMatchAttr(String matchAttr) {
        this.matchAttr = matchAttr;
    }
    public String getMatchAttrName() {
        return matchAttrName;
    }
    public void setMatchAttrName(String matchAttrName) {
        this.matchAttrName = matchAttrName;
    }
    public String getMatchContent() {
        return matchContent;
    }
    public void setMatchContent(String matchContent) {
        this.matchContent = matchContent;
    }
    public String getMatchRelation() {
        return matchRelation;
    }
    public void setMatchRelation(String matchRelation) {
        this.matchRelation = matchRelation;
    }
    @Override
    public String getBeanName() {
        return "nisProjectService";
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getProjectSort() {
        return projectSort;
    }
    public void setProjectSort(Integer projectSort) {
        this.projectSort = projectSort;
    }
    public String getMatchRelationName() {
        return matchRelationName;
    }
    public void setMatchRelationName(String matchRelationName) {
        this.matchRelationName = matchRelationName;
    }
}
