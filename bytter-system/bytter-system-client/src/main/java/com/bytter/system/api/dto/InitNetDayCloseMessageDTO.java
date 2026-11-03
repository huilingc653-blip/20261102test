package com.bytter.system.api.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
/**
 * @author YonJar
 * @date 2023/6/28.
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InitNetDayCloseMessageDTO implements Serializable {
    private static final long serialVersionUID = -7094738277276549786L;
    private String msgId;
    private String msgType;
    private String netId;
    private String userId;
    private Date closeDate;
    private String msgSendDateTime; //消息发送时间
    public static class Msg{
        // P:表示 资金池日终结转；主要是判断 资金池和非资金池 即可
        public static final String P = "P";
        public static final String N = "N";
    }
}
