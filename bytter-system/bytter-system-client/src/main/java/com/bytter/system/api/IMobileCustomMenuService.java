package com.bytter.system.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.dto.MobileCustomMenuDTO;
import java.util.List;
/**
 * 移动端 自定义菜单 Service
 *
 * @author chenlw
 * @since 2021-01-28
 */
public interface IMobileCustomMenuService {
    /**
     * 获取【我的】自定义菜单
     *
     * @param user 当前用户
     * @return 自定义菜单列表
     */
    List<MobileCustomMenuDTO> getMineCustomMenuList(ICurrentUser user);
}
