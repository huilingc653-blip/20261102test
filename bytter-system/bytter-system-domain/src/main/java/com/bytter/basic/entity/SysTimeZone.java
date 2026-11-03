package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 系统时区表
 * @date 2020-1-19 11:14:22
 */
@Entity
@Table(name = "SYS_TIME_ZONE")
public class SysTimeZone extends IdEntity implements java.io.Serializable {
    /**
     * 时区
     */
    private String timeZone;
    @Column(name = "TIME_ZONE", nullable = false, length = 100)
    public String getTimeZone() {
        return timeZone;
    }
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
