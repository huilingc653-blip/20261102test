package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 用户-APP报表模块关联状态类
 *
 * @author chenlw
 * @since 2020-05-12
 */
@Entity
@Table(name = "USER_MOBILE_CHART_MODULE")
public class UserMobileChartModule extends IdEntity implements Serializable {
    private String userId;
    private String chartModuleKey;
    /**
     * 报表模块状态：
     * - 0，启用；
     * - 1，停用；
     */
    private Integer status;
    public UserMobileChartModule() {
    }
    public UserMobileChartModule(String userId, String chartModuleKey, Integer status) {
        this.userId = userId;
        this.chartModuleKey = chartModuleKey;
        this.status = status;
    }
    @Column(name = "USER_ID", nullable = false, length = 32)
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Column(name = "CHART_MODULE_KEY", nullable = false, length = 50)
    public String getChartModuleKey() {
        return chartModuleKey;
    }
    public void setChartModuleKey(String chartModuleKey) {
        this.chartModuleKey = chartModuleKey;
    }
    @Column(name = "STATUS", nullable = false, length = 1)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
