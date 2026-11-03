package com.bytter.cloud.gateway.filter.xss;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * 重写Response，防止xss攻击
 *
 * @author zhoul 2021-3-8
 * @since 1.0.0
 */
@Slf4j
@Component
public class XssResponseGlobalFilter implements Ordered, GlobalFilter {
    
    // 匹配HTML标签的正则表达式，用于检测潜在的XSS内容
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>", Pattern.CASE_INSENSITIVE);

    /**
     * 检查响应数据是否包含潜在的HTML标签
     * @param content 待检查的内容
     * @return 是否包含HTML标签
     */
    private boolean containsHtmlTags(String content) {
        return HTML_TAG_PATTERN.matcher(content).find();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                String contentType = getDelegate().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
                boolean flag = MediaType.APPLICATION_JSON_VALUE.equals(contentType)
                        || MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(contentType);
                if (body instanceof Flux && flag) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        DataBufferUtils.release(join);
                        String responseData = new String(content, StandardCharsets.UTF_8);
                        //清楚xss标签
//                        if(!(responseData.indexOf("<br>")>-1 || responseData.indexOf("<br/>")>-1)) {
//                            responseData = XssCleanRuleUtils.xssClean(responseData);
//                        }
                        // 只对包含HTML标签的内容进行XSS过滤，避免对包含合法数学符号（如 <= >= < >）的内容进行误过滤
                        if (containsHtmlTags(responseData)) {
                        	 responseData = XssCleanRuleUtils.xssClean(responseData);
                        }
                        byte[] uppedContent = responseData.getBytes(StandardCharsets.UTF_8);
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }
    @Override
    public int getOrder() {
        return -50;
    }
}