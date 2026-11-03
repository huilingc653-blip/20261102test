package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 产品类型映射明细表Entity
 * @Description: CON_PD_MAPDTL合产品类型映射明细表
 * @date 2019-11-04
 */
@Entity
@Table(name = "CON_PD_MAPDTL", schema = "")
@SuppressWarnings("serial")
public class ConPdMapdtl extends IdEntity implements java.io.Serializable {
    /**
     * 主映射ID
     */
    private String mapId;
    /**
     * 合同类型明细表ID
     */
    private String typeDtlId;
    /**
     * 产品类型明细表CODE
     */
    private String pdDtlCode;
    /**
     * 方法: 主映射ID
     *
     * @return: 主映射ID
     */
    @Column(name = "MAP_ID", nullable = true, length = 32)
    public String getMapId() {
        return mapId;
    }
    /**
     * 设置 主映射ID
     */
    public void setMapId(String mapId) {
        this.mapId = mapId;
    }
    /**
     * 方法: 取得 合同类型明细表ID
     *
     * @return: 合同类型明细表ID
     */
    @Column(name = "TYPE_DTLID", nullable = true, length = 32)
    public String getTypeDtlId() {
        return typeDtlId;
    }
    /**
     * 设置 合同类型明细表ID
     */
    public void setTypeDtlId(String typeDtlId) {
        this.typeDtlId = typeDtlId;
    }
    /**
     * 方法: 取得 产品类型明细表CODE
     *
     * @return: 产品类型明细表CODE
     */
    @Column(name = "PD_DTLCODE", nullable = true, length = 50)
    public String getPdDtlCode() {
        return pdDtlCode;
    }
    /**
     * 设置 产品类型明细表CODE
     */
    public void setPdDtlCode(String pdDtlCode) {
        this.pdDtlCode = pdDtlCode;
    }
    @Override
    public String toString() {
        return "ConPdMapdtl{" + "mapId='" + mapId + '\'' + ", typeDtlId='" + typeDtlId + '\'' + ", pdDtlCode='"
                + pdDtlCode + '\'' + '}';
    }
}
