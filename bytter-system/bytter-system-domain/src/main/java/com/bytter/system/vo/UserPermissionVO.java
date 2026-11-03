package com.bytter.system.vo;

import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户权限信息
 *
 * @author huao
 * @date 2025-08-19 16:29:35
 */
@Getter
@Setter
@Accessors(chain = true)
@ExcelTarget("UserPermissionVO")
public class UserPermissionVO extends ExcelModel implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    @Excel(name = "账号")
    private String loginName;

    @Excel(name = "姓名")
    private String username;

    /**
     * 单位id
     */
    private String corpId;

    @Excel(name = "单位编码")
    private String corpCode;

    @Excel(name = "单位名称")
    private String corpName;

    /**
     * 角色id
     */
    private String roleId;

    @Excel(name = "角色")
    private String roleName;

    @Excel(name = "权限分配时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date assignTime;

}
