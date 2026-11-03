package com.bytter.guarantee.dto;

import com.bytter.core.base.entity.ICurrentUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 担保日志传输
 */
@Data
@Accessors(chain = true)
public class GuaLogDTO implements Serializable {

    private String guaId;

    private BigDecimal money;

    private String msgType;

    private String message;

    private String createBy;

    private String createByName;

    public static GuaLogDTO buildGuaLogDTO(String guaId, BigDecimal money, String messageType, String message, ICurrentUser user){
        return new GuaLogDTO().setGuaId(guaId).setMoney(money)
                .setMsgType(messageType).setMessage(message)
                .setCreateBy(user.getLoginName()).setCreateByName(user.getUsername());
    }

}
