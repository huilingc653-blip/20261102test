package com.bytter.cloud.gateway.filter;
import cn.hutool.system.HostInfo;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.reactive.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.reactive.Request;
import org.springframework.cloud.client.loadbalancer.reactive.Response;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;
/**
 * 开发环境重写ReactiveLoadBalancerClientFilter传递clientIp
 *
 */
@Slf4j
public class DevLoadBalancerFilter extends ReactiveLoadBalancerClientFilter {
    private static final Map<String, String> LOCALHOST_MAP = new HashMap<>();
    static {
        LOCALHOST_MAP.put("localhost", "127.0.0.1");
    }
    private static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10150;
    private final LoadBalancerClientFactory clientFactory;
    private final LoadBalancerProperties properties;
    public DevLoadBalancerFilter(LoadBalancerClientFactory clientFactory, LoadBalancerProperties properties) {
        super(clientFactory, properties);
        this.clientFactory = clientFactory;
        this.properties = properties;
    }
    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }
    @Override
    @SuppressWarnings("Duplicates")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }
        // preserve the original url
        addOriginalRequestUrl(exchange, url);
        if (log.isTraceEnabled()) {
            log.trace(ReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: " + url);
        }
        return choose(exchange).doOnNext(response -> {
            if (!response.hasServer()) {
                throw NotFoundException.create(properties.isUse404(), "Unable to find instance for " + url.getHost());
            }
            ServiceInstance retrievedInstance = response.getServer();
            URI uri = exchange.getRequest().getURI();
            // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
            // if the loadbalancer doesn't provide one.
            String overrideScheme = retrievedInstance.isSecure() ? "https" : "http";
            if (schemePrefix != null) {
                overrideScheme = url.getScheme();
            }
            DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(retrievedInstance, overrideScheme);
            URI requestUrl = reconstructURI(serviceInstance, uri);
            if (log.isTraceEnabled()) {
                log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
            }
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
        }).then(chain.filter(exchange));
    }
    protected URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }
    @SuppressWarnings("deprecation")
    private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {
        URI uri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        ReactorLoadBalancer<ServiceInstance> loadBalancer = this.clientFactory.getInstance(uri.getHost(), ReactorServiceInstanceLoadBalancer.class);
        if (loadBalancer == null) {
            throw new NotFoundException("No loadbalancer available for " + uri.getHost());
        }
        return loadBalancer.choose(createRequest(exchange));
    }
    @SuppressWarnings("deprecation")
    private Request createRequest(ServerWebExchange exchange) {
        Map<String, String> hostMap = new HashMap<>();
        ServerHttpRequest request = exchange.getRequest();
        String clientIp = Optional.ofNullable(request.getRemoteAddress()).map(address -> address.getAddress().getHostAddress()).orElse("");
        if (LOCALHOST_MAP.containsKey(clientIp) || LOCALHOST_MAP.containsValue(clientIp)) {
            try {
                clientIp = Optional.ofNullable(InetAddress.getLocalHost()).map(InetAddress::getHostAddress).orElse("");
            } catch (UnknownHostException e) {
                log.error("本地网卡地址获取异常::", e);
            }
        }
        //本地启动网关
        if (clientIp.equals("0:0:0:0:0:0:0:1")){
            HostInfo hostInfo = SystemUtil.getHostInfo();
            clientIp = hostInfo.getAddress();
        }
        hostMap.put("clientIp", clientIp);
        return new DefaultRequest<>(hostMap);
    }
}
