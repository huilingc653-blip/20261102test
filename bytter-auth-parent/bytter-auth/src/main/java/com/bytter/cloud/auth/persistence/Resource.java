package com.bytter.cloud.auth.persistence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
/**
 * 资源信息持久对象
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    /**
     * 资源ID
     */
    private String id;
    /**
     * 资源类型
     */
    private String type;
    /**
     * 资源请求URL
     */
    private List<String> url;
    /**
     * 资源请求方法
     */
    private String method;
}
