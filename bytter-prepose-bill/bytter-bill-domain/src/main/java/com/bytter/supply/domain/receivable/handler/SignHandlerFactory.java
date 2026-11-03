package com.bytter.supply.domain.receivable.handler;

import com.bytter.supply.enums.OnlineEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ztd
 * @version 1.0
 * @Title: SignHandlerFactory
 * @Description: 签收处理器工厂，根据onlineSign字段分发到对应的Handler
 * @since 2026/7/3
 */
@Component
public class SignHandlerFactory {

    private final Map<String, SignHandler> handlerMap;

    @Autowired
    public SignHandlerFactory(List<SignHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(SignHandler::getType, h -> h, (a, b) -> a));
    }

    /**
     * 根据onlineSign标识获取对应的签收处理器
     *
     * @param onlineSign 线上线下（0=线下，1=线上）
     * @return 对应的签收处理器
     */
    public SignHandler getHandler(String onlineSign) {
        SignHandler handler = handlerMap.get(onlineSign);
        if (handler == null) {
            throw new IllegalArgumentException("不支持的签署标识: " + onlineSign + "，请确认传入 0(线下) 或 1(线上)");
        }
        return handler;
    }

    /**
     * 判断是否为线上签署
     *
     * @param onlineSign 线上签署标识
     * @return true=线上，false=线下
     */
    public boolean isOnline(String onlineSign) {
        return OnlineEnum.ONLINE.getCode().equals(onlineSign);
    }

    /**
     * 判断是否为线下签署
     *
     * @param onlineSign 线上签署标识
     * @return true=线下，false=线上
     */
    public boolean isOffline(String onlineSign) {
        return OnlineEnum.OFFLINE.getCode().equals(onlineSign);
    }
}