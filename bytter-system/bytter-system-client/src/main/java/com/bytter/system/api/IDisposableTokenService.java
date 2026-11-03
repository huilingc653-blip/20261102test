package com.bytter.system.api;
import java.util.List;
/**
 * Created by zhoul on 2017/8/8.
 */
public interface IDisposableTokenService {
    List<String> createToken(Integer size);
    boolean validateToken(String token);
    String createToken();
    void removeToken(String token);
    String getSessionId(String token);
}
