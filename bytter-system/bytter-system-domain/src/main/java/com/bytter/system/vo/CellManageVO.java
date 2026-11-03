package com.bytter.system.vo;
import com.bytter.core.annotation.Dict;
import java.util.List;
/**
 * @author wangzy
 * @Title: CellManageVO
 * @Description: 管理单元VO
 * @date 2020/5/12 11:22
 */
public class CellManageVO implements java.io.Serializable {
//    private java.util.Date payDate;
    /**
     * 管理单元Id
     */
    private String id;
    /**
     * 管理单元名称
     */
    private String cellCodeAndName;
    /**
     * 上级单元
     */
    private String parentCell;
    /**
     * 管理单元代码
     */
    private String cellCode;
    /**
     * 管理单元名称
     */
    private String cellName;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 有效标志
     */
    //@Dict(dictGroup = "sf_yn", inject = "validSignName")
    private String validSign;
    private String validSignName;
    /**
     * 父节点ID
     */
    private String parentId;
    /**
     * 子节点
     */
    private List<CellManageVO> sonCellManageList;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCellCodeAndName() {
        return cellCodeAndName;
    }
    public void setCellCodeAndName(String cellCodeAndName) {
        this.cellCodeAndName = cellCodeAndName;
    }
    public String getParentCell() {
        return parentCell;
    }
    public void setParentCell(String parentCell) {
        this.parentCell = parentCell;
    }
    public String getCellCode() {
        return cellCode;
    }
    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }
    public String getCellName() {
        return cellName;
    }
    public void setCellName(String cellName) {
        this.cellName = cellName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getValidSignName() {
        return validSignName;
    }
    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public List<CellManageVO> getSonCellManageList() {
        return sonCellManageList;
    }
    public void setSonCellManageList(List<CellManageVO> sonCellManageList) {
        this.sonCellManageList = sonCellManageList;
    }
}
