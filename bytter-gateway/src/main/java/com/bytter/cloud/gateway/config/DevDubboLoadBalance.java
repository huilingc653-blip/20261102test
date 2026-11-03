package com.bytter.cloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;

import java.util.List;

/**
 * 重写dubbo负载均衡策略调度到配置了环境的ip地址
 */
@Slf4j
public class DevDubboLoadBalance extends RandomLoadBalance {
    public DevDubboLoadBalance() {
    }

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        String host = url.getHost();
        for (Invoker<T> invoker : invokers) {
            if (host.equalsIgnoreCase(invoker.getUrl().getHost())) {
                return invoker;
            }
        }
        String dubboService = url.getUrlAddress().toString();
        log.info("dubbo服务【{}】负载均衡【本地实例】未找到", dubboService);
        return super.doSelect(invokers, url, invocation);
    }
}
