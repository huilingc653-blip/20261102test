package com.bytter.system.api.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;
/**
 * API资源信息传输对象
 *
 * @author chenlw 2021-04-07
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResourceDTO implements Serializable {
    /**
     * 资源ID
     */
    private String id;
    /**
     * 资源编码
     */
    private String code;
    /**
     * 鉴权模式:1登录后可访问,2角色资源授权后可访问
     */
    private String authMode;
    /**
     * 资源请求URL
     */
    private String url;
    /**
     * 资源请求URLs
     */
    private List<String> urls;
    /**
     * 资源请求方法
     */
    private String method;
}
