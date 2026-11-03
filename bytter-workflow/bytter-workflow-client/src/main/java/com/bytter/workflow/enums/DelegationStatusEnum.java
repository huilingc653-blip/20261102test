package com.bytter.workflow.enums;

public enum DelegationStatusEnum {
    // 1、暂存 2、待确认 3、打回 4、已确认 5、结束
    STORAGE(1),
    WAIT(2),
    REJEC(3),
    SURE(4),
    FINISH(5);



    DelegationStatusEnum(Integer status){
        this.status = status;
    }

    private Integer status;

    public Integer getStatus() {
        return status;
    }

}
