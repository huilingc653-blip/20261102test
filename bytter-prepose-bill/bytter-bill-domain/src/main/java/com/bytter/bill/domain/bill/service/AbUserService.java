package com.bytter.bill.domain.bill.service;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
/**
 * 票据用户处理服务类
 * @author zhengg
 *
 */
@Service("abUserService")
public class AbUserService {
	@DubboReference(check = false)
	private IUserService userService;
	/**
	 * 获取系统默认发起员
	 * @return 发起员信息
	 */
	public UserDTO getScheduler(){
		return userService.getScheduler();
	}

}
