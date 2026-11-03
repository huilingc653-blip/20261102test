package com.bytter.system.service.impl;
import com.bytter.system.api.IDisposableTokenService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import java.util.List;
@Service("DisposableTokenServiceI")
@DubboService
public class DisposableTokenServiceImpl implements IDisposableTokenService {
    @Override
    public List<String> createToken(Integer size) {
        return null;
    }
    @Override
    public boolean validateToken(String token) {
        return false;
    }
    @Override
    public String createToken() {
        return null;
    }
    @Override
    public void removeToken(String token) {
    }
    @Override
    public String getSessionId(String token) {
        return null;
    }
}
