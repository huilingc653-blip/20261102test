package com.bytter.biz.erp.vo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 通用返回报文
 *
 * @author fangcf
 * @date 2026-05-11
 */
@Data
public class ComPageResponse implements Serializable {
    private Long totalPage;
    private Long currentPage;
    private Long pageSize;
    private List<?> list;
}
