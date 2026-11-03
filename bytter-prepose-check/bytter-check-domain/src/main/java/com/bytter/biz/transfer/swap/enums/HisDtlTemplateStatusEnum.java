package com.bytter.biz.transfer.swap.enums;
/**
 * Created by zhouj on 2018/5/31.
 */
public enum  HisDtlTemplateStatusEnum {
    //状态：-2发送失败，-1打回，0暂存，1待确认，2发送中，3发送成功，10数据已结束
    ISALL(-100),//预处理 包含未处理已处理打回
    ISNULL(-10),//未处理
    SENDFAIL(-2),
    BACK(-1),
    SAVE(0),//已处理
    CONFIRM(1),
    SENDING(2),
    SUCCUESS(3),
    FINISH(10);
    HisDtlTemplateStatusEnum(Integer status){
        this.status = status;
    }
    private Integer status;
    public Integer getStatus() {
        return status;
    }
}
