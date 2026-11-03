package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.credit.domain.entity.RatBusinessRace;
import com.bytter.credit.domain.vo.RatItemCustomVO;
import lombok.Data;

@Data
public class RatBusinessRaceUpdateVO implements java.io.Serializable {

    private RatBusinessRace ratRace;
    private RatItemCustomVO customVOS;


}
