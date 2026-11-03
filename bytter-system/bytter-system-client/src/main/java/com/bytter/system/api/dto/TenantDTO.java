package com.bytter.system.api.dto;
import lombok.Data;
import java.util.Date;
/**
 * 租户传输对象
 *
 * @author zuoy 2021-07-05
 * @since 1.0.0
 */
@Data
public class TenantDTO {
    /**
     * 租戶编码
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 代码
     */
    private String code;
    /**
     * 状态
     */
    private String status;
    /**
     * 是否已删除：1已删除，0未删除（默认）
     */
    private String deleted;
    /**
     * 租户到期日
     */
    private Date expireDate;
}
