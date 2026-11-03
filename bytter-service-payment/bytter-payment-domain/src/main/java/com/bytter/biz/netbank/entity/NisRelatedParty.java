package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Description: 关联方
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/10/17
 */
@ApiModel
@Entity
@Access(AccessType.FIELD)
@Table(name = "NIS_RELATED_PARTY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NisRelatedParty extends IdEntity implements java.io.Serializable{
    @Column(name = "RELATED_CODE")
    @ApiModelProperty("关联代码")
    private String relatedCode;

    @Column(name = "RELATED_NAME")
    @ApiModelProperty("关联名称")
    private String relatedName;

    @Column(name = "status")
    @ApiModelProperty(value = "状态", example = "0")
    private Integer status;

    @Column(name = "CREATE_BY")
    @ApiModelProperty("创建人")
    private String createBy;

    @Column(name = "CREATE_DATE")
    @ApiModelProperty("创建日期")
    private Date createDate;

    @Column(name = "UPDATE_BY")
    @ApiModelProperty("更新人")
    private String updateBy;

    @Column(name = "UPDATE_DATE")
    @ApiModelProperty("更新日期")
    private Date updateDate;
}
