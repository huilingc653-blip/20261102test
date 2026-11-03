package com.bytter.cloud.gateway.config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.reactive.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.reactive.Request;
import org.springframework.cloud.client.loadbalancer.reactive.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;
@Slf4j
public class DevServerInstanceLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private ObjectProvider<ServiceInstanceListSupplier> provider;
    private final String serviceId;
    public DevServerInstanceLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> provider, String serviceId) {
        this.provider = provider;
        this.serviceId = serviceId;
    }
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = provider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get().next().map(instances->this.getInstanceResponse(request,instances));
    }
    /**
     * 优先根据ip匹配否则随机
     * @param request
     * @param instances
     * @return
     */
    private Response<ServiceInstance> getInstanceResponse(Request request,List<ServiceInstance> instances) {
        Map<String,String> context = (Map<String, String>) request.getContext();
        if (instances.isEmpty()) {
            log.warn("No servers available for service: " + this.serviceId);
            return new EmptyResponse();
        }
        ServiceInstance instance = instances.stream().filter(serviceInstance -> serviceInstance.getHost().equals(context.get("clientIp")))
                .findFirst().orElse(instances.get(RandomUtils.nextInt(0, instances.size())));
        log.info("Using server: {}:{} ({})", instance.getHost(), instance.getPort(), instance.getServiceId());
        return new DefaultResponse(instance);
    }
}
