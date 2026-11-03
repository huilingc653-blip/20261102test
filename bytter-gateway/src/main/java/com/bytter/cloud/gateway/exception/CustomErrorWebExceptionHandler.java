package com.bytter.cloud.gateway.exception;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.cloud.gateway.auth.service.IAuthService;
import com.bytter.cloud.gateway.context.GatewayBaseException;
import com.bytter.cloud.gateway.context.GatewayCodeEnum;
import com.bytter.cloud.gateway.util.StringUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 自定义web的异常错误处理器
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
@Slf4j
public class CustomErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    @Autowired
    private IAuthService authService;
    @Value("${bytter.web.microApps}")
    private List<String> microApps;


    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param resourceProperties the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     */
    public CustomErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                          ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, false);
        Throwable error = getError(request);
        log.error("网关获取异常 requestId={}", errorAttributes.get("requestId"), error);
        int status = (int) errorAttributes.get("status");
        HttpHeaders httpHeaders = request.headers().asHttpHeaders();
        String reqTerminal = httpHeaders.getFirst("ReqTerminal");
        String path = request.path();
        ServerHttpRequest httpRequest = request.exchange().getRequest();
        boolean isAjax = isAjaxRequest(httpRequest);
        switch (status) {
            case 404:
                return process404(reqTerminal,path);
            case 502:
                return processErrorType(GatewayCodeEnum.GATEWAY_ERROR);
            case 503:
                return process503((NotFoundException) error);
            case 504:
                return processErrorType(GatewayCodeEnum.GATEWAY_CONNECT_TIME_OUT);
            default:
                return processCommon(error, isAjax);
        }
    }

    private Mono<ServerResponse> processCommon(Throwable error, boolean isAjax) {
        if (error instanceof TokenException
                && ((TokenException) error).getGatewayBaseException() == GatewayCodeEnum.TOKEN_CONFLICT) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("code", GatewayCodeEnum.TOKEN_CONFLICT.getCode());
            response.put("msg", GatewayCodeEnum.TOKEN_CONFLICT.getMsg());
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(response);
        }
        //token过期重定向
        if(error instanceof TokenException||error instanceof ExpiredJwtException) {
            URI uri = URI.create("/login");
            return ServerResponse.seeOther(uri)
                    .contentType(MediaType.APPLICATION_JSON).build();
        }  else if (error instanceof AuthenticationException) {
            if (isAjax){
                // Ajax 请求 → 返回 403 JSON
                String errorJson = "{\"success\":false,\"msg\":\"当前用户尚未分配该菜单资源权限，请联系管理员授权!\"}";
                return ServerResponse.status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(errorJson);
            }
            // 普通网页请求 → 308 重定向到 /403
            URI uri = URI.create("/403");
            return ServerResponse.seeOther(uri)
                    .contentType(MediaType.APPLICATION_JSON).build();
        } else if (error instanceof RpcException) {
            return processErrorType(GatewayCodeEnum.AUTH_NOT_FOUND_SERVICE);
        } else if (error instanceof IpCheckException) {
            return processErrorType(GatewayCodeEnum.IP_CHECK_FAIL);
        }
        return processErrorType(GatewayCodeEnum.COMMON_TIPS_FAIL);
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request HTTP请求。
     */
    // 判断是否为 Ajax 请求
    private boolean isAjaxRequest(ServerHttpRequest request) {
        // 方式1：检查 X-Requested-With 头
        String requestedWith = request.getHeaders().getFirst("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        }
        // 方式2：检查 Accept 头是否包含 application/json
        String accept = request.getHeaders().getFirst("Accept");
        return accept != null && accept.contains("application/json");
    }

    private Mono<ServerResponse> process503(NotFoundException exception) {
        String reason = Optional.ofNullable(exception.getReason()).orElse("");
        String[] reasons = reason.split(" ");
        URI uri = URI.create("/503?serviceName=" + reasons[reasons.length - 1]);
        return ServerResponse.permanentRedirect(uri)
                .contentType(MediaType.APPLICATION_JSON).build();
    }

    private Mono<ServerResponse> process404(String reqTerminal, String path) {
        //校验app设置 服务链接 地址是否正确
        if (StringUtils.isNotEmpty(reqTerminal) && reqTerminal.equals("app") && path.contains("oauth2")){
            return processErrorType2(GatewayCodeEnum.IP_FAIL, HttpStatus.NOT_FOUND);
        }
        URI uri = URI.create("/404");
        // if (matchMicroApps(path)) {
        //     uri = URI.create("/index#");
        // }
        return ServerResponse.permanentRedirect(uri)
                .contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * 是否匹配微应用。
     *
     * @param uri 请求路径。
     * @return true：匹配。false：不匹配。
     */
    public boolean matchMicroApps(String uri) {
        if (ObjectUtil.isEmpty(microApps)) {
            return false;
        }
        for (String w : microApps) {
            if (uri.startsWith(w)) {
                return true;
            }
        }
        return false;
    }

    private Mono<ServerResponse> processErrorType(GatewayBaseException gatewayBaseException) {
        Map<String, Object> rt = new HashMap<>();
        rt.put("msg", gatewayBaseException.getMsg());
        rt.put("code", gatewayBaseException.getCode());
        rt.put("success", false);
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(rt));
    }

    private Mono<ServerResponse> processErrorType2(GatewayBaseException gatewayBaseException, HttpStatus httpStatus) {
        Map<String, Object> rt = new HashMap<>();
        rt.put("msg", gatewayBaseException.getMsg());
        rt.put("code", gatewayBaseException.getCode());
        rt.put("success", false);
        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(rt));
    }


}
