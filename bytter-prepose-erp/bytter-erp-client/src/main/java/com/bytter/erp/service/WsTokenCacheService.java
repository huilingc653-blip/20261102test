package com.bytter.erp.service;

import javax.jws.WebService;

/**
 * @author lifh
 * @description
 * @createDate 2019/8/7
 */
@WebService
public interface WsTokenCacheService {
    String writeToken(String token);

    String readToken(String token);
}
