package com.bytter.cloud.gateway.config;

import com.bytter.cloud.gateway.filter.DevLoadBalancerFilter;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterBeanConfig {

    @Bean
    public ReactiveLoadBalancerClientFilter reactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory, LoadBalancerProperties properties) {
        return new DevLoadBalancerFilter(clientFactory, properties);

    }
}
