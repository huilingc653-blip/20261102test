package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description:
 * @date 2018/6/15 9:02
 */
@Entity
@Table(name = "SYS_EXTERNALCORP_CORP")
public class ExternalCorpAndCorp extends IdEntity implements Serializable {
    /**
     * 客商资料ID
     */
    private String externalCorpId;
    /**
     * 绑定的单位ID
     */
    private String corpId;
    @Column(name = "EXTERNALCORP_ID", nullable = false, length = 32)
    public String getExternalCorpId() {
        return externalCorpId;
    }
    public void setExternalCorpId(String externalCorpId) {
        this.externalCorpId = externalCorpId;
    }
    @Column(name = "CORP_ID", nullable = false, length = 32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}
