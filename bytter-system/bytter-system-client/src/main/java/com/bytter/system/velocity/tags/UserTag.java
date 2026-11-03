package com.bytter.system.velocity.tags;
import org.apache.velocity.tools.config.DefaultKey;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;

@DefaultKey("userTag")
public class UserTag {
    private final IUserService userServiceI;
    public UserTag() {
        this.userServiceI = ApplicationContextUtil.getContext().getBean("userServiceI", IUserService.class);
    }
 
    
    public String getLoginName() {
    	if(UserContextHelper.helper().getCurrentUser()==null) {
    		return "";
    	}
    	return UserContextHelper.helper().getCurrentUser().getLoginName();
    	
    }
    
    public String getCadnByLoginName() {
    	if(UserContextHelper.helper().getCurrentUser()==null) {
    		return "";
    	}
    	UserDTO dto = userServiceI.getByLoginName(UserContextHelper.helper().getCurrentUser().getLoginName());
    	return !ObjectUtils.isEmpty(dto) ? dto.getLoginName() : "";
    }
}
