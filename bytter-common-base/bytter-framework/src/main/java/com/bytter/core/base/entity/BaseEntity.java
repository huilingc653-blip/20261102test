package com.bytter.core.base.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
@EqualsAndHashCode(callSuper = false)
@Data
public class BaseEntity extends IdEntity{
    @TableField(fill = FieldFill.INSERT)
    protected String createBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String updateBy;
    @TableField(fill = FieldFill.INSERT)
    protected Date createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date updateDate;
}
